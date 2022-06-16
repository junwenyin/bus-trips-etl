package etl

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Column, DataFrame, Dataset, SaveMode, SparkSession}
import org.apache.spark.sql.functions.col
import utils.{CSVReader, GoogleAPIHelper, DistanceHelper, DataSettings}
import datamodel.BusPostion
import org.apache.log4j.Logger

object Tranformer {
  val logger = Logger.getLogger(this.getClass)

  def run(implicit dataSettings:DataSettings, sparkSession: SparkSession): Unit = {

    logger.info("start to run transformer")
    val df = CSVReader.readCSV(path = dataSettings.inputPath, delimiter = ";")

    logger.info("clean data")
    val cleanedDF = Tranformer.clean(df)

    val nameMap = df.columns.toSeq.flatMap(name => GoogleAPIHelper.translate(name).toSeq.map(newName => (name, newName))).toMap

    logger.info("rename dataframe")
    val renamedDF = Tranformer.rename(cleanedDF, nameMap)

    logger.info("save to path: " + dataSettings.outputPath)
    renamedDF
      .write
      .mode(SaveMode.Overwrite)
      .option("delimiter", ",")
      .option("header","true")
      .format("csv")
      .save(dataSettings.outputPath)
  }

  val VALID_STATE = "En ligne"
  def clean(df: DataFrame)(implicit sparkSession: SparkSession): DataFrame = {
    // need more data format check here
    df.filter(df("Etat") === VALID_STATE).na.drop
  }

  def rename(df: DataFrame, nameMap: Map[String, String])(implicit sparkSession: SparkSession): DataFrame = {
    val oldColumnNames: Seq[String] = df.columns.toSeq
    val renamedColumns: Seq[Column] = oldColumnNames.map(name =>{
      val newName = nameMap.get(name).getOrElse(name)
      col(name).as(newName)
    })
    df.select(renamedColumns : _*)
  }


  def findMostBusLine(dataset: Dataset[BusPostion])(implicit sparkSession: SparkSession): (String, Int) = {
    import sparkSession.implicits._

    dataset
      .groupByKey(_.lineId)
      .mapGroups{
        case (lineId, grps) => (lineId, grps.toSeq.map(_.busId).distinct.size)
      }.reduce( (line1, line2) => if(line1._2 > line2._2) line1 else line2)
  }

  def findDestinationByName(dataset: Dataset[BusPostion], name: String)(implicit sparkSession: SparkSession): Seq[String] = {
    import sparkSession.implicits._

    dataset.map(_.dest).distinct.filter(_.toLowerCase.contains(name.toLowerCase)).collect.toSeq
  }

  def findNearestBusByLatLon(dataset: Dataset[BusPostion], latlon: (Double, Double))(implicit sparkSession: SparkSession): BusPostion = {
    import sparkSession.implicits._
    dataset.map(b => {
      val gps = b.latlon.split(",")
      val (lat2, lon2) = (gps(0).toDouble, gps(1).toDouble)
      (b, DistanceHelper.getDistance(lat1 = latlon._1, lon1 = latlon._2, lat2 = lat2, lon2 = lon2))
    }).reduce( (pair1, pair2) => if(pair1._2 < pair2._2) pair1 else pair2)._1
  }
}
