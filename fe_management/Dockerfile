FROM node:18-alpine AS build
WORKDIR /app

COPY package*.json ./

RUN npm install --legacy-peer-deps && \
    npm install ajv@^8 ajv-keywords@^5 --legacy-peer-deps

COPY . .

ARG REACT_APP_API_URL=http://localhost:8080
ENV REACT_APP_API_URL=$REACT_APP_API_URL

RUN npm run build

FROM nginx:1.25-alpine
WORKDIR /usr/share/nginx/html

RUN rm -rf ./*

COPY --from=build /app/build .

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]