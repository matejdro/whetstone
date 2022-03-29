import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        gradlePluginPortal()
        google()
    }

    dependencies {
        classpath(libs.androidGradle)
        classpath(libs.kotlinGradle)
        classpath(libs.anvilGradle)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

subprojects {
    afterEvaluate {
        tasks.withType<KotlinCompile> {
            configureTask()
        }
        extensions.findByType<BaseExtension>()?.apply {
            configureExtension()
        }
    }
}

fun KotlinCompile.configureTask() {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()

        val compilerArgs = mutableListOf(
            "-Xassertions=jvm",
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xuse-experimental=com.squareup.anvil.annotations.ExperimentalAnvilApi",
        )
        if (project.name != "sample") compilerArgs += "-Xexplicit-api=strict"
        freeCompilerArgs = freeCompilerArgs + compilerArgs
    }
}

fun BaseExtension.configureExtension() {
    compileSdkVersion(31)

    defaultConfig {
        minSdk = 21
        targetSdk = 31
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
