package utils

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.types.StructType
import scala.reflect.runtime.universe._
import org.apache.log4j.Logger

object CSVReader {
  val logger = Logger.getLogger(this.getClass)

  def readCSV(path: String, header:String = "true", delimiter:String = ",", encoding:String = "UTF-8")(implicit sparkSession: SparkSession) : DataFrame = {
    logger.info("read from path: " + path)
    sparkSession
      .read
      .option("header",header)
      .option("delimiter", delimiter)
      .option("encoding", encoding)
      .csv(path)
  }

  def read[T <: Product](path: String, header:String = "true", delimiter:String = ",", encoding:String = "UTF-8")(implicit sparkSession: SparkSession, tag: TypeTag[T]) : Dataset[T] = {
    import sparkSession.implicits._
    logger.info("read from path: " + path)

    sparkSession
      .read
      .option("header", header)
      .option("delimiter", delimiter)
      .option("encoding", encoding)
      .option("inferSchema", "false")
      .schema(ScalaReflection.schemaFor[T].dataType.asInstanceOf[StructType])
      .csv(path)
      .as[T]
  }
}
