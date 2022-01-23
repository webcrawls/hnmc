rootProject.name = "hnmc"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.stellardrift.ca/repository/snapshots/")
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

plugins {
    id("ca.stellardrift.polyglot-version-catalogs") version "5.0.0-SNAPSHOT"
}