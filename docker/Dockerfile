FROM ubuntu/jre8:0.231

LABEL name="common-hsqldb"
LABEL build-date="2019-10-23"

ADD hsqldb/* ${APP_ROOT}/hsqldb/
WORKDIR ${APP_ROOT}/hsqldb/
VOLUME ${APP_ROOT}/hsqldb/db

ENTRYPOINT sh ${APP_ROOT}/hsqldb/run.sh

EXPOSE 9001