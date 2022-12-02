

plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.5.2"
}

group = "com.summer.lijiahao"
version = "1.0.7"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    version.set("2022.2")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf("com.intellij.java"))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    patchPluginXml {
        sinceBuild.set("222")
        untilBuild.set("223.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    runIde {
        jvmArgs("-Xmx8192m","-XX:ReservedCodeCacheSize=512m","-Xms128m")
    }
}

dependencies {
    implementation("com.jolbox:bonecp:0.8.0.RELEASE") {
        exclude("org.slf4j")
    }
    implementation("com.oracle.database.jdbc:ojdbc8:21.6.0.0.1")
    implementation("com.alibaba:fastjson:2.0.14")
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
}

