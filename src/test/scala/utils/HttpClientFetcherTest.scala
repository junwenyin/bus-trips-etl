package utils

import org.scalatest.funsuite._
class HttpClientFetcherTest extends AnyFunSuite{
  test("should read send httppost request") {
    val json =
      """
        |{
        |  "q": "ciel",
        |  "source": "fr",
        |  "target":"en"
        |}
        |""".stripMargin

    val res = HttpClientFetcher.fetch(GoogleAPIHelper.BASE_URL, json)
    assert(res.isSuccess == true)
  }
}
