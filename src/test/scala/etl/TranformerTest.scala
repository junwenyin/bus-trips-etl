package etl

import org.scalatest.funsuite._
import utils.CSVReader
import utils.SparkSessionEnv
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}
import datamodel.BusPostion
import utils.DataSettings

class TranformerTest extends AnyFunSuite{
  implicit val sparkSession = SparkSessionEnv.sparkSession
  implicit val dataSettings = DataSettings.load()
  import sparkSession._

  test("should clean test csv file") {
    val path = "src/test/resources/input/data.csv"
    val df = CSVReader.readCSV(path= path, delimiter = ";")
    val cleanedDF = Tranformer.clean(df)
    assert(253 == cleanedDF.count)
  }

  test("should rename dataframe correctly") {

    val path = "src/test/resources/input/data.csv"
    val df = CSVReader.readCSV(path= path, delimiter = ";")
    val nameMap = Map("Etat" -> "State", "Ligne (ID)" -> "Line (ID)", "Ligne (nom court)" -> "Line (name short)")
    val renamedDF = Tranformer.rename(df, nameMap)

    assert(true == renamedDF.columns.contains("State"))
    assert(true == renamedDF.columns.contains("Line (ID)"))
    assert(true == renamedDF.columns.contains("Line (name short)"))
    assert(false == renamedDF.columns.contains("Etat"))
    assert(false == renamedDF.columns.contains("Ligne (ID)"))
    assert(false == renamedDF.columns.contains("Ligne (nom court)"))
  }

  test("should run correctly") {
    val inputPath = "src/test/resources/input/data.csv"
    val outputPath = "target/output"
    Tranformer.run
    val output = CSVReader.read[BusPostion](path = outputPath, delimiter = ",")
    assert(253 == output.count)
  }

  test("should find the line with most bus running correctly") {
    val inputPath = "src/test/resources/input/cleanedData.csv"
    val cleanedDS = CSVReader.read[BusPostion](path = inputPath, delimiter = ",")
    val (line, nb) = Tranformer.findMostBusLine(cleanedDS)

    assert("0001" == line)
    assert(15 == nb)
  }

  test("should find find destination contains Saint correctly") {
    val inputPath = "src/test/resources/input/cleanedData.csv"
    val cleanedDS = CSVReader.read[BusPostion](path = inputPath, delimiter = ",")
    val res = Tranformer.findDestinationByName(cleanedDS, "Saint")
    assert(9 == res.size)
  }

  test("should find find nearest bus near tour eiffel") {
    val inputPath = "src/test/resources/input/cleanedData.csv"
    val cleanedDS = CSVReader.read[BusPostion](path = inputPath, delimiter = ",")
    val res = Tranformer.findNearestBusByLatLon(cleanedDS, (48.858370, -2.294481))
    println(res)
    assert("1205387608" == res.busId)
  }
}
