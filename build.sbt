name := "ITA RAM"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "com.typesafe" % "config" % "1.3.1"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.2.0"
libraryDependencies += "org.postgresql" % "postgresql" % "42.1.4"
libraryDependencies += "org.mongodb.spark" %% "mongo-spark-connector" % "2.2.0"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.2.0"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.1.0"

// This is here to add the TypeSafe config.file system property so that
// we can use the default values in application.conf and override them
// with an overrides.conf in each environment.
//
// important to use ~= so that any other initializations aren't dropped
// the _ discards the meaningless () value previously assigned to 'initialize'
// (from https://stackoverflow.com/a/25361855)
initialize ~= { _ =>
  System.setProperty( "config.file", "overrides.conf" )
}
