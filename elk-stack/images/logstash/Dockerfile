FROM docker.elastic.co/logstash/logstash:7.10.1

#ADD ./logstash-conf/*.conf ${LOGSTASH_PATH_CONF}/conf.d
#RUN rm -f /usr/share/logstash/pipeline/logstash.conf
#RUN rm -f /usr/share/logstash/pipeline/*
ADD pipeline/*.conf /usr/share/logstash/pipeline/
ADD config /usr/share/logstash/config/

RUN bin/logstash-plugin install logstash-filter-rest
#RUN bin/logstash-plugin install logstash-input-kafka
