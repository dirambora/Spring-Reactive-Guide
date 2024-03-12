plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("io.projectreactor:reactor-core:3.6.0")
    testImplementation("ch.qos.logback:logback-classic:1.5.3")
    implementation("org.assertj:assertj-core:3.19.0") // Use the latest version available





}

tasks.test {
    useJUnitPlatform()
}