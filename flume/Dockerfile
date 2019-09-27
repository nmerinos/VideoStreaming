FROM openjdk:8-alpine
MAINTAINER Alex Wilson a.wilson@alumni.warwick.ac.uk

RUN apk add --update bash python3 && rm -rf /var/cache/apk/*

ENV PATH /opt/flume/bin:$PATH

ADD ./flume/apache-flume-1.8.0-bin.tar.gz /usr/local/apache-flume/
RUN ln -s /usr/local/apache-flume/apache-flume-1.8.0-bin/ /opt/flume
ADD ./flume/start-flume.sh /opt/flume/bin/start-flume

ADD ./entrypoint.sh /entrypoint.sh
RUN chmod a+x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]