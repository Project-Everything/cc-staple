plugins {
    id("java")
    id("maven-publish")
    id("com.gradleup.shadow") version ("9.0.0-beta2")
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
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
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

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Project-Everything/cc-staple")
            credentials {
                username = "CreativeCentral-mc"
                password = "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"
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = group.toString()
            artifactId = "staple"
            version = version.toString()
        }
    }
}