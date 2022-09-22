# === STAGE 1 ================================================================ #
# Build the .jar
# ============================================================================ #
FROM clojure:tools-deps-1.11.1.1165-jammy AS builder

LABEL maintainer="giacomo@giacomodebidda.com"

# An ARG instruction goes out of scope at the end of the build stage where it
# was defined.
# To use an arg in multiple stages, EACH STAGE must include the ARG instruction.
# https://docs.docker.com/engine/reference/builder/#scope
ARG APP_NAME
RUN if [ -z "${APP_NAME}" ] ; then echo "The APP_NAME argument is missing!" ; exit 1; fi

ENV APP_DIR=/usr/src/app

RUN mkdir -p ${APP_DIR}

WORKDIR ${APP_DIR}

COPY deps.edn ${APP_DIR}/
COPY build.clj ${APP_DIR}/
COPY src ${APP_DIR}/src

RUN clojure -T:build uber

# === STAGE 2 ================================================================ #
# Copy the .jar built at stage 1 and run it
# ============================================================================ #
# https://hub.docker.com/_/eclipse-temurin
FROM eclipse-temurin:11

# RUN apt-get update && apt-get install --quiet --assume-yes sudo \
#   lsb-release \
#   tree

# Each ARG goes out of scope at the end of the build stage where it was
# defined. That's why we have to repeat it here in this stage.
ARG APP_NAME

ENV APP_GROUP=dk-group \
    APP_USER=dk-user \
    APP_PORT=8080 \
    BUILDER_APP_DIR=/usr/src/app \
    JAR_NAME=hello-cloud-run-clj-1.2.0-standalone.jar

# add a non-privileged user
RUN groupadd --system ${APP_GROUP} && \
    useradd --system --gid ${APP_GROUP} --create-home ${APP_USER} --comment "container user account" && \
    mkdir -p /home/${APP_USER}/${APP_NAME}

WORKDIR /home/${APP_USER}/${APP_NAME}

# open a non-privileged port for the app to listen to
EXPOSE ${APP_PORT}

COPY --from=builder ${BUILDER_APP_DIR}/target/${JAR_NAME} ./

RUN chown -R ${APP_USER} ./

# run everything AFTER as non-privileged user
USER ${APP_USER}

# RUN echo "App ${APP_NAME} will be run by user $(whoami) on $(lsb_release -i -s) $(lsb_release -r -s) and will listen on port ${APP_PORT}"

# RUN tree -a -L 3 .
# RUN ls -1la

# This does not work:
# ENTRYPOINT ["java", "-jar", $JAR_NAME]
# While this works:
ENTRYPOINT java -jar ${JAR_NAME}
# Here is why:
# https://stackoverflow.com/questions/37904682/how-do-i-use-docker-environment-variable-in-entrypoint-array
