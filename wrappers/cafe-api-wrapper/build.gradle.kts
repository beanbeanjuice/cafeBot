import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

version = "2.0.0"

dependencies {
    implementation(project(":wrappers:kawaii-api-wrapper"))

    implementation("com.fasterxml.jackson.core", "jackson-core", "2.17.1")
    implementation("com.fasterxml.jackson.core", "jackson-databind", "2.17.1")
    implementation("com.fasterxml.jackson.core", "jackson-annotations", "2.17.1")

    implementation("org.apache.httpcomponents", "httpclient", "4.5.14")

    testImplementation("junit", "junit", "4.13.2")
    testImplementation("org.junit.jupiter", "junit-jupiter", "5.8.1")

    implementation("org.jetbrains", "annotations", "24.1.0")

}

tasks.withType<ShadowJar> {
    relocate("com.fasterxml.jackson.core", "com.beanbeanjuice.cafeapi.wrapper.libs.com.fasterxml.jackson.core")
    relocate("org.apache.httpcomponents", "com.beanbeanjuice.cafeapi.wrapper.libs.org.apache.httpcomponents")
    relocate("org.jetbrains", "com.beanbeanjuice.cafeapi.wrapper.libs.org.jetbrains")
}
