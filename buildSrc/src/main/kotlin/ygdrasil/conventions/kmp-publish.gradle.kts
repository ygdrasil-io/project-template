package ygdrasil.conventions

plugins {
    id("maven-publish")
    id("signing")
}

group = "io.ygdrasil.shared"
version = project.findProperty("releaseVersion") as? String ?: "1.0.0-SNAPSHOT"

val isSnapshot = version.toString().endsWith("SNAPSHOT")

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(provider { tasks.findByName("dokkaGfm")?.outputs?.files ?: files() })
}

publishing {
    publications.withType<MavenPublication>().configureEach {
        artifact(javadocJar)

        pom {
            name.set("KMP Starter Pack Shared Library")
            description.set("Shared library logic for KMP Starter Pack")
            url.set("https://github.com/ygdrasil-io/project-template")

            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }

            developers {
                developer {
                    id.set("ygdrasil-io")
                    name.set("Ygdrasil team")
                    email.set("contact@ygdrasil.com")
                }
            }

            scm {
                connection.set("scm:git:git://github.com/ygdrasil-io/project-template.git")
                developerConnection.set("scm:git:ssh://github.com/ygdrasil-io/project-template.git")
                url.set("https://github.com/ygdrasil-io/project-template")
            }
        }
    }

    if (isSnapshot) {
        repositories {
            maven {
                name = "snapshots"
                url = uri("https://central.sonatype.com/repository/maven-snapshots/")
                credentials {
                    username = project.findProperty("mavenCentralUsername") as? String ?: System.getenv("MAVEN_CENTRAL_USERNAME") ?: ""
                    password = project.findProperty("mavenCentralPassword") as? String ?: System.getenv("MAVEN_CENTRAL_PASSWORD") ?: ""
                }
            }
        }
    }
}

if (!isSnapshot) {
    signing {
        val signingKey = project.findProperty("signingKey") as? String ?: System.getenv("SIGNING_KEY")
        val signingPassword = project.findProperty("signingPassword") as? String ?: System.getenv("SIGNING_PASSWORD")
        if (!signingKey.isNullOrEmpty() && !signingPassword.isNullOrEmpty()) {
            useInMemoryPgpKeys(signingKey, signingPassword)
            sign(publishing.publications)
        }
    }
}
