package datamodel
import spray.json.{ DefaultJsonProtocol, JsonFormat }

case class GTranslateResponse(data: GTranslateData)
case class GTranslateData(translations: Seq[GTranslation])
case class GTranslation(translatedText: String)

object GTranslateResponseSchema extends DefaultJsonProtocol {
  implicit val gTranslationFormat: JsonFormat[GTranslation] = jsonFormat1(GTranslation.apply)
  implicit val gTranslateDataFormat: JsonFormat[GTranslateData] = jsonFormat1(GTranslateData.apply)
  implicit val gTranslateResponseFormat: JsonFormat[GTranslateResponse] = jsonFormat1(GTranslateResponse.apply)
}
