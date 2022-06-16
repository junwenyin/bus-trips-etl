name := "axa-test"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.3",
  "org.apache.spark" %% "spark-sql" % "2.4.3",
  "org.apache.commons" % "commons-io" % "1.3.2",
  "org.apache.httpcomponents" % "httpclient" % "4.5.2",
  "org.slf4j" % "slf4j-api" % "1.7.22",
  "io.spray" %%  "spray-json" % "1.3.5",
  "org.scalatest" %% "scalatest" % "3.2.7",
  "com.typesafe" % "config" % "1.3.1"
)

