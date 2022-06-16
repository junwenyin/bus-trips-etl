import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{ DataFrame, SparkSession, SaveMode }
import utils.{ CSVReader, GoogleAPIHelper, DataSettings, SparkSessionEnv }
import etl.Tranformer

object Main {
  def main(args: Array[String]): Unit = {
    implicit val dataSettings = DataSettings.load(args)
    implicit val sparkSession = SparkSessionEnv.sparkSession

    Tranformer.run(dataSettings, sparkSession)
  }
}
