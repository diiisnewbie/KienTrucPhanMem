// PLUGIN: Lifecycle Management
// Chức năng: install, activate, deactivate, uninstall plugin khác
// Kết nối vào Core — độc lập với plugin khác

const core = require('../../core/CMSCore');

const LifecyclePlugin = {
  name: 'lifecycle',
  version: '1.0.0',

  activate() {
    const db = core.getDB();
    console.log('✅ Lifecycle plugin: activated');

    this.api = {
      // Lấy danh sách plugin từ DB
      async getPlugins() {
        const [rows] = await db.execute('SELECT * FROM plugins ORDER BY created_at DESC');
        return rows;
      },

      // Cài plugin (lưu vào DB, status = installed)
      async installPlugin(name, version = '1.0.0') {
        if (!name) throw new Error('Plugin name is required');
        const [exists] = await db.execute('SELECT id FROM plugins WHERE name = ?', [name]);
        if (exists.length) throw new Error(`Plugin "${name}" already installed`);
        const [r] = await db.execute(
          'INSERT INTO plugins (name, version, status) VALUES (?, ?, "installed")',
          [name, version]
        );
        console.log(`📦 Lifecycle: installed plugin → ${name}`);
        return { id: r.insertId, name, version, status: 'installed' };
      },

      // Kích hoạt plugin (status = active)
      // Core sẽ gọi plugin.activate() nếu plugin đã register vào core
      async activatePlugin(name) {
        const [rows] = await db.execute('SELECT * FROM plugins WHERE name = ?', [name]);
        if (!rows.length) throw new Error(`Plugin "${name}" not found`);
        if (rows[0].status === 'active') throw new Error(`Plugin "${name}" already active`);

        // Gọi activate trên plugin instance nếu có trong core
        try {
          const pluginInstance = core.getPlugin(name);
          if (pluginInstance && typeof pluginInstance.activate === 'function') {
            pluginInstance.activate();
          }
        } catch (_) {
          // Plugin chưa register vào core — chỉ update DB
        }

        await db.execute('UPDATE plugins SET status = "active" WHERE name = ?', [name]);
        const [updated] = await db.execute('SELECT * FROM plugins WHERE name = ?', [name]);
        console.log(`✅ Lifecycle: activated plugin → ${name}`);

        // Đăng ký hook minh hoạ khi plugin active
        await db.execute(
          'INSERT INTO hooks (event_name, plugin_name, callback_info) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE callback_info = VALUES(callback_info)',
          ['after_save', name, `auto-hook registered when ${name} activated`]
        );

        return updated[0];
      },

      // Tắt plugin (status = inactive)
      async deactivatePlugin(name) {
        const [rows] = await db.execute('SELECT * FROM plugins WHERE name = ?', [name]);
        if (!rows.length) throw new Error(`Plugin "${name}" not found`);
        if (rows[0].status !== 'active') throw new Error(`Plugin "${name}" is not active`);

        try {
          const pluginInstance = core.getPlugin(name);
          if (pluginInstance && typeof pluginInstance.deactivate === 'function') {
            pluginInstance.deactivate();
          }
        } catch (_) {}

        await db.execute('UPDATE plugins SET status = "inactive" WHERE name = ?', [name]);
        const [updated] = await db.execute('SELECT * FROM plugins WHERE name = ?', [name]);
        console.log(`⏸  Lifecycle: deactivated plugin → ${name}`);
        return updated[0];
      },

      // Gỡ plugin hoàn toàn
      async uninstallPlugin(name) {
        const [rows] = await db.execute('SELECT * FROM plugins WHERE name = ?', [name]);
        if (!rows.length) throw new Error(`Plugin "${name}" not found`);
        if (rows[0].status === 'active') throw new Error(`Deactivate "${name}" before uninstalling`);

        await db.execute('DELETE FROM plugins WHERE name = ?', [name]);
        await db.execute('DELETE FROM hooks WHERE plugin_name = ?', [name]);
        core.offPlugin(name);
        console.log(`🗑  Lifecycle: uninstalled plugin → ${name}`);
        return { uninstalled: true, name };
      },
    };
  },

  deactivate() {
    core.offPlugin(this.name);
    this.api = null;
    console.log('⏸  Lifecycle plugin: deactivated');
  },
};

module.exports = LifecyclePlugin;
