plugins {
    application
    id("idea")
}

application {
    mainClass.set("ru.asmisloff.codewars.App")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
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
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
    implementation("org.apache.logging.log4j:log4j-core:2.22.0")

    compileOnly("org.projectlombok:lombok:1.18.30")

    testImplementation("org.projectlombok:lombok:1.18.30")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
}