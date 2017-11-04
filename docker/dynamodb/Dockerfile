FROM openjdk:8-jre
MAINTAINER Abhishek Ghosh <mishu0007@gmail.com>
# Based on https://hub.docker.com/r/peopleperhour/dynamodb/~/dockerfile/

RUN mkdir /var/dynamodb_wd
RUN mkdir /var/dynamodb_local
WORKDIR /var/dynamodb_wd

RUN \
    wget -O /tmp/dynamodb_local_latest https://s3-us-west-2.amazonaws.com/dynamodb-local/dynamodb_local_latest.tar.gz && \
    tar xfz /tmp/dynamodb_local_latest && \
    rm -f /tmp/dynamodb_local_latest

# Install script
COPY start.sh /scripts/start.sh

ENTRYPOINT ["/scripts/start.sh"]
CMD ["-port", "8000"]

# Add VOLUMEs to allow backup of config, logs and databases
VOLUME ["/var/dynamodb_local", "/var/dynamodb_wd"]

# install aws cli to interact w/ dynamoDB
RUN apt-get update && \
  apt-get install -y python2.7 python-pip && \
  pip install awscli


# Default table we create on startup
ENV TABLE carAdvert

# Mocked AWS credentials (needed for dynamoDB)
ENV AWS_ACCESS_KEY_ID ""
ENV AWS_SECRET_ACCESS_KEY ""

ENV TERM xterm

# Default command
# CMD ["/scripts/start.sh"]


