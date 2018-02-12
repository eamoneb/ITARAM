/**
  * TODO complete these comments and doc tags
  * @brief A service for...
  *
  * @details
  *
  * A service that ...
  * @example fooo...
  * @usage sbt run ...
  * @author ...
  * @date 
  * @file RAM.scala
  */
package com.everbridge.ita.ram

import org.apache.spark._
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession
import org.apache.spark.storage.StorageLevel

object RAM {
  //val warehouseLocation = "file:${system:user.dir}/spark-warehouse"
  val warehouseLocation = "spark-warehouse"
  // SparkSession is required to create Spark SQL DataFrames
  val spark = SparkSession
    .builder()
    .master("local") // run locally within sbt, not Spark cluster
    .appName("RAM")
    .config("spark.driver.extraClassPath", "/Users/eamon.oneill/Library/Application\\ Support/Postgres/var-10/postgresql-42.2.1.jar")
    .config("spark.executor.extraClassPath", "/Users/eamon.oneill/Library/Application\\ Support/Postgres/var-10/postgresql-42.2.1.jar")
    .config("spark.mongodb.input.uri", "mongodb://127.0.0.1/itaram.incidentCollection")
    .config("spark.sql.warehouse.dir", warehouseLocation)
    .config("spark.sql.parquet.cacheMetadata", false)
    .getOrCreate()

  def main(args: Array[String]): Unit = {
    cluster()

    /*
    * Before doing this I executed the SQL statements, that are in the sql
    * sub-directory to create a postgres data store and insert data
    */
    // TODO remove hard-coding of dbtable, url, etc.
    val jdbcDF = spark.read
      .format("jdbc")
      .option("driver", "org.postgresql.Driver")
      .option("url", "jdbc:postgresql://localhost:5432/eamon.oneill")
      .option("dbtable", "public.incident")
      .option("user", "eamon.oneill")
      .option("password", "")
      .load()

    jdbcDF.persist(StorageLevel.MEMORY_ONLY)
    jdbcDF.show
    jdbcDF.printSchema

    // one way of querying postgres - use Dataset select method
    val incidentTitle1DF = jdbcDF.select("title")
    incidentTitle1DF.show

    // alternate way of querying postgres - use SQL statement
    jdbcDF.createOrReplaceTempView("incidentView")
    val incidentTitle2DF = spark.sql(
      s"""SELECT title FROM incidentView where id == 1""")
    incidentTitle2DF.show

    /*
    * Before doing this I executed the following at the command line, to create
    * a mongo data store and insert data
    *
    * mongo
    * use itaram
    * db.incidentCollection.insertOne( { id : 1 , title: "baz" } )
    * db.incidentCollection.insertOne( { id : 2 , title: "qux" } )
    *
    */
    import com.mongodb.spark._
    val NoSQLDF = MongoSpark.load(spark)
    NoSQLDF.show
    NoSQLDF.printSchema()

    //https://docs.mongodb.com/spark-connector/master/scala/datasets-and-sql
    // one way of querying MongoDB - use Dataset select method
    val incidentTitle3DF = NoSQLDF.select("title")
    incidentTitle3DF.show

    // alternate way of querying MongoDB - use SQL statement
    NoSQLDF.createOrReplaceTempView("incidentNoSQLView")
    val incidentTitle4DF = spark.sql(
      s"""SELECT title FROM incidentNoSQLView where id == 1""")
    incidentTitle4DF.show
  }

  def cluster() : Unit = {
    import org.apache.spark.ml.clustering.KMeans

    // Loads data.
    val dataset = spark.read.format("libsvm").load("/Users/eamon.oneill/spark/data/mllib/sample_kmeans_data.txt")

    // Trains a k-means model.
    val kmeans = new KMeans().setK(2).setSeed(1L)
    val model = kmeans.fit(dataset)

    // Evaluate clustering by computing Within Set Sum of Squared Errors.
    val WSSSE = model.computeCost(dataset)
    println(s"Within Set Sum of Squared Errors = $WSSSE")

    // Shows the result.
    println("Cluster Centers: ")
    model.clusterCenters.foreach(println)
  }
}

