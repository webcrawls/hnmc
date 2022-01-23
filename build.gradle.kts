plugins {
    `java-library`
    `maven-publish`
    id("io.papermc.paperweight.userdev")
    id("com.github.johnrengelman.shadow")
    id("org.checkerframework")
}
group = "sh.kaden.hnmc"
version = "1.0.0-SNAPSHOT"
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
repositories {
    // Main repos
    mavenCentral()
    mavenLocal()
    // Minecraft
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://repo.citizensnpcs.co/")
    maven("https://jitpack.io")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.essentialsx.net/snapshots/")
    maven("https://repo.dmulloy2.net/repository/public")
    maven("https://repo.incendo.org/content/repositories/snapshots/")
}
dependencies {
    // Minecraft
    paperDevBundle("1.18.1-R0.1-SNAPSHOT") // paper dev bundle
    implementation(libs.adventure.minimessage)
    implementation(libs.cloud.core)
    implementation(libs.cloud.paper)
    implementation(libs.interfaces.paper)
    implementation(libs.corn.minecraft.paper) {
        exclude(group="io.papermc.paper", module="paper-api")
    }
    // Misc
    implementation(libs.configurate.hocon)
    implementation(libs.checker.qual)
}
tasks {
    // Re-obfuscate jar with correct mappings using Paperweight.
    assemble {
        dependsOn(reobfJar)
    }
    // Set UTF-8 & Java 17
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    // Set UTF-8
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    // Set UTF-8
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
    // Shade & relocate dependencies
    shadowJar {
        fun reloc(pkg: MinimalExternalModuleDependency) = relocate(pkg.module.group, "sh.kaden.hnmc.dependencies.${pkg.module.group.split(".").last()}")
        reloc(libs.cloud.core.get())
        reloc(libs.interfaces.paper.get())
        reloc(libs.corn.minecraft.paper.get())
        reloc(libs.configurate.hocon.get())
    }
}
publishing {
    // create maven publication using java artifacts
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
