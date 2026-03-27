# Stage 1: Build React + Vite
FROM node:20-alpine AS builder

WORKDIR /app

# Copy package.json trước để cache
COPY package*.json ./
RUN npm install

# Copy toàn bộ source code
COPY . .

# Vite build ra thư mục dist/
RUN npm run build

# ──────────────────────────────────────
# Stage 2: Serve bằng Nginx
FROM nginx:alpine

# Vite output là dist/ (khác với CRA là build/)
COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80