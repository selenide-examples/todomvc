plugins {
  `java-library`
}

repositories {
  jcenter()
}

dependencies {
  testImplementation("com.codeborne:selenide:5.15.1")
  testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.0")
  testRuntimeOnly("org.slf4j:slf4j-simple:1.7.30")
}

sourceSets {
  main {
    java.srcDir("src/main/java")
  }
  test {
    java.srcDir("src/test/java")
  }
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
  testLogging.showExceptions = true
  systemProperties["selenide.headless"] = System.getProperty("selenide.headless")
}