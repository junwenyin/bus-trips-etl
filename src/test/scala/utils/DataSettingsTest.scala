package utils

import org.scalatest.funsuite._

class DataSettingsTest extends AnyFunSuite{

  test("should load the right config") {
    val path = "src/test/resources/input/data.csv"
    val dataSettings = DataSettings.load()

    assert("src/test/resources/input/data.csv" == dataSettings.inputPath)
    assert("target/output" == dataSettings.outputPath)
    assert("https://translation.googleapis.com/language/translate/v2?key=AIzaSyCrmwWditz2cxvHjoWq94mz2BnfqTYqi0I" == dataSettings.GOOGLE_TRANSLATE_URL)
  }
}
