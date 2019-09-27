FROM bde2020/spark-base:2.4.3-hadoop2.7

MAINTAINER Erika Pauwels <erika.pauwels@tenforce.com>
MAINTAINER Gezim Sejdiu <g.sejdiu@gmail.com>


COPY ./spark/submit/submit.sh /
RUN chmod a+x /submit.sh

ADD ./entrypoint.sh /entrypoint.sh
RUN chmod a+x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]