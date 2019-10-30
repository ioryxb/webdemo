FROM ubuntu/jre8:0.231

LABEL name="crxdemo"
LABEL build-date="2019-10-23"
ENV APP_NAME=ticketoffice
ENV APP_VERSION=0.0.1-SNAPSHOT

COPY ${APP_NAME}-${APP_VERSION}.jar ${APP_ROOT}
COPY db/migration/* ${APP_ROOT}/db/migration/
COPY application.properties ${APP_ROOT}
WORKDIR ${APP_ROOT}/

VOLUME ${APP_ROOT}/logs

ENTRYPOINT java -jar ${APP_NAME}-${APP_VERSION}.jar --spring.flyway.locations=filesystem:db/migration --spring.datasource.url=jdbc:hsqldb:hsql://aliashsqldbsource:9001/mydb > ${APP_ROOT}/logs/app.log

EXPOSE 8081