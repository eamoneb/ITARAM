ITARAM
======

TODO - description

First-time Setup
----------------

1. Copy `sample-overrides.conf` to `overrides.conf`. It will be git-ignored.

2. Edit the values in `overrides.conf` for your environment. Example `overrides.conf`:

       include classpath("application.conf")
       
       postgres {
         jar = "/Users/cory.veilleux/.ivy2/cache/org.postgresql/postgresql/bundles/postgresql-42.1.4.jar"
         database = "cory.veilleux"
         user = "cory.veilleux"
         password = ""
       }
       
       libsvm {
         datafile = "/Users/cory.veilleux/Documents/ml/spark/data/mllib/sample_kmeans_data.txt"
       } 

Running
-------

1. Via `spark-submit`:

       sbt package && \
       spark-submit \
           --driver-class-path $(find $HOME/.ivy2 \( -name "mongo-spark-connector_2.11-2.2.0.jar" -o -name "postgresql-42.1.4.jar" -o -name "config-1.3.1.jar" -o -name "mongo-java-driver-3.4.2.jar" \) | tr '\n' ':') \
           --class com.everbridge.ita.ram.RAM \
           --driver-java-options='-Dconfig.file=./overrides.conf' \
           --master local target/scala-2.11/ita-ram_2.11-1.0.jar

2. Via `sbt run`:

       sbt run
