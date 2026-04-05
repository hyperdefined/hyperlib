plugins {
    `java-library`
    id("maven-publish")
    id("com.gradleup.shadow") version "9.4.1"
}

group = "lol.hyper"
version = "1.0.14"
description = "hyperlib"

repositories {
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    mavenCentral()
}

dependencies {
    implementation("org.bstats:bstats-bukkit:3.2.1")
    implementation("org.json:json:20251224")
    compileOnly("io.papermc.paper:paper-api:26.1.1.build.+")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<GenerateModuleMetadata>().configureEach {
    enabled = false
}

tasks.shadowJar {
    relocate("org.bstats", "lol.hyper.hyperlib.shaded.bstats")
    relocate("org.json", "lol.hyper.hyperlib.shaded.json")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["shadow"])
        }
    }
}