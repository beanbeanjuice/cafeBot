import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("io.github.goooler.shadow") version "8.1.7"
    id("java")
}

group = "com.beanbeanjuice"
version = "4.0.0"

allprojects {
    group = "com.beanbeanjuice"

    apply(plugin = "java")
    apply(plugin = "io.github.goooler.shadow")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    repositories {
        mavenCentral()

        maven {
            name = "sonatype"
            url = uri("https://oss.sonatype.org/content/groups/public/")
        }

        maven {
            name = "OSSRH-snapshots"
            url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        }

        maven {
            name = "OSSRH-releases"
            url = uri("https://s01.oss.sonatype.org/content/repositories/releases/")
        }

        maven {
            name = "jitpack"
            url = uri("https://jitpack.io")
        }
    }

    dependencies {
        // Lombok
        compileOnly("org.projectlombok", "lombok", "1.18.32")
        annotationProcessor("org.projectlombok", "lombok", "1.18.32")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<ShadowJar> {
        minimize()
        archiveClassifier.set("")
        archiveBaseName.set(project.name)
        destinationDirectory.set(File(rootProject.projectDir, "libs"))

        doLast {
            archiveVersion.set(project.version as String)
            println("Compiling: " + project.name + "-" + project.version + ".jar")
        }
    }

    tasks.test {
        useJUnitPlatform()
    }
}

tasks.clean {
    doLast {
        file("libs").deleteRecursively()
    }
}

dependencies {
    implementation(project(":wrappers:cafe-api-wrapper"))

    implementation("net.dv8tion", "JDA", "5.0.0-beta.24") { exclude(module = "opus-java") }

    implementation("org.apache.logging.log4j", "log4j-api", "2.23.1")
    implementation("org.apache.logging.log4j", "log4j-core", "2.23.1")
    implementation("org.slf4j", "slf4j-reload4j", "2.0.13")
    implementation("org.apache.logging.log4j", "log4j-slf4j18-impl", "2.18.0")  // JDA logs.

    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")

    implementation("com.github.twitch4j", "twitch4j", "1.15.0")

    compileOnly("org.projectlombok", "lombok", "1.18.32")
    annotationProcessor("org.projectlombok", "lombok", "1.18.32")

    testImplementation("junit", "junit", "4.13.2")
    testImplementation("org.junit.jupiter", "junit-jupiter", "5.8.1")

    implementation("com.github.plexpt", "chatgpt", "4.4.0")
}

tasks.withType<ShadowJar> {
    relocate("net.dv8tion", "com.beanbeanjuice.cafebot.libs.net.dv8tion")
    relocate("org.slf4j", "com.beanbeanjuice.cafebot.libs.org.slf4j")
    relocate("org.apache.logging.log4j", "com.beanbeanjuice.cafebot.libs.org.apache.logging.log4j")
    relocate("com.github.twitch4j", "com.beanbeanjuice.cafebot.libs.com.github.twitch4j")
    relocate("com.fasterxml.jackson.core", "com.beanbeanjuice.cafebot.libs.com.fasterxml.jackson.core")
}

configure<ProcessResources>("processResources") {
    filesMatching("cafebot.properties") {
        expand(project.properties)
    }
}

inline fun <reified C> Project.configure(name: String, configuration: C.() -> Unit) {
    (this.tasks.getByName(name) as C).configuration()
}
