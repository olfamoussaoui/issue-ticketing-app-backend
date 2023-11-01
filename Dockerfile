FROM eclipse-temurin:latest
EXPOSE 8080
ADD target/issue-ticketing-backend-app.jar issue-ticketing-backend-app.jar

ENTRYPOINT ["java", "-jar", "issue-ticketing-backend-app.jar"]