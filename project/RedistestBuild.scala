import sbt._
import sbt.Keys._

object RedistestBuild extends Build {

  lazy val redistest = Project(
    id = "redistest",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "redistest",
      organization := "com.janrain",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.9.2",
      scalacOptions += "-deprecation",
      initialize ~= { _ =>
        System.setProperty("REDIS_SERVER_PRIMARY", "localhost:6379")
        System.setProperty("REDIS_SERVER_READS", "localhost:6379")
//        System.setProperty("", "")
      },
      selectMainClass := None,
      javaOptions in test += "-DREDIS_SERVER_PRIMARY=localhost:6379 -DREDIS_SERVER_READS=localhost:6379",
      fork in test := true,
      libraryDependencies ++= Seq(
        // Redis
        "net.debasishg" % "redisclient_2.9.2" % "2.7",
        // Jedis
        "redis.clients" % "jedis" % "2.1.0",
        // logging
        "org.slf4j" % "jcl-over-slf4j" % "1.6.6" % "runtime",
        "org.slf4j" % "slf4j-log4j12" % "1.6.6" % "runtime" force(),
        "org.slf4j" % "slf4j-api" % "1.6.6" force(),
        "log4j" % "log4j" % "1.2.16",
        // backplane server dependencies needed for message conversion
        "com.janrain.commons.supersimpledb" % "commons-supersimpledb" % "1.0.27",
        "com.yammer.metrics" % "metrics-core" % "2.1.2",
        "com.netflix.curator" % "curator-recipes" % "1.1.15",
        // Test
        "org.scalatest" %% "scalatest" % "1.6.1"
      ),
      resolvers ++= Seq(
        "janrain-repo" at "https://repository-janrain.forge.cloudbees.com/release"
//        "Sonatype Releases" at "https://oss.sonatype.org/content/groups/public/"
//        "twitter-maven-repository" at "http://maven.twttr.com/"
      ),
      testOptions in Test += Tests.Argument("-oD")
    )
  )
}
