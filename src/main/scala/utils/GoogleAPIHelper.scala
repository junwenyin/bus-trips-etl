package utils
import datamodel.GTranslateResponse
import datamodel.GTranslateResponseSchema._
import spray.json.{ JsonReader, enrichString }
import org.slf4j.LoggerFactory

object GoogleAPIHelper {
  val logger = LoggerFactory.getLogger(this.getClass)

  val API_KEY = "XXXXX"
  val BASE_URL = "https://translation.googleapis.com/language/translate/v2?key=" + API_KEY

  def translate(queryText: String, source: String = "fr", target: String = "en")(implicit dataSettings : DataSettings): Option[String] = {
    val entity = s"""
        {
            "q":"$queryText",
            "source":"$source",
            "target":"$target"
        }
        """
    val response = HttpClientFetcher.fetch(dataSettings.GOOGLE_TRANSLATE_URL, entity)
    logger.info(response.toString)
    response.map(r => readFromJson[GTranslateResponse](r).data.translations.map(_.translatedText).headOption).toOption.flatten
  }

  def readFromJson[T: JsonReader](value: String): T = {
    try {
      value.parseJson.convertTo[T]
    } catch {
      case e: Exception ⇒
        logger.error("Failed to parse " + value, e)
        throw e
    }
  }
}
