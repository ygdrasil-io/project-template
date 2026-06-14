plugins {
    alias(libs.plugins.nexus.publish)
}

nexusPublishing {
    repositories {
        create("mavenCentral") {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(project.findProperty("mavenCentralUsername") as? String ?: System.getenv("MAVEN_CENTRAL_USERNAME") ?: "")
            password.set(project.findProperty("mavenCentralPassword") as? String ?: System.getenv("MAVEN_CENTRAL_PASSWORD") ?: "")
        }
    }
}
