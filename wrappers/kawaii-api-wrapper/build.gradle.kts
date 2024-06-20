import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

version = "2.0.0"

dependencies {
    implementation("com.fasterxml.jackson.core", "jackson-core", "2.15.2")
    implementation("com.fasterxml.jackson.core", "jackson-databind", "2.15.2")
    implementation("com.fasterxml.jackson.core", "jackson-annotations", "2.15.2")

    implementation("org.apache.httpcomponents", "httpclient", "4.5.14")

    testImplementation("junit", "junit", "4.13.2")
    testImplementation("org.junit.jupiter", "junit-jupiter", "5.8.1")
}

tasks.withType<ShadowJar> {
    relocate("com.fasterxml.jackson.core", "com.beanbeanjuice.kawaiiapi.wrapper.libs.com.fasterxml.jackson.core")
    relocate("org.apache.httpcomponents", "com.beanbeanjuice.kawaiiapi.wrapper.libs.org.apache.httpcomponents")
}
