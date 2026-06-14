package ygdrasil.conventions

plugins {
    id("maven-publish")
    id("signing")
}

group = "io.ygdrasil.shared"
version = project.findProperty("releaseVersion") as? String ?: "1.0.0-SNAPSHOT"

publishing {
    publications.withType<MavenPublication>().configureEach {
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

    repositories {
        maven {
            name = "mavenCentral"
            val releasesRepoUrl = uri("https://central.sonatype.com/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://central.sonatype.com/repository/maven-snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            credentials {
                username = project.findProperty("mavenCentralUsername") as? String ?: System.getenv("MAVEN_CENTRAL_USERNAME") ?: ""
                password = project.findProperty("mavenCentralPassword") as? String ?: System.getenv("MAVEN_CENTRAL_PASSWORD") ?: ""
            }
        }
    }
}

signing {
    val signingKey = project.findProperty("signingKey") as? String ?: System.getenv("SIGNING_KEY")
    val signingPassword = project.findProperty("signingPassword") as? String ?: System.getenv("SIGNING_PASSWORD")
    if (!signingKey.isNullOrEmpty() && !signingPassword.isNullOrEmpty()) {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications)
    }
}
