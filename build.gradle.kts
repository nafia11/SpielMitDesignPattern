plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.0.9"
}

repositories {
    mavenCentral()
}

dependencies {
    // JavaFX dependencies
//    implementation("org.openjfx:javafx-controls:17.0.2")
//    implementation("org.openjfx:javafx-fxml:17.0.2")
//    implementation("org.openjfx:javafx-graphics:17.0.2")
//    implementation("org.openjfx:javafx-base:17.0.2")
    implementation("org.openjfx:javafx-controls:19.0.2.1")
    implementation("org.openjfx:javafx-media:21.0.2")
    // JUnit 5 dependencies
    implementation(platform("org.junit:junit-bom:5.9.1"))
    implementation("org.junit.jupiter:junit-jupiter")
    // Log4j2 dependencies
    implementation("org.apache.logging.log4j:log4j-api:2.17.1")
    implementation("org.apache.logging.log4j:log4j-core:2.17.1")
}

//javafx{
//    version = "17.0.2"
//    modules = listOf("javafx.controls", "javafx.fxml")
//}
javafx {
    version = "19.0.2.1"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.base", "javafx.graphics", "javafx.media")
}


