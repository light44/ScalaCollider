name         := "ScalaCollider"

version      := "1.11.1"

organization := "de.sciss"

scalaVersion := "2.10.0" // for FUCK'S SAKE, SI-7436!!! Scala broken through out 2.10.x except for 2.10.0

crossScalaVersions := Seq("2.11.0-RC3", "2.10.0")

description  := "A sound synthesis library for the SuperCollider server"

homepage     := Some(url("https://github.com/Sciss/" + name.value))

licenses     := Seq("GPL v2+" -> url("http://www.gnu.org/licenses/gpl-2.0.txt"))

lazy val ugensVersion     = "1.8.+"

lazy val oscVersion       = "1.1.2+"

lazy val audioFileVersion = "1.4.1+"

lazy val processorVersion = "0.2.+"

lazy val scalaTestVersion = "2.1.2"

libraryDependencies ++= Seq(
  "de.sciss"      %% "scalaosc"                % oscVersion,
  "de.sciss"      %% "scalaaudiofile"          % audioFileVersion,
  "de.sciss"      %% "processor"               % processorVersion,
  "de.sciss"      %% "scalacolliderugens-core" % ugensVersion,
  "org.scalatest" %% "scalatest"               % scalaTestVersion % "test"
)

retrieveManaged := true

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

scalacOptions ++= Seq("-Xelide-below", "INFO")     // elide debug logging!

// ---- console ----

initialCommands in console :=
"""import de.sciss.osc
  |import de.sciss.synth._
  |import ugen._
  |import Predef.{any2stringadd => _}
  |import Ops._
  |def s = Server.default
  |def boot(): Unit = Server.run(_ => ())
  |""".stripMargin

// ---- build info ----

buildInfoSettings

sourceGenerators in Compile <+= buildInfo

buildInfoKeys := Seq(name, organization, version, scalaVersion, description,
  BuildInfoKey.map(homepage) { case (k, opt) => k -> opt.get },
  BuildInfoKey.map(licenses) { case (_, Seq( (lic, _) )) => "license" -> lic }
)

buildInfoPackage := "de.sciss.synth"

// ---- publishing ----

publishMavenStyle := true

publishTo :=
  Some(if (version.value endsWith "-SNAPSHOT")
    "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  else
    "Sonatype Releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
  )

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := { val n = name.value
<scm>
  <url>git@github.com:Sciss/{n}.git</url>
  <connection>scm:git:git@github.com:Sciss/{n}.git</connection>
</scm>
<developers>
   <developer>
      <id>sciss</id>
      <name>Hanns Holger Rutz</name>
      <url>http://www.sciss.de</url>
   </developer>
</developers>
}

// ---- disable scaladoc generation during development phase ----

// publishArtifact in (Compile, packageDoc) := false

// ---- ls.implicit.ly ----

seq(lsSettings :_*)

(LsKeys.tags   in LsKeys.lsync) := Seq("sound-synthesis", "sound", "music", "supercollider")

(LsKeys.ghUser in LsKeys.lsync) := Some("Sciss")

(LsKeys.ghRepo in LsKeys.lsync) := Some(name.value)
