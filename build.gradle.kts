/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * To learn more about Gradle by exploring our Samples at https://docs.gradle.org/8.5/samples
 */

plugins {
    application
    id("checkstyle")
    id("jacoco")
    id("io.freefair.lombok") version "8.6"
    id("io.sentry.jvm.gradle") version "4.3.1"
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "io.hexlet"
version = "0.0.1-SNAPSHOT"

application { mainClass.set("hexlet.code.AppApplication") }

repositories {
    mavenCentral()
}

jacoco {
    toolVersion = "0.8.12"
}

dependencies {
    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    // database
    runtimeOnly("com.h2database:h2:2.2.224")
    // jpa & validation
    implementation("org.springframework.boot:spring-boot-starter-validation:3.2.4")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.5")
    // mapping
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    // spring boot tools
    implementation("org.springframework.boot:spring-boot-starter:3.2.4")
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.4")
    implementation("org.springframework.boot:spring-boot-devtools:3.2.5")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.4")
    // spring security
    implementation("org.springframework.boot:spring-boot-starter-security:3.2.4")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:3.2.5")
    testImplementation("org.springframework.security:spring-security-test:6.2.4")
    // for tests
    implementation("org.instancio:instancio-junit:4.6.0")
    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.7")
    implementation("net.datafaker:datafaker:2.2.2")
    // sentry
    implementation("io.sentry:sentry-spring-boot-starter-jakarta:7.9.0")

}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JacocoReport> {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

sentry {
    includeSourceContext = true
    org = "ittz"
    projectName = "java-spring-boot"
    authToken = System.getenv("SENTRY_AUTH_TOKEN")
}

tasks.sentryBundleSourcesJava {
    enabled = System.getenv("SENTRY_AUTH_TOKEN") != null
}
