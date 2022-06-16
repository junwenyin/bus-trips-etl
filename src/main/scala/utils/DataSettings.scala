package utils

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.hadoop.conf.Configuration

case class DataSettings(config: Config, params: Array[String]){
  lazy val GOOGLE_API_KEY = config.getString("app.googleTranslate.key")
  lazy val GOOGLE_TRANSLATE_URL = config.getString("app.googleTranslate.url") + "?key=" + GOOGLE_API_KEY
  lazy val inputPath = config.getString("app.inputPath")
  lazy val outputPath = config.getString("app.outputPath")
}

object DataSettings {

  def load(args: Array[String] = Array.empty[String]): DataSettings = {
    val config = ConfigFactory.load("app")

    DataSettings(
      config = config,
      params = args
    )
  }

}
