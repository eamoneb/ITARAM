ITARAM
======

TODO - description

Running
-------

1. Via `spark-submit`:

    sbt package && \
    spark-submit \
        --driver-class-path $(find $HOME/.ivy2 \( -name "mongo-spark-connector_2.11-2.2.0.jar" -o -name "postgresql-42.1.4.jar" -o -name "config-1.3.1.jar" \) | tr '\n' ':') \
        --class com.everbridge.ita.ram.RAM \
        --driver-java-options='-Dconfig.file=./overrides.conf' \
        --master local target/scala-2.11/ita-ram_2.11-1.0.jar

2. Via `sbt run`:

    TODO - need to figure this out
