FROM openjdk:8-alpine

MAINTAINER Juergen Jakobitsch <jakobitschj@semantic-web.at>

RUN apk add --update bash python3 && rm -rf /var/cache/apk/*

ADD ./kafka/kafka_2.12-2.3.0.tgz /usr/local/apache-kafka/

RUN ln -s /usr/local/apache-kafka/kafka_2.12-2.3.0 /usr/local/apache-kafka/current

RUN rm -f /tmp/kafka_2.12-2.3.0.tgz

COPY ./kafka/wait-for-step.sh /
COPY ./kafka/execute-step.sh /
COPY ./kafka/finish-step.sh /

RUN ln -s /usr/local/apache-kafka/kafka_2.12-2.3.0 /app
RUN ln -s /usr/local/apache-kafka/kafka_2.12-2.3.0/config /config

ADD ./kafka/config/kafka-startup.json /config/
ADD ./kafka/config/kafka-init.json /config/

ADD ./kafka/kafka-bin.py /app/bin/
ADD ./kafka/kafka-init /app/bin/

ADD ./entrypoint.sh /entrypoint.sh

RUN chmod a+x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
