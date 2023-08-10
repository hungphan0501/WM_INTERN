# Sử dụng hình ảnh MySQL 8.0 làm cơ sở
FROM openjdk:8-jdk-alpine
ENV PORT 8080
EXPOSE 8080
COPY target/demo-app.jar demo-app.jar
ENTRYPOINT ["java", "-jar","/demo-app.jar"]
