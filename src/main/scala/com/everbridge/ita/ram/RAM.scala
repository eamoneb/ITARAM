/** 
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

import scala.concurrent.duration._

import org.apache.spark._
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.Row
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.SparkSession
import org.apache.spark.storage.StorageLevel
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming._
import org.apache.spark.sql.types._

/**
  * 
  */
object RAM {
        
// SparkSession is required to create Spark SQL DataFrames
val warehouseLocation = "file:${system:user.dir}/spark-warehouse"
val spark = SparkSession
  .builder()
  .master("local") // run locally within sbt, not Spark cluster
  .appName("RAM")
  //.config("spark.sql.warehouse.dir", warehouseLocation)
  .config("spark.sql.parquet.cacheMetadata", false)
  .getOrCreate()
         
  def main(args: Array[String]) {   
  }
}

