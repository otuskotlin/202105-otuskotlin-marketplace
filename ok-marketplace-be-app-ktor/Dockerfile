# Run gradle task `installDist` at first
FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/install/ok-marketplace-be-app-ktor/ /app/
WORKDIR /app/bin
CMD ["./ok-marketplace-be-app-ktor"]
