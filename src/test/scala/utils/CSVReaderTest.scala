package utils

import org.scalatest.funsuite._

class CSVReaderTest extends AnyFunSuite{
  implicit val dataSettings = DataSettings.load()
  implicit val sparkSession = SparkSessionEnv.sparkSession
  import sparkSession._

  test("should read test csv file") {
    val res = CSVReader.readCSV(path= dataSettings.inputPath, delimiter = ";")
    assert(429 == res.count)
  }
}
