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

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}



///// Dependencies /////

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.rabbitmq:amqp-client:5.27.1")
    implementation("org.slf4j:slf4j-api:1.7.25")
    implementation("net.sourceforge.argparse4j:argparse4j:0.9.0")
    implementation("io.javalin:javalin:6.7.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}


///// Tasks /////

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Main-Class"] = application.mainClass
    }

    from(
        configurations.runtimeClasspath.get().map { file: File ->
            if (file.isDirectory) file else zipTree(file)
        }
    )
}


tasks.test {
    useJUnitPlatform()
}
