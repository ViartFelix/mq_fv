var kotlinVersion = "2.0.21"

plugins {
    kotlin("jvm") version "2.0.21"
    id("idea")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "fr.fv"
//version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://repo.dmulloy2.net/repository/public/")
}

val ktormVersion = "4.1.1"
val postgresqlAdapterVersion = "42.7.4"
val protocolLibVersion = "5.3.0"
val boostedYamlVersion = "1.3.6"
val mcVersion = "1.20.6"

dependencies {
    compileOnly("io.papermc.paper:paper-api:${mcVersion}-R0.1-SNAPSHOT")

    //kotlin's librairies
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation ("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")

    implementation("dev.dejvokep:boosted-yaml:${boostedYamlVersion}")

    //ktorm
    implementation("org.ktorm:ktorm-core:${ktormVersion}")
    implementation("org.ktorm:ktorm-support-postgresql:${ktormVersion}")

    //postgresql
    implementation("org.postgresql:postgresql:${postgresqlAdapterVersion}")

    //protocolLib
    //implementation("com.comphenix.protocol:ProtocolLib:${protocolLibVersion}")
    compileOnly("com.comphenix.protocol:ProtocolLib:${protocolLibVersion}")
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks {
    build {
        dependsOn("shadowJar")
    }

    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }

    shadowJar {
        archiveClassifier.set("") // Ensures this is the primary JAR

        // Include Kotlin runtime
        dependencies {
            //kotlin's libs
            include(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
            include(dependency("org.jetbrains.kotlin:kotlin-reflect"))

            //ktorm
            include(dependency("org.ktorm:ktorm-core"))
            include(dependency("org.ktorm:ktorm-support-postgresql"))

            //postgresql
            include(dependency("org.postgresql:postgresql"))
        }
    }
}