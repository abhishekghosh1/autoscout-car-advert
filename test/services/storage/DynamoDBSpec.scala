package services.storage

import awscala.dynamodbv2._
import com.amazonaws.services.{dynamodbv2 => aws}
import org.scalatest.BeforeAndAfter
import org.scalatestplus.play._
import services.storage.{DynamoDBResource => Service, SimpleStorable => Storable}

/**
  * NOTE: A DynamoDB database must be runinng locally on port 8000 for these tests to work (see ./docker/dynamoDB/README.md)
  */
class DynamoDBSpec extends PlaySpec with BeforeAndAfter with OneAppPerSuite {

  val service: Service = app.injector.instanceOf[Service]


  val mockAdvertData = Seq("title" -> "BMW M3", "fuel" -> "Diesel", "price" -> 55, "mileage" -> 2000, "new" -> false, "first_registration" -> "2016-05-12")
  val mockAdvertData2 = Seq("title" -> "Ferrari", "fuel" -> "Gasoline", "price" -> 2500, "new" -> true)
  /**
    * Recreate database after each test
    * https://github.com/seratch/AWScala/blob/master/src/test/scala/awscala/DynamoDBV2Spec.scala#L18
    */
  before {

    // Initialize client and read table
    implicit val dynamoDB: DynamoDB = service.dynamoDB
    // destroy table
    dynamoDB.table(service.tableName).get.destroy()

    // Create fresh table
    val createdTableMeta: TableMeta = dynamoDB.createTable(
      name = service.tableName,
      hashPK = "id" -> AttributeType.String
    )

    // Wait for table to be activated
    println(s"Waiting for DynamoDB table activation...")
    var isTableActivated = false
    while (!isTableActivated) {
      dynamoDB.describe(createdTableMeta.table).foreach { meta =>
        isTableActivated = meta.status == aws.model.TableStatus.ACTIVE
      }
      Thread.sleep(1000L)
      print(".")
    }
    println("Created DynamoDB table has been activated.")

    // Populate database w/ test data
    service.create(Storable("1", mockAdvertData))
    service.create(Storable("2", mockAdvertData))
    service.create(Storable("3", mockAdvertData2))
    service.create(Storable("foobar", mockAdvertData2))
  }

  "Storage service" must {

    "return all items" in {
      assert(service.showAll().get.nonEmpty)
    }

    "retrieve an item" in {
      assert(service.show("foobar").isSuccess)
    }

    "retrieve an item and hydrate an advert successfully " in {
      // Create a valid ad entry
      service.create(Storable("a_random_id", mockAdvertData2))

      // Fetch add entry
      val entry = service.show("a_random_id")

      // Check hydration is succesful
      assert(Converter.toAdvert(entry.get).isSuccess)
    }

    "throw when item is not found" in {
      assert(service.show("notAnExistingKey").isFailure)
    }

    "throw when attempting to create an item that already exists" in {
      assert(service.create(Storable("1", mockAdvertData)).isFailure)
    }

    "delete an item" in {
      val existsBeforeDeletion = service.show("1").isSuccess
      service.delete("1")
      val doesntExistAfterDeletion = service.show("1").isFailure
      assert(existsBeforeDeletion && doesntExistAfterDeletion)
    }

  }

}

