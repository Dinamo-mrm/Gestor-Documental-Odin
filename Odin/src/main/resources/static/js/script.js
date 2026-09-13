/* ODIN - Gestión de Trámites */
(function () {
    'use strict';

    const jsonHeaders = {'Content-Type': 'application/json'};

    function selectedIds() {
        return [...document.querySelectorAll('tbody input[type="checkbox"]:checked')]
            .map(el => el.value).filter(Boolean);
    }

    async function patchRadicado(id, endpoint, payload) {
        const response = await fetch(`/api/radicados/${id}/${endpoint}`, {
            method: 'PATCH', headers: jsonHeaders, body: JSON.stringify(payload)
        });
        if (!response.ok) throw new Error(`No se pudo actualizar el radicado ${id}`);
        return response.json();
    }

    async function processSelected(endpoint, payloadFactory, message) {
        const ids = selectedIds();
        if (!ids.length) return alert('Seleccione al menos un radicado.');
        if (!confirm(message)) return;
        try {
            for (const id of ids) await patchRadicado(id, endpoint, payloadFactory());
            alert('Operación realizada correctamente.');
            window.location.reload();
        } catch (error) { alert(error.message); }
    }

    document.addEventListener('DOMContentLoaded', () => {
        const master = document.querySelector('thead input[type="checkbox"]');
        if (master) master.addEventListener('change', () => {
            document.querySelectorAll('tbody input[type="checkbox"]').forEach(cb => cb.checked = master.checked);
        });

        const filterForm = document.querySelector('#formFiltros');
        if (filterForm) filterForm.addEventListener('submit', e => {
            e.preventDefault();
            const params = new URLSearchParams(new FormData(filterForm));
            window.location.href = `/view/tramites?${params.toString()}`;
        });

        const buttons = [...document.querySelectorAll('[data-action]')];
        buttons.forEach(button => button.addEventListener('click', () => {
            const action = button.dataset.action;
            if (action === 'estado') {
                const value = prompt('Ingrese el ID del nuevo estado:');
                if (value) processSelected('estado', () => ({estado: Number(value)}), '¿Cambiar el estado de los radicados seleccionados?');
            }
            if (action === 'asignar') {
                const value = prompt('Ingrese el ID del usuario responsable:');
                if (value) processSelected('asignar', () => ({usuario: Number(value)}), '¿Asignar los radicados seleccionados?');
            }
        }));
    });
})();