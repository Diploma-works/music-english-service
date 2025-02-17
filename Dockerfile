FROM maven:3.8.5-openjdk-17
WORKDIR /${service}
COPY . .
ARG service
ENV service=$service
RUN mvn clean install -DskipTests -pl ${service} -amd

CMD java -jar ${service}/target/${service}-1.0.jar
