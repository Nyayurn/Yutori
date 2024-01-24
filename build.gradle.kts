import java.net.URI

plugins {
    kotlin("jvm") version "1.9.21"
    `maven-publish`
    java
}

group = "com.github.Nyayurn"

val jacksonVersion = "2.16.0"
val jsoupVersion = "1.17.1"
val ktorVersion = "2.3.7"
val slf4jVersion = "1.7.2"
val junitVersion = "5.10.1"

repositories {
    maven { url = URI("https://repo.huaweicloud.com/repository/maven/") }
    mavenCentral()
}

dependencies {
    api("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    api("org.jsoup:jsoup:$jsoupVersion")
    api("io.ktor:ktor-server-core:$ktorVersion")
    api("io.ktor:ktor-server-cio:$ktorVersion")
    api("io.ktor:ktor-client-core:$ktorVersion")
    api("io.ktor:ktor-client-cio:$ktorVersion")
    api("io.ktor:ktor-client-websockets:$ktorVersion")
    implementation("org.apache.directory.studio:org.slf4j.api:$slf4jVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
}

sourceSets {
    main {
        java.srcDir("main")
    }
    test {
        java.srcDir("test")
    }
}

java {
    withSourcesJar()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}

publishing.publications.create<MavenPublication>("maven") {
    from(components["java"])
}

kotlin {
    jvmToolchain(8)
}