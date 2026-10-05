/**
 * ODIN – tema claro/oscuro + notificaciones
 * Debe cargarse en cada página (body), no solo en el archivo layout.html suelto.
 */
(function () {
  'use strict';
  if (window.__odinUiLoaded) return;
  window.__odinUiLoaded = true;

  var STORAGE_KEY = 'odin-theme';

  function isDark() {
    return document.documentElement.classList.contains('dark');
  }

  function applyTheme(dark) {
    dark = !!dark;
    if (dark) {
      document.documentElement.classList.add('dark');
      document.documentElement.setAttribute('data-theme', 'dark');
    } else {
      document.documentElement.classList.remove('dark');
      document.documentElement.setAttribute('data-theme', 'light');
    }
    try {
      localStorage.setItem(STORAGE_KEY, dark ? 'dark' : 'light');
    } catch (e) {}

    var sun = document.getElementById('iconSun');
    var moon = document.getElementById('iconMoon');
    if (sun) {
      if (dark) sun.classList.remove('hidden');
      else sun.classList.add('hidden');
    }
    if (moon) {
      if (dark) moon.classList.add('hidden');
      else moon.classList.remove('hidden');
    }
  }

  function initTheme() {
    var saved = null;
    try { saved = localStorage.getItem(STORAGE_KEY); } catch (e) {}
    applyTheme(saved === 'dark');
  }

  window.odinToggleTheme = function (ev) {
    if (ev) { ev.preventDefault(); ev.stopPropagation(); }
    applyTheme(!isDark());
  };


  function csrfHeaders(headers) {
    var out = Object.assign({}, headers || {});
    var token = document.querySelector('meta[name="_csrf"]');
    var header = document.querySelector('meta[name="_csrf_header"]');
    if (token && header && token.content && header.content) out[header.content] = token.content;
    return out;
  }

  function escapeHtml(value) {
    return String(value == null ? '' : value).replace(/[&<>"']/g, function (c) {
      return ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'})[c];
    });
  }

  async function loadNotifications() {
    var list = document.getElementById('odinNotifList');
    if (!list) return;
    try {
      var response = await fetch('/api/notificaciones/resumen', { credentials: 'same-origin' });
      if (!response.ok) throw new Error('No se pudieron cargar');
      var items = await response.json();
      if (!items.length) {
        list.innerHTML = '<li class="px-4 py-4 text-sm text-slate-500">No hay alertas. <a href="/view/tramites?vencimiento=vencidos" class="text-green-700 font-semibold hover:underline">Ver vencidos</a></li>';
        return;
      }
      list.innerHTML = items.map(function (n) {
        var unread = n.leida === false;
        return '<li data-notif-id="' + escapeHtml(n.id_notificacion) + '" class="px-4 py-3 text-sm hover:bg-slate-50 ' + (unread ? 'bg-green-50/60 font-medium' : 'text-slate-500') + '">' +
          '<button type="button" class="w-full text-left" data-mark-notif="' + escapeHtml(n.id_notificacion) + '">' +
          '<span class="block text-slate-800">' + escapeHtml(n.titulo) + '</span>' +
          '<span class="block mt-0.5 text-xs text-slate-500">' + escapeHtml(n.mensaje) + '</span>' +
          '<span class="block mt-1 text-[10px] text-slate-400">' + escapeHtml(n.fecha || '') + '</span>' +
          '</button></li>';
      }).join('');
    } catch (e) {
      list.innerHTML = '<li class="px-4 py-4 text-sm text-red-600">No fue posible cargar las notificaciones.</li>';
    }
  }

  async function markNotificationRead(id) {
    try {
      var response = await fetch('/api/notificaciones/' + encodeURIComponent(id) + '/leer', {
        method: 'PUT', headers: csrfHeaders({'Content-Type': 'application/json'}), credentials: 'same-origin'
      });
      if (!response.ok) throw new Error('No se pudo actualizar');
      var item = document.querySelector('[data-notif-id="' + CSS.escape(String(id)) + '"]');
      if (item) { item.classList.remove('bg-green-50/60','font-medium'); item.classList.add('text-slate-500'); }
      loadNotifications();
      var badge = document.querySelector('#btnNotif span.absolute');
      if (badge) { var n = parseInt(badge.textContent, 10); if (!isNaN(n) && n > 1) badge.textContent = String(n - 1); else badge.remove(); }
    } catch (e) {}
  }

  function closeNotif() {
    var panel = document.getElementById('odinNotifPanel');
    var btn = document.getElementById('btnNotif');
    if (panel) {
      panel.classList.add('hidden');
      panel.style.display = 'none';
    }
    if (btn) btn.setAttribute('aria-expanded', 'false');
  }

  function openNotif() {
    var panel = document.getElementById('odinNotifPanel');
    var btn = document.getElementById('btnNotif');
    if (!panel || !btn) return;
    panel.classList.remove('hidden');
    panel.style.display = 'block';
    var rect = btn.getBoundingClientRect();
    panel.style.position = 'fixed';
    panel.style.top = (rect.bottom + 8) + 'px';
    panel.style.right = Math.max(8, window.innerWidth - rect.right) + 'px';
    panel.style.left = 'auto';
    panel.style.zIndex = '9999';
    btn.setAttribute('aria-expanded', 'true');
    loadNotifications();
  }

  window.odinToggleNotif = function (ev) {
    if (ev) { ev.preventDefault(); ev.stopPropagation(); }
    var panel = document.getElementById('odinNotifPanel');
    if (!panel) return;
    var open = panel.style.display === 'block' && !panel.classList.contains('hidden');
    if (open) closeNotif();
    else openNotif();
  };

  function bind() {
    initTheme();
    // Solo listeners si el botón NO tiene onclick (evita doble toggle)
    var btnTheme = document.getElementById('btnTheme');
    if (btnTheme && !btnTheme.getAttribute('onclick')) {
      btnTheme.addEventListener('click', function (e) { window.odinToggleTheme(e); });
    }
    var btnNotif = document.getElementById('btnNotif');
    if (btnNotif && !btnNotif.getAttribute('onclick')) {
      btnNotif.addEventListener('click', function (e) { window.odinToggleNotif(e); });
    }
    document.addEventListener('click', function (e) {
      var wrap = document.getElementById('odinNotifWrap');
      var panel = document.getElementById('odinNotifPanel');
      if (!panel || !wrap) return;
      if (wrap.contains(e.target) || panel.contains(e.target)) return;
      closeNotif();
    });
    document.addEventListener('click', function (e) {
      var mark = e.target.closest('[data-mark-notif]');
      if (mark) { e.preventDefault(); markNotificationRead(mark.getAttribute('data-mark-notif')); }
    });
    document.addEventListener('keydown', function (e) {
      if (e.key === 'Escape') closeNotif();
    });
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', bind);
  } else {
    bind();
  }
})();
