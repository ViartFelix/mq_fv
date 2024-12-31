plugins {
    kotlin("jvm") version "2.0.21"
    id("idea")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "fr.fv"
version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
}

var ktormVersion = "4.0.0"

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.3-R0.1-SNAPSHOT")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("dev.dejvokep:boosted-yaml:1.3.6")

    compileOnly("org.ktorm:ktorm-core:4.1.1")

    //database-related
    //implementation("org.ktorm:ktorm-core:4.1.1")
    //implementation("io.ktor:ktor-server-core")
    //implementation("io.ktor:ktor-server-netty")
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
            include(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
        }
    }
}