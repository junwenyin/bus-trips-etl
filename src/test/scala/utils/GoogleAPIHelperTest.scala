package utils

import org.scalatest.funsuite._
import datamodel._
import datamodel.GTranslateResponseSchema._

class GoogleAPIHelperTest extends AnyFunSuite{
  implicit val dataSettings = DataSettings.load()

  test("should read GTranslateResponse from json") {
    val json =
      """
        |{
        |  "data": {
        |    "translations": [
        |      {
        |        "translatedText": "sky"
        |      }
        |    ]
        |  }
        |}
        |""".stripMargin

    val res = GoogleAPIHelper.readFromJson[GTranslateResponse](json)
    assert(res == GTranslateResponse(GTranslateData(Seq(GTranslation("sky")))))
  }

  test("should return the right translation") {
    assert(Some("sky") == GoogleAPIHelper.translate("ciel"))
  }

}
