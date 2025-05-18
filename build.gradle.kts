plugins {
	java
	id("org.springframework.boot") version "3.4.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.defi"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.github.f4b6a3:uuid-creator:6.0.0")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	implementation("org.flywaydb:flyway-core")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// lombok test
	testCompileOnly ("org.projectlombok:lombok")
	testAnnotationProcessor ("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

	// unit test
	testImplementation("org.mockito:mockito-core:5.12.0")
	testImplementation("org.mockito:mockito-junit-jupiter:5.12.0")

	// MapStruct
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<JavaCompile> {
	options.compilerArgs.add("-Xlint:deprecation")
}

tasks.register<Copy>("extractDependencies") {
	from(configurations.runtimeClasspath)
	into(layout.buildDirectory.dir("dependencies"))
	include("**/*.jar")
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

val appName = "kha-auth"
val appVersion = "1.0.0"

tasks.named<Jar>("jar") {
	archiveFileName.set("${appName}-${appVersion}.jar")
}
