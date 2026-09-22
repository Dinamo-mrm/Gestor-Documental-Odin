/* ODIN - Gestión de Trámites */
(function () {
    'use strict';

    const jsonHeaders = {'Content-Type': 'application/json'};

    function selectedIds() {
        return [...document.querySelectorAll('tbody input[type="checkbox"]:checked')]
            .map(el => el.value).filter(Boolean);
    }

    async function requestRadicado(id, endpoint, method, payload = {}) {
        const response = await fetch(`/api/radicados/${id}/${endpoint}`, {
            method,
            headers: jsonHeaders,
            body: JSON.stringify(payload)
        });
        if (!response.ok) {
            let detail = '';
            try {
                const data = await response.json();
                detail = data.error ? `: ${data.error}` : '';
            } catch (_) { }
            throw new Error(`No se pudo procesar el radicado ${id}${detail}`);
        }
        return response.json();
    }

    async function processSelected(endpoint, method, payloadFactory, message) {
        const ids = selectedIds();
        if (!ids.length) {
            alert('Seleccione al menos un radicado.');
            return;
        }
        if (!confirm(message)) return;
        try {
            for (const id of ids) {
                await requestRadicado(id, endpoint, method, payloadFactory());
            }
            alert('Operación realizada correctamente.');
            window.location.reload();
        } catch (error) {
            alert(error.message);
        }
    }

    function exportarTabla() {
        const table = document.querySelector('table');
        if (!table) return;
        const rows = [...table.querySelectorAll('tr')].map(row =>
            [...row.querySelectorAll('th,td')]
                .slice(1, -1)
                .map(cell => `"${cell.innerText.replace(/"/g, '""').replace(/\n/g, ' ')}"`)
                .join(';')
        );
        const blob = new Blob([rows.join('\n')], {type: 'text/csv;charset=utf-8;'});
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `odin-tramites-${new Date().toISOString().slice(0,10)}.csv`;
        link.click();
        URL.revokeObjectURL(url);
    }

    document.addEventListener('DOMContentLoaded', () => {
        const master = document.querySelector('thead input[type="checkbox"]');
        if (master) {
            master.addEventListener('change', () => {
                document.querySelectorAll('tbody input[type="checkbox"]')
                    .forEach(cb => cb.checked = master.checked);
            });
        }

        const filterForm = document.querySelector('#formFiltros');
        if (filterForm) {
            filterForm.addEventListener('submit', e => {
                e.preventDefault();
                const params = new URLSearchParams(new FormData(filterForm));
                [...params.keys()].forEach(key => {
                    if (!params.get(key)) params.delete(key);
                });
                window.location.href = `/view/tramites?${params.toString()}`;
            });
        }

        const exportButton = document.querySelector('#btnExportar');
        if (exportButton) exportButton.addEventListener('click', exportarTabla);

        const vencidosKpi = document.querySelector('#kpiVencidos');
        if (vencidosKpi) {
            vencidosKpi.addEventListener('click', () => {
                window.location.href = '/view/tramites?vencimiento=vencidos';
            });
        }

        const vencimientoFilter = document.querySelector('#filtroVencimiento');
        const params = new URLSearchParams(window.location.search);
        if (vencimientoFilter && params.has('vencimiento')) {
            vencimientoFilter.value = params.get('vencimiento');
        }

        document.querySelectorAll('[data-action]').forEach(button => {
            button.addEventListener('click', () => {
                const action = button.dataset.action;
                if (action === 'estado') {
                    const estado = prompt('Ingrese el ID del nuevo estado:');
                    if (estado && Number(estado) > 0) {
                        processSelected('estado', 'PATCH', () => ({estado: Number(estado)}), '¿Cambiar el estado de los radicados seleccionados?');
                    }
                }
                if (action === 'asignar') {
                    const usuario = prompt('Ingrese el ID del usuario responsable:');
                    if (usuario && Number(usuario) > 0) {
                        processSelected('asignar', 'PATCH', () => ({usuario: Number(usuario)}), '¿Asignar los radicados seleccionados al usuario indicado?');
                    }
                }
                if (action === 'reasignar') {
                    const usuarioNuevo = prompt('Ingrese el ID del nuevo usuario responsable:');
                    if (!usuarioNuevo || Number(usuarioNuevo) <= 0) return;
                    const dependenciaNueva = prompt('Ingrese el ID de la nueva dependencia:');
                    if (!dependenciaNueva || Number(dependenciaNueva) <= 0) return;
                    processSelected('reasignar', 'POST', () => ({usuarioNuevo: Number(usuarioNuevo), dependenciaNueva: Number(dependenciaNueva)}), '¿Confirmar la reasignación de los radicados seleccionados?');
                }
                if (action === 'cerrar') {
                    processSelected('cerrar', 'PATCH', () => ({}), '¿Cerrar los radicados seleccionados? Esta acción cambiará su estado a finalizado.');
                }
            });
        });
    });
})();