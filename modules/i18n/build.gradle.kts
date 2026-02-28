import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

version = "0.0.0"

dependencies {
    implementation("org.yaml:snakeyaml:2.5") // https://mvnrepository.com/artifact/org.yaml/snakeyaml
}

tasks.withType<ShadowJar> { }
