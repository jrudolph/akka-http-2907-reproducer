scalaVersion := "2.13.0"
val akkaVersion = "2.5.23"
val akkaHttpVersion = "10.1.8"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion
)

fork in run := true