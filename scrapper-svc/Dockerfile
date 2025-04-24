FROM gradle:8.13.0-jdk21-alpine AS build

WORKDIR /app/scrapper-svc
COPY build.gradle.kts ./
COPY src src/

WORKDIR /app/
COPY --from=gradle-root gradle gradle/
COPY --from=gradle-root gradlew settings.gradle.kts ./

RUN --mount=type=cache,target=/home/gradle/.gradle,from=gradle-root,source=.gradle \
    --mount=type=bind,target=src \
    ./gradlew clean build --no-daemon -x test

FROM mcr.microsoft.com/playwright/java:v1.51.0-noble AS runtime
ENV USER=user

RUN apt-get update && apt-get install --no-install-recommends -y curl \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* \
    && useradd -m -s /bin/bash $USER

USER $USER
EXPOSE 8080
COPY --from=build /app/scrapper-svc/build/libs /app/
CMD ["java", "-jar", "/app/app.jar"]
