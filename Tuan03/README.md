# CMS Plugin-based — Layer vs Microkernel

## Cấu trúc 2 project

```
cms-layer/                        cms-microkernel/
├── config/database.js            ├── config/database.js
├── src/                          ├── src/
│   ├── layers/                   │   ├── core/
│   │   ├── presentation/         │   │   └── CMSCore.js          ← Lõi trung tâm
│   │   │   └── presentationLayer.js  │   ├── plugins/
│   │   ├── business/             │   │   ├── content-management/
│   │   │   └── businessLayer.js  │   │   │   └── index.js
│   │   └── dataAccess/           │   │   ├── hook-system/
│   │       └── dataAccessLayer.js│   │   │   └── index.js
│   └── index.js                  │   │   └── lifecycle/
└── package.json                  │   │       └── index.js
                                  │   ├── routes.js
                                  │   └── index.js
                                  └── package.json
```

---

## Cài đặt và chạy

### 1. Tạo database MySQL

```sql
CREATE DATABASE cms_layer;
CREATE DATABASE cms_microkernel;
```

### 2. Layer project (port 3000)

```bash
cd cms-layer
cp .env.example .env        # chỉnh DB_PASSWORD nếu cần
npm install
npm start
```

### 3. Microkernel project (port 4000)

```bash
cd cms-microkernel
cp .env.example .env        # chỉnh DB_PASSWORD nếu cần
npm install
npm start
```

---

## API endpoints (giống nhau cho cả 2 project)

### Content Management
| Method | URL | Mô tả |
|--------|-----|-------|
| GET | /api/content-types | Lấy danh sách content type |
| POST | /api/content-types | Đăng ký content type mới |
| GET | /api/contents | Lấy tất cả content |
| GET | /api/contents?type=blog | Lọc theo type |
| GET | /api/contents/:id | Lấy 1 content |
| POST | /api/contents | Tạo content mới |
| PUT | /api/contents/:id | Cập nhật content |
| DELETE | /api/contents/:id | Xoá content |

### Plugin Lifecycle
| Method | URL | Mô tả |
|--------|-----|-------|
| GET | /api/plugins | Danh sách plugin |
| POST | /api/plugins/install | Cài plugin |
| POST | /api/plugins/:name/activate | Kích hoạt plugin |
| POST | /api/plugins/:name/deactivate | Tắt plugin |
| DELETE | /api/plugins/:name | Gỡ plugin |

### Hook System
| Method | URL | Mô tả |
|--------|-----|-------|
| GET | /api/hooks | Danh sách hook |
| GET | /api/hooks?event=after_save | Lọc theo event |
| POST | /api/hooks/register | Đăng ký hook mới |
| DELETE | /api/hooks | Xoá hook |
| POST | /api/hooks/fire | Phát event thủ công (Microkernel only) |

---

## Ví dụ test nhanh

```bash
# 1. Đăng ký content type
curl -X POST http://localhost:3000/api/content-types \
  -H "Content-Type: application/json" \
  -d '{"name":"blog","description":"Blog posts"}'

# 2. Tạo content
curl -X POST http://localhost:3000/api/contents \
  -H "Content-Type: application/json" \
  -d '{"typeName":"blog","title":"Hello World","body":"My first post","status":"published"}'

# 3. Cài plugin
curl -X POST http://localhost:3000/api/plugins/install \
  -H "Content-Type: application/json" \
  -d '{"name":"seo-plugin","version":"1.0.0"}'

# 4. Kích hoạt plugin
curl -X POST http://localhost:3000/api/plugins/seo-plugin/activate

# 5. Xem hooks đã đăng ký
curl http://localhost:3000/api/hooks
```

---

## So sánh kiến trúc

| | Layer | Microkernel |
|---|---|---|
| Request flow | Đi qua **từng tầng** tuần tự | Plugin gọi **thẳng vào Core** |
| Thêm tính năng | Thêm vào tầng tương ứng | Tạo plugin mới, register vào core |
| Plugin biết nhau? | Có thể (cùng tầng) | **Không** — chỉ biết Core |
| Tắt 1 tính năng | Phải sửa code tầng | Deactivate plugin, Core không đổi |
| Port | 3000 | 4000 |
