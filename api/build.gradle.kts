plugins {
    java
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    id("io.github.goooler.shadow") version "8.1.7"
}

group = "com.beanbeanjuice"
version = "0.0.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring
    implementation("org.springframework.boot", "spring-boot-starter-web", "3.3.2")
    implementation("org.springframework.boot", "spring-boot-starter-security", "3.3.2")
    implementation("org.springframework.data", "spring-data-jpa", "3.3.2")
    testImplementation("org.springframework.boot", "spring-boot-starter-test", "3.3.2")
    testImplementation("org.springframework.security", "spring-security-test", "6.3.1")

    // Hibernate
    implementation("org.hibernate.orm", "hibernate-core", "6.5.2.Final")

    // JWT Token
    implementation("com.auth0", "java-jwt", "4.4.0")

    // MySQL Driver
    implementation("com.mysql", "mysql-connector-j", "9.0.0")

    // Junit for ???
    testRuntimeOnly("org.junit.platform", "junit-platform-launcher", "1.11.0-M2")

    // Lombok
    compileOnly("org.projectlombok", "lombok", "1.18.34")
    annotationProcessor("org.projectlombok", "lombok", "1.18.34")
}
