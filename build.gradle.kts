plugins {
    alias(libs.plugins.nexus.publish)
}

nexusPublishing {
    repositories {
        create("mavenCentral") {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
            username.set(project.findProperty("mavenCentralUsername") as? String ?: System.getenv("MAVEN_CENTRAL_USERNAME") ?: "")
            password.set(project.findProperty("mavenCentralPassword") as? String ?: System.getenv("MAVEN_CENTRAL_PASSWORD") ?: "")
        }
    }
}
