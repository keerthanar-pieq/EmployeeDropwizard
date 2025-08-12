plugins {
    kotlin("jvm") version "2.2.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
application{
    mainClass.set("ApplicationKt")
    applicationDefaultJvmArgs = listOf()
}
tasks.run<JavaExec> {
    args = listOf("server", "src/main/resources/config.yml")
}
dependencies {
    implementation("io.dropwizard:dropwizard-core:2.1.0")
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}