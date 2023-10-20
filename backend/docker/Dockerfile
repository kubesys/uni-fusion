FROM openjdk:17.0.2-slim

RUN mkdir -p /home/kube-backend/config

ADD kube-backend.jar /home/kube-backend/
ADD config/ /home/kube-backend/config

WORKDIR /home/kube-backend

ENTRYPOINT ["java", "-jar","kube-backend.jar"]