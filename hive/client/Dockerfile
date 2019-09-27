FROM arleicho/haddop-hive:1.0.0-hadoop3.1.2

MAINTAINER Arley Wilches Marcelo <arley.wilches@gmail.com>

COPY ./startup.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/startup.sh

ADD ./mappings.sh /mappings.sh
RUN chmod +x /mappings.sh

ADD ./entrypoint.sh /entrypoint.sh
RUN chmod a+x /entrypoint.sh

ENTRYPOINT ["entrypoint.sh"]