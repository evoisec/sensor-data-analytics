ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.15"

lazy val root = (project in file("."))
  .settings(
    name := "s1"
  )

name := "Sensor Data Analytics"

version := "1.0"

scalaVersion := "2.12.15"

val sparkVersion = "3.2.1"

//libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.2.1"

libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion