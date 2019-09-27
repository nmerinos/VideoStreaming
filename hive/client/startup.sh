#!/bin/bash


cd $HIVE_HOME/bin

if [ -f "${HIVE_CLIENT_SQL}" ]; then
	echo "Executing sql file on hive: ${HIVE_CLIENT_SQL}"
	./hive -f ${HIVE_CLIENT_SQL}
	
	if [ -f "${ELASTICSEARCH_INIT}" ]; then
		echo "Executing Elastich startup file on hive: ${ELASTICSEARCH_INIT}"
		# chmod a+x ${ELASTICSEARCH_INIT}
		# sh ${ELASTICSEARCH_INIT}
	fi

	tail -f /dev/null
fi
