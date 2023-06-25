plugins {
    application
    id("idea")
}

application {
    mainClass.set("ru.asmisloff.codewars.App")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }

}

repositories {
    mavenCentral()
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
}