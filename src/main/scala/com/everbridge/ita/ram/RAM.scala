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

//import scala.concurrent.duration._
import org.apache.spark._
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession
import org.apache.spark.storage.StorageLevel
//import org.apache.spark.sql.functions._
//import org.apache.spark.sql.types._

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
    .config("spark.sql.warehouse.dir", warehouseLocation)
    .config("spark.sql.parquet.cacheMetadata", false)
    .getOrCreate()
         
  def main(args: Array[String]): Unit = {
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

    // one way of querying - use Dataset select method
    val incidentTitle1DF = jdbcDF.select("title")
    incidentTitle1DF.show

    // alternate way of query use SQL statement
    jdbcDF.createOrReplaceTempView("incidentView")
    val incidentTitle2DF = spark.sql(
      s"""SELECT title FROM incidentView where id == 1""")
    incidentTitle2DF.show
  }
}

