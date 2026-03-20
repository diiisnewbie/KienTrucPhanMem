// PLUGIN: Hook System
// Chức năng: quản lý việc đăng ký và lưu trữ hook vào DB
// Kết nối vào Core — độc lập với plugin khác

const core = require('../../core/CMSCore');

const HookSystemPlugin = {
  name: 'hook-system',
  version: '1.0.0',

  activate() {
    const db = core.getDB();
    console.log('✅ HookSystem plugin: activated');

    // Lắng nghe chính các hook để log vào DB
    core.on('after_save', this.name, async (data) => {
      return `[hook-system] logged after_save: ${data.title || data.id}`;
    });

    core.on('after_publish', this.name, async (data) => {
      return `[hook-system] logged after_publish: ${data.title}`;
    });

    this.api = {
      // Lấy tất cả hook đã đăng ký trong DB
      async getHooks(eventName) {
        if (eventName) {
          const [rows] = await db.execute(
            'SELECT * FROM hooks WHERE event_name = ? ORDER BY created_at ASC',
            [eventName]
          );
          return rows;
        }
        const [rows] = await db.execute('SELECT * FROM hooks ORDER BY event_name');
        return rows;
      },

      // Đăng ký hook mới (lưu vào DB để track)
      async registerHook(eventName, pluginName, callbackInfo) {
        if (!eventName || !pluginName) throw new Error('eventName and pluginName are required');
        const [r] = await db.execute(
          'INSERT INTO hooks (event_name, plugin_name, callback_info) VALUES (?, ?, ?)',
          [eventName, pluginName, callbackInfo || '']
        );
        return { id: r.insertId, eventName, pluginName, callbackInfo };
      },

      // Xoá hook
      async removeHook(eventName, pluginName) {
        await db.execute(
          'DELETE FROM hooks WHERE event_name = ? AND plugin_name = ?',
          [eventName, pluginName]
        );
        core.offPlugin(pluginName);
        return { removed: true, eventName, pluginName };
      },

      // Phát sự kiện thủ công qua core
      async fireEvent(eventName, data) {
        const results = await core.emit(eventName, data);
        return { eventName, fired: true, results };
      },
    };
  },

  deactivate() {
    core.offPlugin(this.name);
    this.api = null;
    console.log('⏸  HookSystem plugin: deactivated');
  },
};

module.exports = HookSystemPlugin;
