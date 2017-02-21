import sbt.Resolver

name := "KSInventory"
//
version := "0.1"

scalaVersion := "2.12.1"

resolvers += "Spigot Repo" at "https://hub.spigotmc.org/nexus/content/repositories/snapshots/"
resolvers += "BungeeCord Repo" at "https://oss.sonatype.org/content/repositories/snapshots/"
resolvers += "WorldEdit Repo" at "http://maven.sk89q.com/repo/"
resolvers += Resolver sonatypeRepo "public"

libraryDependencies += "org.spigotmc" % "spigot-api" % "1.11.2-R0.1-SNAPSHOT" % "provided"
libraryDependencies += "org.bukkit" % "bukkit" % "1.11.2-R0.1-SNAPSHOT" % "provided"
libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-core" % "3.1.3"
libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-mapping" % "3.1.3"