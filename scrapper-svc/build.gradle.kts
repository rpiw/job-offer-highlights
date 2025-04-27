import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	java
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "io.rp"
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

dependencyManagement {
	imports {
		mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webflux")

	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.projectlombok:lombok")

	testImplementation("org.springframework.boot:spring-boot-starter-test")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation("com.microsoft.playwright:playwright:1.51.0")
//	implementation("org.springframework.boot:spring-boot-starter-actuator")
//	implementation("io.micrometer:micrometer-tracing-bridge-otel")
//	implementation("io.opentelemetry:opentelemetry-exporter-zipkin")

	testImplementation("org.assertj:assertj-core:3.27.3")

}

springBoot {
	mainClass.set("io.rp.job.offer.viewer.JobOfferScrapperApplication")
	buildInfo()
}

tasks.named<BootJar>("bootJar") {
	archiveFileName.set("app.jar")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

