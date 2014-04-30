name := """BI event ETL tool"""

version := "1.0"

scalaVersion := "2.10.2"

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= {
  val astyanax_version = "1.56.48"
  Seq(
    //"com.datastax.cassandra" % "cassandra-driver-core" % "2.0.1",
    "com.netflix.astyanax" % "astyanax-thrift" % astyanax_version,
    "com.netflix.astyanax" % "astyanax-core" % astyanax_version,
    "com.netflix.astyanax" % "astyanax-cassandra" % astyanax_version
  )
}
