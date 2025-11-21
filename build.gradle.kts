plugins {
    id("java")
    id("application")
}

group = "ru.anafro.hyperstream.leonardo"
version = "1.0-SNAPSHOT"

application {
    mainClass = "ru.anafro.hyperstream.leonardo.Main"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-api:1.7.25")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.jar {
    archiveFileName = "Hyperstream-Leonardo.jar"

    manifest {
        attributes["Main-Class"] = application.mainClass
    }
}

tasks.test {
    useJUnitPlatform()
}