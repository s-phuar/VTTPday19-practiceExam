FROM openjdk:23-jdk-oracle AS builder
#FROM exclispe-temurin:23-jdk-noble AS builder

WORKDIR /compileddir

#allows the build to run without requiring maven to be installed on host machine
COPY mvnw .
COPY todos.json .
#defines project dependencies and build settings
COPY pom.xml .
COPY .mvn .mvn
COPY src src

#add executable to all {file/folder name} to treat it as executable
RUN chmod a+x ./mvnw && ./mvnw package -Dmaven.test.skip=true
# RUN ./mvnw package -Dmaven.test.skip=true

ENV PORT=3000
EXPOSE ${PORT}

# below Worked previously but because railway uses PORT, hence PORT from railway to passed to PORT in dockerfiler
# ENV SERVER_PORT=3000
# EXPOSE ${SERVER_PORT}

#optional if multi stage
# ENTRYPOINT SEVER_PORT=${SERVER_PORT} java -jar target/inclass-0.0.1-SNAPSHOT.jar

#2nd build
FROM openjdk:23-jdk-oracle
#FROM exclispe-temurin:23-jre-noble

#working directory for 2nd stage
ARG WORK_DIR=/app
WORKDIR ${WORK_DIR}

#first arg from entrypoint above(previous container), 2nd arg name it anything.jar
#copies the 1st stage's jar file (from mvn package) into /app/anything.jar
COPY --from=builder /compileddir/target/practiceExam-0.0.1-SNAPSHOT.jar anything.jar

# Copy todos.json from the builder stage (assuming it's in /compileddir)
COPY --from=builder /compileddir/todos.json todos.json


ENV PORT=3000
EXPOSE ${PORT}

# below Worked previously but because railway uses PORT, hence PORT from railway to passed to PORT in dockerfiler
# ENV SERVER_PORT=3000
# EXPOSE ${SERVER_PORT}

#overrides the variables in application properties (this should be palceholder, do not put actual key)
ENV WEATHER_API_KEY=
#previously redis.host=localhost
    #my redis is running on host machine outside of docker, cannot use localhost to refer to it
    #so we want to connect redis instance using the host machine's ip address

ENV SPRING_DATA_REDIS_HOST=host.docker.internal
ENV SPRING_DATA_REDIS_PORT=6379
ENV SPRING_DATA_REDIS_DATABSE=0
ENV SPRING_DATA_REDIS_USERNAME=
ENV SPRING_DATA_REDIS_PASSWORD=

# ENV spring.data.redis.host=host.docker.internal
# ENV spring.data.redis.port=6379
# ENV spring.data.redis.database=0
# ENV spring.data.redis.username=
# ENV spring.data.redis.password=

#bring down from copy line (2nd arg)
ENTRYPOINT SERVER_PORT=${PORT} java -jar anything.jar

#docker will check localhost:4000/demo/health for healthiness, container marked as unhealthy if fails
HEALTHCHECK --interval=30s --timeout=30s --start-period=5s --retries=3 CMD curl -s -f http://localhost:4000/demo/health || exit 1

#when you docker image build, use the correct tag
#debuggin:   docker logs <container_id> && railway logs 


