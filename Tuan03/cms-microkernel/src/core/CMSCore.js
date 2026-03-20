// CMS CORE — Microkernel
// Đây là "lõi nhỏ" của hệ thống.
// Core KHÔNG biết logic của plugin — chỉ biết interface chuẩn.
// Tất cả plugin kết nối vào core qua register().

const { pool } = require('../config/database');

class CMSCore {
  constructor() {
    this.plugins = new Map();      // name → plugin instance (reference gốc)
    this.hookRegistry = new Map(); // eventName → [{ pluginName, fn }]
    console.log('🔧 CMS Core initialized');
  }

  // ── Plugin Registry ──────────────────────────────────
  // Lưu REFERENCE gốc của plugin — không spread/copy
  // Vì activate() gán this.api sau đó, phải trỏ đúng object gốc
  register(plugin) {
    if (!plugin.name) throw new Error('Plugin must have a name');
    if (typeof plugin.activate !== 'function') throw new Error('Plugin must implement activate()');
    if (typeof plugin.deactivate !== 'function') throw new Error('Plugin must implement deactivate()');

    plugin.status = 'registered';
    this.plugins.set(plugin.name, plugin); // lưu reference, không spread
    console.log(`📥 Core: plugin registered → ${plugin.name}`);
  }

  getPlugin(name) {
    const p = this.plugins.get(name);
    if (!p) throw new Error(`Plugin "${name}" not registered in core`);
    return p;
  }

  listPlugins() {
    return Array.from(this.plugins.values()).map(p => ({
      name: p.name,
      version: p.version,
      status: p.status,
    }));
  }

  // ── Hook System ──────────────────────────────────────
  on(eventName, pluginName, fn) {
    if (!this.hookRegistry.has(eventName)) {
      this.hookRegistry.set(eventName, []);
    }
    this.hookRegistry.get(eventName).push({ pluginName, fn });
  }

  async emit(eventName, data) {
    const handlers = this.hookRegistry.get(eventName) || [];
    const results = [];
    for (const { pluginName, fn } of handlers) {
      try {
        const result = await fn(data);
        results.push({ pluginName, result });
        console.log(`  🔔 Core emit [${eventName}] → ${pluginName}`);
      } catch (err) {
        // Nếu hook throw error thì propagate lên (ví dụ before_save validation)
        throw err;
      }
    }
    return results;
  }

  offPlugin(pluginName) {
    for (const [event, handlers] of this.hookRegistry.entries()) {
      this.hookRegistry.set(event, handlers.filter(h => h.pluginName !== pluginName));
    }
  }

  // ── Database access ──────────────────────────────────
  getDB() {
    return pool;
  }
}

// Singleton core
const core = new CMSCore();
module.exports = core;
