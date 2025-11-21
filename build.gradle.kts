plugins {
    id("java")
    id("application")
}


///// Application /////

group = "ru.anafro.hyperstream.leonardo"
version = "1.0-SNAPSHOT"

application {
    mainClass = "ru.anafro.hyperstream.leonardo.Main"
}

java.sourceCompatibility = JavaVersion.VERSION_24
java.targetCompatibility = JavaVersion.VERSION_24


///// Dependencies /////

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-api:1.7.25")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}


///// Tasks /////

tasks.jar {
    archiveFileName = "Hyperstream-Leonardo.jar"

    manifest {
        attributes["Main-Class"] = application.mainClass
    }
}

tasks.test {
    useJUnitPlatform()
}