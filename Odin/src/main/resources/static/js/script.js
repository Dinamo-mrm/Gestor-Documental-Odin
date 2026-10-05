/* ODIN - Gestión de Trámites: acciones con ventana flotante */
(function () {
  'use strict';

  function csrfHeaders() {
    var h = { 'Content-Type': 'application/json', 'X-Requested-With': 'XMLHttpRequest' };
    var t = document.querySelector('meta[name="_csrf"]');
    var n = document.querySelector('meta[name="_csrf_header"]');
    if (t && n) {
      h[n.getAttribute('content')] = t.getAttribute('content');
    } else {
      var input = document.querySelector('input[name="_csrf"]');
      if (input) h['X-CSRF-TOKEN'] = input.value;
    }
    var uid = document.querySelector('meta[name="odin-user-id"]');
    if (uid && uid.content) h['X-User-Id'] = uid.content;
    return h;
  }

  function selectedIds() {
    return Array.prototype.slice
      .call(document.querySelectorAll('#panel-bandeja tbody input[type="checkbox"]:checked, table tbody input[type="checkbox"]:checked'))
      .map(function (cb) { return cb.value; })
      .filter(function (v) { return v && v !== 'on'; });
  }

  function optionsFromCatalog(selectId) {
    var src = document.getElementById(selectId);
    if (!src) return '<option value="">Sin catálogo</option>';
    var html = '';
    Array.prototype.slice.call(src.options).forEach(function (o) {
      html += '<option value="' + o.value + '">' + o.textContent + '</option>';
    });
    return html || '<option value="">Sin opciones</option>';
  }

  var modal = { backdrop: null, box: null, title: null, help: null, fields: null, ok: null, cancel: null, resolve: null };

  function ensureModal() {
    if (modal.box) return;
    modal.backdrop = document.getElementById('odinModalBackdrop');
    modal.box = document.getElementById('odinModal');
    modal.title = document.getElementById('odinModalTitle');
    modal.help = document.getElementById('odinModalHelp');
    modal.fields = document.getElementById('odinModalFields');
    modal.ok = document.getElementById('odinModalOk');
    modal.cancel = document.getElementById('odinModalCancel');
    if (!modal.box) return;
    modal.cancel.addEventListener('click', function () { closeModal(null); });
    modal.backdrop.addEventListener('click', function () { closeModal(null); });
    modal.ok.addEventListener('click', function () {
      var data = {};
      modal.fields.querySelectorAll('[name]').forEach(function (el) {
        data[el.name] = el.value;
      });
      closeModal(data);
    });
  }

  function openModal(title, help, fieldsHtml) {
    return new Promise(function (resolve) {
      ensureModal();
      if (!modal.box) {
        resolve(null);
        return;
      }
      modal.resolve = resolve;
      modal.title.textContent = title;
      modal.help.textContent = help || '';
      modal.fields.innerHTML = fieldsHtml || '';
      modal.backdrop.hidden = false;
      modal.box.hidden = false;
    });
  }

  function closeModal(result) {
    if (modal.box) {
      modal.box.hidden = true;
      modal.backdrop.hidden = true;
    }
    if (modal.resolve) {
      var r = modal.resolve;
      modal.resolve = null;
      r(result);
    }
  }

  async function callApi(id, endpoint, method, body) {
    var response = await fetch('/api/radicados/' + id + '/' + endpoint, {
      method: method,
      headers: csrfHeaders(),
      body: body ? JSON.stringify(body) : undefined
    });
    var text = await response.text();
    var json = null;
    try { json = text ? JSON.parse(text) : null; } catch (e) { json = null; }
    if (!response.ok) {
      var msg = (json && (json.error || json.message)) ? (json.error || json.message) : text;
      throw new Error('Radicado ' + id + ': ' + (msg || ('HTTP ' + response.status)) + ' (HTTP ' + response.status + ')');
    }
    return json;
  }

  async function processSelected(endpoint, method, bodyFn, successMsg) {
    var ids = selectedIds();
    if (!ids.length) {
      alert('Seleccione al menos un radicado (casilla de la izquierda).');
      return;
    }
    var errors = [];
    for (var i = 0; i < ids.length; i++) {
      var id = ids[i];
      try {
        await callApi(id, endpoint, method, bodyFn(id));
      } catch (e) {
        errors.push(e.message || String(e));
      }
    }
    if (errors.length) {
      alert(errors.join('\n'));
    } else {
      alert(successMsg || 'Operación realizada.');
    }
    location.reload();
  }

  document.addEventListener('DOMContentLoaded', function () {
    ensureModal();

    document.querySelectorAll('[data-action]').forEach(function (btn) {
      btn.addEventListener('click', async function () {
        var action = btn.getAttribute('data-action');
        var ids = selectedIds();
        if (!ids.length) {
          alert('Seleccione al menos un radicado.');
          return;
        }

        try {
          if (action === 'asignar') {
            var htmlA =
              '<label>Usuario responsable</label>' +
              '<select name="usuario">' + optionsFromCatalog('catalogoUsuarios') + '</select>';
            var dataA = await openModal(
              'Asignar responsable',
              'Se asignará a ' + ids.length + ' radicado(s) seleccionado(s).',
              htmlA
            );
            if (!dataA || !dataA.usuario) return;
            await processSelected('asignar', 'PATCH', function () {
              return { usuario: Number(dataA.usuario) };
            }, 'Asignación realizada.');
          }

          if (action === 'estado') {
            var htmlE =
              '<label>Nuevo estado</label>' +
              '<select name="estado">' + optionsFromCatalog('catalogoEstados') + '</select>';
            var dataE = await openModal(
              'Cambiar estado',
              'Se aplicará a ' + ids.length + ' radicado(s) seleccionado(s).',
              htmlE
            );
            if (!dataE || !dataE.estado) return;
            await processSelected('estado', 'PATCH', function () {
              return { estado: Number(dataE.estado) };
            }, 'Estado actualizado.');
          }

          if (action === 'reasignar') {
            var htmlR =
              '<label>Nuevo usuario responsable</label>' +
              '<select name="usuarioNuevo">' + optionsFromCatalog('catalogoUsuarios') + '</select>' +
              '<label>Nueva dependencia</label>' +
              '<select name="dependenciaNueva">' + optionsFromCatalog('catalogoDependencias') + '</select>';
            var dataR = await openModal(
              'Reasignar',
              'Nuevo responsable y dependencia para ' + ids.length + ' radicado(s).',
              htmlR
            );
            if (!dataR || !dataR.usuarioNuevo || !dataR.dependenciaNueva) {
              alert('Debe seleccionar usuario y dependencia.');
              return;
            }
            await processSelected('reasignar', 'POST', function () {
              return {
                usuarioNuevo: Number(dataR.usuarioNuevo),
                dependenciaNueva: Number(dataR.dependenciaNueva)
              };
            }, 'Reasignación realizada.');
          }

          if (action === 'cerrar') {
            var htmlC =
              '<label>Observación de cierre (opcional)</label>' +
              '<textarea name="observacion" rows="3" placeholder="Motivo o comentario de cierre"></textarea>';
            var dataC = await openModal(
              'Cerrar / Finalizar trámite',
              'Se cerrarán ' + ids.length + ' radicado(s). Esta acción registra fecha de cierre.',
              htmlC
            );
            if (!dataC) return;
            await processSelected('cerrar', 'PATCH', function () {
              return { observacion: dataC.observacion || 'Cierre desde bandeja de trámites' };
            }, 'Radicado(s) cerrado(s).');
          }
        } catch (e) {
          alert(e.message || String(e));
        }
      });
    });
  });
})();
