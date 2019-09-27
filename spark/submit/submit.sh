    #!/bin/bash

export SPARK_MASTER_URL=spark://${SPARK_MASTER_NAME}:${SPARK_MASTER_PORT}
export SPARK_HOME=/spark
export START_APPLICATION_JAVA_LOCATION
export SPARK_APPLICATION_JAVA_USER_LOCATION
export SPARK_APPLICATION_JAVA_BEHAVIOR_LOCATION


/wait-for-step.sh

/execute-step.sh
if [ -f "${SPARK_APPLICATION_JAR_LOCATION}" ]; then
    
    echo "Submit application ${SPARK_APPLICATION_JAR_LOCATION} with main class ${SPARK_APPLICATION_MAIN_CLASS} to Spark master ${SPARK_MASTER_URL}"
    echo "Passing arguments ${SPARK_APPLICATION_ARGS}"
    /spark/bin/spark-submit \
        --class ${SPARK_APPLICATION_MAIN_CLASS} \
        --master ${SPARK_MASTER_URL} \
        ${SPARK_SUBMIT_ARGS} \
        ${SPARK_APPLICATION_JAR_LOCATION} ${SPARK_APPLICATION_ARGS}
else
    if [ -f "${SPARK_APPLICATION_PYTHON_LOCATION}" ]; then
        echo "Submit application ${SPARK_APPLICATION_PYTHON_LOCATION} to Spark master ${SPARK_MASTER_URL}"
        echo "Passing arguments ${SPARK_APPLICATION_ARGS}"
        PYSPARK_PYTHON=python3 /spark/bin/spark-submit \
            --master ${SPARK_MASTER_URL} \
            ${SPARK_SUBMIT_ARGS} \
            ${SPARK_APPLICATION_PYTHON_LOCATION} ${SPARK_APPLICATION_ARGS}
    else
        if [ -f "${START_APPLICATION_JAVA_LOCATION}" ]; then
            echo "Submit application ${START_APPLICATION_JAVA_LOCATION} to local enviroment"
            echo "Passing arguments ${SPARK_APPLICATION_ARGS}"
            java -cp ${START_APPLICATION_JAVA_LOCATION} ${SPARK_APPLICATION_JAVA_INIT}
            nohup java -cp ${START_APPLICATION_JAVA_LOCATION} ${SPARK_APPLICATION_JAVA_USER} >/dev/null 2>&1 & 
            nohup java -cp ${START_APPLICATION_JAVA_LOCATION} ${SPARK_APPLICATION_JAVA_BEHAVIOR} >/dev/null 2>&1 & 
            tail -f /dev/null
        fi
    fi
fi
/finish-step.sh