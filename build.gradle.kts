plugins {
  `java-library`
}

repositories {
  jcenter()
}

dependencies {
    components.all<NettyBomAlignmentRule>()
    testImplementation(platform("org.junit:junit-bom:5.13.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.platform:junit-platform-engine")
    testImplementation("org.junit.platform:junit-platform-launcher")

    testImplementation("com.codeborne:selenide:7.9.3")
    testRuntimeOnly("org.slf4j:slf4j-simple:2.0.17")
    constraints {
        implementation("com.google.guava:guava:33.4.8-jre") {
            because("latest compatible version with all dependencies")
        }
    }
    testImplementation("com.microsoft.playwright:playwright:1.53.0")
    testImplementation("com.microsoft.playwright:driver-bundle:1.53.0")
}

java {
  toolchain {
      languageVersion.set(JavaLanguageVersion.of(17))
  }
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
  testLogging.showExceptions = true
  systemProperties["selenide.headless"] = System.getProperty("selenide.headless")
}

open class NettyBomAlignmentRule: ComponentMetadataRule {
    override fun execute(ctx: ComponentMetadataContext) {
        ctx.details.run {
            if (id.group.startsWith("io.netty")) {
                // declare that Netty modules belong to the platform defined by the Netty BOM
                belongsTo("io.netty:netty-bom:${id.version}", false)
            }
        }
    }
}