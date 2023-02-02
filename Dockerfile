FROM maven:3.8.4-eclipse-temurin-11 as builder
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
ENV ACTIVE_PROFILE=staging_svc
RUN --mount=type=cache,target=/root/.m2 mvn -f $HOME/pom.xml clean install

FROM eclipse-temurin:11-jre-alpine 

RUN rm -f /etc/localtime \
&& ln -sv /usr/share/zoneinfo/Asia/Dhaka /etc/localtime \
&& echo "Asia/Dhaka" > /etc/timezone

WORKDIR /opt/app
ENV ACTIVE_PROFILE=staging_svc
EXPOSE 9090
COPY --from=builder /usr/app/target/*.jar /opt/app/staging-api-exposure.jar
COPY --from=builder /usr/app/src/main/resources/application-staging_svc.properties /usr/local/lib/application-staging_svc.properties
ENTRYPOINT ["java", "-jar","-Dspring.profiles.active=staging_svc", "/opt/app/staging-api-exposure.jar" ]
