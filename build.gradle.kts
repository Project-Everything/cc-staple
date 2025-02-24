plugins {
    id("java")
    id("com.gradleup.shadow") version ("9.0.0-beta2")
    id("io.freefair.lombok") version ("8.12.1")
}

group = "net.cc"
version = "1.0"

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        name = "glaremasters repo"
        url = uri("https://repo.glaremasters.me/repository/towny/")
    }
}

dependencies {
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("org.spongepowered:configurate-hocon:4.1.2")
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    shadowJar {
        archiveClassifier.set("")
    }
    build {
        dependsOn(shadowJar)
    }
}