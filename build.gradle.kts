plugins {
  `java-library`
}

repositories {
  jcenter()
}

dependencies {
    components.all<NettyBomAlignmentRule>()
    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.codeborne:selenide:5.16.2")
    testRuntimeOnly("org.slf4j:slf4j-simple:1.7.30")
    constraints {
        implementation("com.google.guava:guava:30.0-jre") {
            because("latest compatible version with all dependencies")
        }
    }
}

java {
  toolchain {
      languageVersion.set(JavaLanguageVersion.of(11))
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