FROM node:16.17.0 as build

LABEL maintainer="bingshuai@nj.iscas.ac.cn"

COPY . /Frontend/test

RUN cd /Frontend/test \
    && npm install \
    && yarn install \
    && npm run build:prod

FROM nginx as nginx

COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=build /Frontend/test/dist /usr/share/nginx/html

EXPOSE 80
