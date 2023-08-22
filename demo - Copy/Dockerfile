# Sử dụng hình ảnh OpenJDK 8 làm cơ sở
FROM openjdk:8-jdk-alpine
WORKDIR /
ENV PORT 8080
EXPOSE 8080
COPY ./target/demo-app.jar /demo-app.jar
ENTRYPOINT ["java", "-jar","/demo-app.jar"]
