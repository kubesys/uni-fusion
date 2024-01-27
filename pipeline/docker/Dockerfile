FROM alpine

USER root

#RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.tuna.tsinghua.edu.cn/g' /etc/apk/repositories && \
RUN sed -i 's/https/http/g' /etc/apk/repositories
#RUN apk update && \
RUN apk add --no-cache openrc docker git curl tar gcc g++ make wget \
    bash shadow openjdk11 py-pip python3-dev openssl-dev libffi-dev \
    libstdc++ harfbuzz nss freetype ttf-freefont iputils bind-tools maven go gradle
#    wget https://mirrors.cloud.tencent.com/gradle/gradle-7.3.3-bin.zip -P /tmp && \
#    unzip -d /opt/gradle /tmp/gradle-7.3.3-bin.zip && \
#    ln -s /opt/gradle/gradle-7.3.3 /opt/gradle/latest && \
#    ln -s /opt/gradle/latest/bin/gradle /usr/bin/gradle && \
#    ln -s /usr/share/maven/bin/mvn /usr/bin/mvn && \
#    rm /tmp/gradle-7.3.3-bin.zip && \

#RUN mkdir -p /root/.kube && \
RUN usermod -a -G docker root

#COPY config /root/.kube/
#COPY kubectl /usr/bin/
#RUN chmod +x /usr/bin/kubectl
