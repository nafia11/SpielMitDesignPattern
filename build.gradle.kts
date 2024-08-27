plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.0.9"
}

repositories {
    mavenCentral()
}

dependencies {
    // JavaFX dependencies
    implementation("org.openjfx:javafx-controls:19.0.2.1")
    implementation("org.openjfx:javafx-media:21.0.2")

    // JUnit 5 dependencies
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // Log4j2 dependencies
    implementation("org.apache.logging.log4j:log4j-api:2.17.1")
    implementation("org.apache.logging.log4j:log4j-core:2.17.1")

    implementation ("org.mockito:mockito-core:5.0.0")
    implementation ("org.junit.jupiter:junit-jupiter:5.8.2")
}

javafx {
    version = "19.0.2.1"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.base", "javafx.graphics", "javafx.media")
}

tasks.test {
    useJUnitPlatform()
}