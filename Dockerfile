# Stage 1: Build file WAR bằng Maven với JDK 21
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml và mã nguồn
COPY pom.xml .
COPY src ./src

# Đóng gói WAR
RUN mvn clean package -DskipTests

# Stage 2: Khởi chạy bằng Tomcat 11 trên nền JDK 21
FROM tomcat:11.0-jdk21-temurin

# Xóa các ứng dụng mẫu mặc định của Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Đổi tên và copy thành ROOT.war để web chạy trực tiếp tại domain gốc (/)
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]

