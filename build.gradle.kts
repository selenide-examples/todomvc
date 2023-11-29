plugins {
  `java-library`
}

repositories {
  jcenter()
}

dependencies {
    components.all<NettyBomAlignmentRule>()
    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.codeborne:selenide:7.0.3")
    testRuntimeOnly("org.slf4j:slf4j-simple:2.0.9")
    constraints {
        implementation("com.google.guava:guava:32.1.3-jre") {
            because("latest compatible version with all dependencies")
        }
    }
    testImplementation("com.microsoft.playwright:playwright:1.40.0")
    testImplementation("com.microsoft.playwright:driver-bundle:1.40.0")
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