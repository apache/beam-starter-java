mainClass := Some("com.example.App")

val beamVersion = "2.33.0"
libraryDependencies ++= Seq(
  // App dependencies.
  "org.apache.beam" % "beam-sdks-java-core" % beamVersion,
  "org.apache.beam" % "beam-runners-direct-java" % beamVersion,
  "org.slf4j" % "slf4j-jdk14" % "1.7.32",

  // Test dependencies.
  "net.aichler" % "jupiter-interface" % JupiterKeys.jupiterVersion.value % Test,
  "org.junit.jupiter" % "junit-jupiter" % "5.8.1" % Test
)

// Package self-contained jar file.
assembly / assemblyOutputPath := file("build/pipeline.jar")
assembly / assemblyMergeStrategy := {
  case PathList("META-INF")      => MergeStrategy.discard
  case x if x.endsWith(".class") => MergeStrategy.first
  case x                         => (assembly / assemblyMergeStrategy).value(x)
}
