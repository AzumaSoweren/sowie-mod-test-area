import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

//@SuppressWarnings("all")

// file and path
val homePath: String? = System.getenv("HOME")
val sdkPath: String? = System.getenv("ANDROID_HOME")
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
    kotlin("jvm") version "2.2.0"
}

sourceSets {
    main {
        kotlin {
            srcDir("src")
        }
    }
}

version = "1.0"

configurations.create("compileOnlyResolvable") {
    isCanBeResolved = true
    isCanBeConsumed = false
    extendsFrom(configurations.compileOnly.get())
}

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

    register<Jar>("jarR8") {
        dependsOn("runR8")
//        dependsOn(jar)

        archiveFileName = "${rootProject.name}.jar"

        from(
            zipTree("build/libs/${rootProject.name}Android.jar"),
            zipTree("build/libs/${rootProject.name}Desktop.jar"),
        )

        doLast {
            delete("build/libs/${rootProject.name}Android.jar")
        }

//        from("assets/") { include("**") }
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
        dependsOn("pack")

        // temp Desktop name

        doLast {
            project.exec {
//                commandLine("java", "-jar", mindustryPath)
                commandLine("mindustry")
            }
        }
    }

    register<JavaExec>("runR8") {
        dependsOn(jar)

        val buildToolsVersion: String by project
        val platformsVersion: String by project
        mainClass.set("com.android.tools.r8.R8")
        classpath = files("$sdkPath/build-tools/$buildToolsVersion/lib/d8.jar")

        val dexOutput = file("build/libs/${rootProject.name}Android.jar")
        val r8Rules = file("r8-rules.pro")
        val archiveFile = jar.get().archiveFile.get().asFile
        val compileOnly = configurations["compileOnlyResolvable"].files.toList()

        val r8Args = mutableListOf(
            "--release",
            "--output", dexOutput.absolutePath,
            "--pg-conf", r8Rules.absolutePath,
        )

        compileOnly.forEach { libFile ->
            println("Adding compileOnly lib to R8: ${libFile.absolutePath}") // for debug
            r8Args.add("--lib")
            r8Args.add(libFile.absolutePath)
        }

        r8Args.add("--lib")
        r8Args.add("$sdkPath/platforms/$platformsVersion/android.jar")
        r8Args.add(archiveFile.absolutePath)

        args = r8Args

        inputs.file(r8Rules)
        inputs.files(compileOnly)
        inputs.file(archiveFile)
        outputs.file(dexOutput)
    }
}
