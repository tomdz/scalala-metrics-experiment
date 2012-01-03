name := "scalala-metrics-experiment"

version := "0.0.1"

scalaVersion := "2.9.1"

libraryDependencies  ++= Seq(
            // other dependencies here
            "org.scalala" %% "scalala" % "1.0.0.RC2",
            "org.apache.commons" % "commons-math" % "2.2",
            "com.yammer.metrics" % "metrics-core" % "2.0.0-BETA19-SNAPSHOT"
)

resolvers ++= Seq(
            "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
            // other resolvers here
            "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/",
            "ScalaNLP Maven2" at "http://repo.scalanlp.org/repo",
            Classpaths.typesafeResolver
)

javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

scalacOptions ++= Seq("-no-specialization","-deprecation")
