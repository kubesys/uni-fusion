FROM openjdk:17.0.2-slim

RUN mkdir -p /home/kube-mirror

ADD kube-mirror.jar /home/kube-mirror/

WORKDIR /home/kube-mirror

ENTRYPOINT ["java", "-jar","kube-mirror.jar"]