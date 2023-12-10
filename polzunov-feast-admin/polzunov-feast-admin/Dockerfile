FROM node:21-alpine AS builder
WORKDIR /app
COPY . .
RUN npm ci
RUN npm run generate-api
RUN npm run build

FROM nginx:1.25-alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf
CMD ["nginx", "-g", "daemon off;"]