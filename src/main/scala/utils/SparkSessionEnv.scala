package utils

import org.apache.spark.sql.SparkSession

object SparkSessionEnv {

  val sparkSession = SparkSession
    .builder
    .master("local")
    .appName("axa test")
    .getOrCreate()

}
