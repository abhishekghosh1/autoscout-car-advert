package services.storage
import scala.util.Try

trait StorageDriverTrait {

  /**
    * Returns all adverts
    */
  def showAll() : Try[Seq[StorableTrait]]
  /**
    * Returns advert by id
    */
  def show(id: String): Try[StorableTrait]

  /**
    * Delete advert by id
    */
  def delete(id: String): Try[Unit]

  /**
    * Create Advert. Create throws an exception if the advert already exists
    */
  def create(advert: StorableTrait): Try[StorableTrait]

  /**
    * Update and existing Advert.
    */
  def update(update: StorableTrait): Try[StorableTrait]

}
