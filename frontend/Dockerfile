FROM node:16.17.0 as build

LABEL maintainer="bingshuai@nj.iscas.ac.cn"

COPY . /kube-frontend

RUN cd /kube-frontend \
    && npm install \
    && yarn install \
    && npm run build:prod

FROM nginx as nginx

COPY --from=build /kube-frontend/dist /usr/share/nginx/html

CMD ["nginx", "-g", "daemon off;"]

