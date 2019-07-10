FROM alpine/git
WORKDIR /app
RUN git clone https://github.com/pipinoclaudia/chat.git
WORKDIR ./chat


FROM maven:3.5-jdk-8-alpine
WORKDIR /app
COPY --from=0 /app/chat /app
RUN mvn clean compile assembly:single

FROM openjdk:8-jre-alpine
WORKDIR /app
ENV MASTERIP=127.0.0.1
ENV ID=0
COPY --from=1 /app/target/chat-0.0.1-jar-with-dependencies.jar /app

CMD /usr/bin/java -jar chat-0.0.1-jar-with-dependencies.jar -m $MASTERIP -id $ID
