FROM node:16.17.0 as build

LABEL maintainer="bingshuai@nj.iscas.ac.cn"

COPY . /Frontend/test

RUN cd /Frontend/test \
    && npm install \
    && yarn install \
    && npm run build:prod

FROM nginx as nginx

# 将前端构建后的静态文件复制到 Nginx 的 HTML 根目录
COPY --from=build /Frontend/test/dist /usr/share/nginx/html

# 添加 Nginx 配置文件，定义代理规则
COPY nginx.conf /etc/nginx/conf.d/default.conf

CMD ["nginx", "-g", "daemon off;"]
