package utils

import org.slf4j.LoggerFactory
import spray.json.{ JsonReader, enrichString }

import org.apache.http.client.methods.{ CloseableHttpResponse, HttpGet, HttpPost }
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.commons.io.IOUtils
import scala.util.{ Failure, Success, Try }
import org.slf4j.LoggerFactory

object HttpClientFetcher {
  val httpClient = HttpClients.createDefault()

  def fetch(url: String, entity: String): Try[String] = {
    Try {
      println(s"Will fetch from $url...")
      val stringEntity = new StringEntity(entity)
      val httpPost = new HttpPost(url)
      httpPost.addHeader("content-type", "application/json")
      httpPost.setEntity(stringEntity)
      val response = httpClient.execute(httpPost)

      try {
        val statusLine = response.getStatusLine
        val entity = response.getEntity
        val encoding = Option(entity.getContentEncoding).map(_.getValue).getOrElse("UTF-8")
        val outputContent = Try(IOUtils.toString(response.getEntity.getContent, encoding))
        if (statusLine.getStatusCode == 200) {
          println(s"Fetched from $url !")
          outputContent
        } else {
          val message = s"$url is not available, status code is ${statusLine.getStatusCode} with reason '${statusLine.getReasonPhrase}', and output content '$outputContent'"
          println(message)
          Failure(new Exception(message))
        }
      } finally {
        response.close()
      }
    }.flatten
  }

}
