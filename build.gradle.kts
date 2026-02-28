import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

//@SuppressWarnings("all")

// file and path
val homePath: String? = System.getenv("HOME")
val modFilePath = "${homePath}/.local/share/Mindustry/mods"
val mindustryPath = "${homePath}/Games/Mindustry/Mindustry.jar"

buildscript {
    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        mavenCentral()
    }

    dependencies {
        val kotlinVersion: String by project

        //classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
        classpath(kotlin("gradle-plugin", kotlinVersion))
    }
}

plugins {
    kotlin("jvm") version "2.3.0"
}

sourceSets {
    main {
        kotlin {
            srcDir("src")
        }
    }
}

version = "1.0"

repositories {
    // Aliyun Maven
    maven { setUrl("https://maven.aliyun.com/repository/public") }
    // Central
    mavenCentral()
    // Mindustry Repo
    maven { setUrl("https://raw.githubusercontent.com/Zelaux/MindustryRepo/master/repository") }
    // Jitpack
    maven { setUrl("https://www.jitpack.io") }
}

dependencies {
    val anukenGroup: String by project
    val mindustryVersion: String by project

    compileOnly("${anukenGroup}.Arc:arc-core:${mindustryVersion}")
    compileOnly("${anukenGroup}.Mindustry:core:${mindustryVersion}")
}

kotlin {
    jvmToolchain(21)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_1_8
}

// TODO add Android support
tasks {
    withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
        }
    }

    withType<Jar>().configureEach {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveFileName = "${rootProject.name}Desktop.jar"

        from(configurations.runtimeClasspath.get().map { file ->
            if (file.isDirectory) file else zipTree(file)
        })

        from("assets/") { include("**") }
    }

    register<DefaultTask>("pack") {
        dependsOn(jar)

        doLast {
            copy {
                from(jar.get().archiveFile)
                into(modFilePath)
            }
        }
    }

    register<DefaultTask>("runMod") {
        dependsOn(jar)

        // temp Desktop name

        doLast {
            copy {
                from(jar.get().archiveFile)
                into(modFilePath)
            }

            project.exec {
//                commandLine("java", "-jar", mindustryPath)
                commandLine("mindustry")
            }
        }
    }
}
