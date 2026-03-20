require('dotenv').config();
const express = require('express');
const path = require('path');
const { initDB } = require('./config/database');
const core = require('./core/CMSCore');
const routes = require('./routes');

// Import plugins
const ContentManagementPlugin = require('./plugins/content-management');
const HookSystemPlugin = require('./plugins/hook-system');
const LifecyclePlugin = require('./plugins/lifecycle');

const app = express();
app.use(express.json());

// CORS — cho phép HTML local và browser kết nối
app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Methods', 'GET,POST,PUT,DELETE,OPTIONS');
  res.header('Access-Control-Allow-Headers', 'Content-Type');
  if (req.method === 'OPTIONS') return res.sendStatus(200);
  next();
});

// Serve file HTML dashboard
app.use(express.static(path.join(__dirname, '../../')));

// ──────────────────────────────────────────
//  Microkernel Architecture
//
//  [Client] ──call──► [Plugin A] ──call──► [Core]
//  [Client] ◄──result── [Plugin A] ◄──result── [Core]
//
//  Mỗi plugin kết nối thẳng vào Core.
//  Các plugin KHÔNG biết nhau.
//  Core chỉ biết interface: { name, activate(), deactivate() }
// ──────────────────────────────────────────

async function bootstrap() {
  await initDB();

  // 1. Đăng ký tất cả plugin vào core
  core.register(ContentManagementPlugin);
  core.register(HookSystemPlugin);
  core.register(LifecyclePlugin);

  // 2. Kích hoạt plugin (gọi activate() trên từng plugin)
  ContentManagementPlugin.activate();
  HookSystemPlugin.activate();
  LifecyclePlugin.activate();

  // 3. Gắn routes
  app.use('/api', routes);

  app.get('/', (req, res) => {
    res.json({
      project: 'CMS - Microkernel Architecture',
      architecture: 'Microkernel (core + plugins)',
      core: 'CMSCore — plugin registry + hook emitter',
      plugins: core.listPlugins(),
      features: ['Content Management', 'Hook System', 'Plugin Lifecycle'],
      endpoints: {
        contentTypes: 'GET/POST /api/content-types',
        contents: 'GET/POST/PUT/DELETE /api/contents',
        plugins: 'GET /api/plugins | POST /api/plugins/install | POST /api/plugins/:name/activate',
        hooks: 'GET/POST/DELETE /api/hooks | POST /api/hooks/fire',
      },
    });
  });

  const PORT = process.env.PORT || 4000;
  app.listen(PORT, () => {
    console.log(`\n🚀 CMS Microkernel Architecture running on http://localhost:${PORT}`);
    console.log(`📖 API docs: http://localhost:${PORT}/\n`);
  });
}

bootstrap().catch(err => {
  console.error('❌ Failed to start:', err.message);
  process.exit(1);
});
