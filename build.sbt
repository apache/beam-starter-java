// Copyright 2022 Google LLC
//
// Licensed under the Apache License, Version 2.0 <LICENSE-APACHE or
// https://www.apache.org/licenses/LICENSE-2.0> or the MIT license
// <LICENSE-MIT or https://opensource.org/licenses/MIT>, at your
// option. This file may not be copied, modified, or distributed
// except according to those terms.

// Set the main class for the executable JAR
mainClass := Some("com.example.App")

// Define the Apache Beam version
val beamVersion = "2.64.0" // As of 2025-04-01, consider checking for newer stable versions if appropriate

// Define project library dependencies
libraryDependencies ++= Seq(
  // App dependencies.
  "org.apache.beam" % "beam-sdks-java-core" % beamVersion,
  "org.apache.beam" % "beam-runners-direct-java" % beamVersion,
  // Note: The 'exclude' below prevents pulling in transitive deps, but doesn't remove files from the JAR itself.
  "org.apache.beam" % "beam-vendor-grpc-1_69_0" % "0.1" exclude("org.mozilla", "public-suffix-list.txt"),
  "org.slf4j" % "slf4j-jdk14" % "1.7.32", // Consider updating SLF4J to 2.x series if compatible
  "org.apache.httpcomponents" % "httpclient" % "4.5.13" exclude("org.apache.beam", "beam-vendor-grpc-1_69_0"), // Excludes the Beam gRPC dep

  // Test dependencies.
  "junit" % "junit" % "4.13.2" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test,
  "org.hamcrest" % "hamcrest" % "2.2" % Test
)

// Configure sbt-assembly to package a self-contained jar file.
assembly / assemblyOutputPath := file("build/pipeline.jar")

assembly / assemblyMergeStrategy := {
  // Explicitly handle the conflicting public suffix list file by taking the first one found
  case PathList("mozilla", "public-suffix-list.txt") => MergeStrategy.first
  // Discard META-INF files (common practice)
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  // For duplicate class files, take the first one (use with caution)
  case x if x.endsWith(".class") => MergeStrategy.first
  // For all other files, use the default sbt-assembly strategy
  case x => (assembly / assemblyMergeStrategy).value(x)
}
