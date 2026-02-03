import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.gradleup.shadow") version "9.3.1"
    id("java")
}

group = "com.beanbeanjuice"
version = "4.3.0" // x-release-please-version

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.beanbeanjuice.cafebot.CafeBot"
    }
}

allprojects {
    group = "com.beanbeanjuice"

    apply(plugin = "java")
    apply(plugin = "com.gradleup.shadow")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    repositories {
        mavenCentral()

        maven {
            name = "jitpack"
            url = uri("https://jitpack.io")
        }
    }

    dependencies {
        // Lombok
        compileOnly("org.projectlombok:lombok:1.18.42")
        annotationProcessor("org.projectlombok:lombok:1.18.42")

        implementation("org.apache.httpcomponents.client5:httpclient5:5.6")  // HTTP Requests

        implementation("tools.jackson.core:jackson-core:3.0.4") // https://mvnrepository.com/artifact/tools.jackson.core/jackson-core
        implementation("tools.jackson.core:jackson-databind:3.0.4") // https://mvnrepository.com/artifact/tools.jackson.core/jackson-databind

        implementation("io.github.cdimascio:java-dotenv:5.2.2") // https://mvnrepository.com/artifact/io.github.cdimascio/java-dotenv

        testImplementation("org.junit.jupiter:junit-jupiter:6.0.2") // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

        implementation("org.jetbrains", "annotations", "24.1.0")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<ShadowJar> {
        archiveClassifier.set("")
        archiveBaseName.set(project.name)
        destinationDirectory.set(File(rootProject.projectDir, "libs"))

        relocate("tools.jackson.core", "com.beanbeanjuice.libs.tools.jackson.core")
        relocate("org.jetbrains", "com.beanbeanjuice.libs.org.jetbrains")
        relocate("org.apache.httpcomponents.client5:httpclient5", "com.beanbeanjuice.libs.org.apache.httpcomponents.client5:httpclient5")

        doLast {
            archiveVersion.set(project.version as String)
            println("Compiling: " + project.name + "-" + project.version + ".jar")
        }

        mergeServiceFiles()
    }

    tasks.test {
        useJUnitPlatform()

        testLogging {
            // Only show the standard events
            events("started", "passed", "skipped", "failed")

            // Compact output
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.SHORT
            showExceptions = true
            showCauses = true
            showStackTraces = false
            showStandardStreams = false
        }
    }

}

tasks.clean {
    doLast {
        file("libs").deleteRecursively()
    }
}

dependencies {
    implementation(project(":modules:cafeBot-api-wrapper"))
    implementation(project(":modules:meme-api-wrapper"))

    implementation("net.dv8tion:JDA:6.3.0") { exclude(module = "opus-java") }

    implementation("org.apache.logging.log4j:log4j-api:2.25.3")  // Logging - https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core
    implementation("org.apache.logging.log4j:log4j-core:2.25.3")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.25.3")  // JDA logging.

    implementation("org.mnode.ical4j:ical4j:4.2.3")  // Calendar Stuff - https://mvnrepository.com/artifact/org.mnode.ical4j/ical4j

    implementation("com.github.twitch4j:twitch4j:1.25.0")  // Twitch - https://github.com/twitch4j/twitch4j

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
}

tasks.withType<ShadowJar> {
    minimize {
        // Do NOT minimize Twitch4J Dependencies
        exclude(dependency("org.apache.logging.log4j:.*:.*"))
        exclude(dependency("io.github.xanthic.cache:.*:.*"))
        exclude(dependency("com.github.twitch4j:.*:.*"))
        exclude(dependency("com.squareup.okhttp3:.*:.*"))
        exclude(dependency("org.mnode.ical4j:.*:.*"))
    }

    relocate("org.mnode.ical4j", "com.beanbeanjuice.libs.org.mnode.ical4j")

    mergeServiceFiles()
}

configure<ProcessResources>("processResources") {
    filesMatching("cafebot.properties") {
        expand(project.properties)
    }
}

inline fun <reified C> Project.configure(name: String, configuration: C.() -> Unit) {
    (this.tasks.getByName(name) as C).configuration()
}
