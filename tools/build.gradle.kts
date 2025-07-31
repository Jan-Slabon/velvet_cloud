
plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    alias(libs.plugins.kotlin.jvm)

    // Apply the application plugin to add support for building a CLI application in Java.
    application
}
repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}
dependencies {
    // This dependency is used by the application.
    implementation(libs.guava)
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use Kotlin Test test framework
            useKotlinTest("2.1.0")
        }
    }
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

sourceSets{
    main{
        kotlin.srcDirs("gen")
        resources.srcDirs("gen/resources")
    }

}
dependencies{
    implementation("com.squareup:kotlinpoet:2.2.0")
}
application {
    // Define the main class for the application.
    mainClass = "tools.todoKt"
}
