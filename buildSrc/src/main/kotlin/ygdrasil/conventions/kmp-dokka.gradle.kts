package ygdrasil.conventions

import java.net.URI

plugins {
    id("org.jetbrains.dokka")
}

dependencies {
    dokkaPlugin("org.jetbrains.dokka:gfm-plugin:2.3.0")
}

dokka {
    moduleName.set("shared")
    dokkaSourceSets.configureEach {
        sourceLink {
            localDirectory.set(project.file("src/commonMain/kotlin"))
            remoteUrl.set(URI("https://github.com/ygdrasil-io/project-template/blob/master/shared/src/commonMain/kotlin"))
            remoteLineSuffix.set("#L")
        }
    }
}
