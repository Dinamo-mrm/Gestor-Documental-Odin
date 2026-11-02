/**
 * ============================================================================
 * ARCHIVO: guia-tour.js
 * MÓDULO: Guía Interactiva de ODIN (Tour Guiado Paso a Paso con Spotlight)
 * ============================================================================
 * PROPÓSITO PARA SUSTENTACIÓN:
 * Crea un recorrido interactivo tipo "spotlight" sobre los elementos reales de
 * la interfaz de ODIN (menú lateral, campana de notificaciones, botón de modo
 * oscuro, buscador de la guía o tarjetas del dashboard).
 * - Puede lanzarse desde el botón "Hacer recorrido" en /view/guia.
 * - También se activa automáticamente en cualquier pantalla si la URL incluye
 *   el parámetro ?tour=dashboard o ?tour=guia.
 * - Permite navegar con teclado (Flecha Derecha, Flecha Izquierda y tecla Esc).
 * ============================================================================
 */

(function () {
    "use strict";

    var indiceActual = 0;
    var pasosActivos = [];
    var overlayEl = null;
    var focoEl = null;
    var tarjetaEl = null;

    /**
     * Construye la lista de pasos según los elementos reales presentes en la página.
     */
    function obtenerPasosDisponibles() {
        var candidatos = [
            {
                selector: "#main-sidebar",
                titulo: "1. Menú Principal de ODIN",
                texto: "Desde este panel lateral navegas entre Dashboard, Control de Acceso, Dependencias, Radicación, Gestión Documental, Trámites, Consulta, Bitácora, Reportes y esta Guía."
            },
            {
                selector: "#btnNotif",
                titulo: "2. Campana de Notificaciones",
                texto: "Aquí recibes avisos inmediatos de radicados asignados o vencidos sin salir de la pantalla en la que estés trabajando."
            },
            {
                selector: "#btnTheme",
                titulo: "3. Interruptor de Modo Claro / Oscuro SENA",
                texto: "Cambia con un clic entre el tema claro y el modo oscuro institucional SENA (#39a900, #007832, #00304d). Tu elección queda guardada en el navegador."
            },
            {
                selector: "#btnTopbarGuia",
                titulo: "4. Botón Rápido de Ayuda (?)",
                texto: "Este botón '?' en la barra superior está disponible en todas las pantallas para abrir la Guía Interactiva cuando tengas una duda."
            },
            {
                selector: "#guiaBuscadorInput",
                titulo: "5. Buscador en Vivo (Atajo '/')",
                texto: "Presiona la tecla '/' en cualquier momento para buscar palabras clave, permisos, estados o preguntas frecuentes al instante."
            },
            {
                selector: "#guiaRutasContainer",
                titulo: "6. Rutas de Aprendizaje por Perfil",
                texto: "Filtra las lecciones según tu rol operativo: Radicador, Gestor de Trámites, Consulta/Auditor o Administrador."
            },
            {
                selector: "#sec-simuladores",
                titulo: "7. Zona de Simuladores Sandbox (Sin tocar la BD)",
                texto: "Aquí puedes practicar cómo radicar un documento, probar el semáforo de vencimientos y simular permisos 200/403 de forma 100 % segura."
            }
        ];

        // Filtrar solo los elementos que existan y sean visibles en la pantalla actual
        return candidatos.filter(function (p) {
            var el = document.querySelector(p.selector);
            if (!el) return false;
            var rect = el.getBoundingClientRect();
            return rect.width > 0 && rect.height > 0;
        });
    }

    /**
     * Crea los elementos visuales del tour (overlay oscuro, recorte luminoso y tarjeta) si no existen.
     */
    function asegurarElementosDom() {
        if (overlayEl) return;

        // Inyectar estilos mínimos por si el tour se lanza fuera de /view/guia (ej. ?tour=dashboard)
        if (!document.getElementById("odinTourInlineStyles")) {
            var st = document.createElement("style");
            st.id = "odinTourInlineStyles";
            st.textContent = ""
                + ".odin-tour-overlay{position:fixed;inset:0;background:rgba(0,18,32,.65);z-index:9990;transition:opacity .2s}"
                + ".odin-tour-spotlight{position:fixed;z-index:9991;border:3px solid #39a900;border-radius:12px;box-shadow:0 0 0 9999px rgba(0,18,32,.68),0 0 24px rgba(57,169,0,.75);pointer-events:none;transition:all .25s ease}"
                + ".odin-tour-card{position:fixed;z-index:9992;width:340px;max-width:92vw;background:#fff;color:#0f172a;border:2px solid #39a900;border-radius:14px;padding:1rem 1.15rem;box-shadow:0 16px 40px rgba(0,0,0,.45);font-family:sans-serif}"
                + "html.dark .odin-tour-card{background:#002236;color:#f8fafc;border-color:#39a900}"
                + ".odin-tour-card h4{margin:0 0 .4rem;font-size:1rem;font-weight:800;color:#15803d}"
                + "html.dark .odin-tour-card h4{color:#4ade80}"
                + ".odin-tour-card p{margin:0 0 .85rem;font-size:.85rem;line-height:1.45;color:#334155}"
                + "html.dark .odin-tour-card p{color:#e2e8f0}"
                + ".odin-tour-bar{display:flex;align-items:center;justify-content:space-between;gap:.5rem}"
                + ".odin-tour-step{font-size:.75rem;font-weight:700;color:#64748b}"
                + "html.dark .odin-tour-step{color:#94a3b8}"
                + ".odin-tour-btns{display:flex;gap:.35rem}"
                + ".odin-tour-btn{border:1px solid #cbd5e1;background:#f8fafc;color:#1e293b;border-radius:8px;padding:.35rem .65rem;font-size:.78rem;font-weight:700;cursor:pointer}"
                + "html.dark .odin-tour-btn{background:#00304d;border-color:#004d7a;color:#e2e8f0}"
                + ".odin-tour-btn.primario{background:#39a900;border-color:#39a900;color:#fff}";
            document.head.appendChild(st);
        }

        overlayEl = document.createElement("div");
        overlayEl.className = "odin-tour-overlay hidden";
        overlayEl.setAttribute("aria-hidden", "true");

        focoEl = document.createElement("div");
        focoEl.className = "odin-tour-spotlight hidden";

        tarjetaEl = document.createElement("div");
        tarjetaEl.className = "odin-tour-card hidden";
        tarjetaEl.setAttribute("role", "dialog");
        tarjetaEl.setAttribute("aria-modal", "true");
        tarjetaEl.setAttribute("aria-label", "Recorrido guiado de ODIN");

        document.body.appendChild(overlayEl);
        document.body.appendChild(focoEl);
        document.body.appendChild(tarjetaEl);

        overlayEl.addEventListener("click", cerrarTour);
    }

    /**
     * Inicia el recorrido guiado desde el primer paso.
     */
    function iniciarTour() {
        asegurarElementosDom();
        pasosActivos = obtenerPasosDisponibles();
        if (!pasosActivos.length) return;

        indiceActual = 0;
        overlayEl.classList.remove("hidden");
        focoEl.classList.remove("hidden");
        tarjetaEl.classList.remove("hidden");
        mostrarPaso(indiceActual);
    }

    /**
     * Posiciona el recorte luminoso y la tarjeta explicativa sobre el elemento actual.
     */
    function mostrarPaso(idx) {
        var paso = pasosActivos[idx];
        if (!paso) return;

        var objetivo = document.querySelector(paso.selector);
        if (!objetivo) return;

        objetivo.scrollIntoView({ behavior: "smooth", block: "center" });

        setTimeout(function () {
            var rect = objetivo.getBoundingClientRect();
            var pad = 6;

            focoEl.style.top = Math.max(4, rect.top - pad) + "px";
            focoEl.style.left = Math.max(4, rect.left - pad) + "px";
            focoEl.style.width = Math.min(window.innerWidth - 8, rect.width + pad * 2) + "px";
            focoEl.style.height = Math.min(window.innerHeight - 8, rect.height + pad * 2) + "px";

            tarjetaEl.innerHTML = ''
                + '<h4>' + paso.titulo + '</h4>'
                + '<p>' + paso.texto + '</p>'
                + '<div class="odin-tour-bar">'
                + '  <span class="odin-tour-step">Paso ' + (idx + 1) + ' de ' + pasosActivos.length + ' (Esc para salir)</span>'
                + '  <div class="odin-tour-btns">'
                + (idx > 0 ? '<button type="button" class="odin-tour-btn" id="tourBtnPrev">← Anterior</button>' : '')
                + (idx + 1 < pasosActivos.length
                    ? '<button type="button" class="odin-tour-btn primario" id="tourBtnNext">Siguiente →</button>'
                    : '<button type="button" class="odin-tour-btn primario" id="tourBtnFinish">Finalizar ✓</button>')
                + '    <button type="button" class="odin-tour-btn" id="tourBtnClose">✕</button>'
                + '  </div>'
                + '</div>';

            // Posicionar la tarjeta debajo o arriba del elemento resaltado
            var cardTop = rect.bottom + 14;
            if (cardTop + 180 > window.innerHeight) {
                cardTop = Math.max(16, rect.top - 185);
            }
            var cardLeft = Math.min(window.innerWidth - 356, Math.max(12, rect.left));

            tarjetaEl.style.top = cardTop + "px";
            tarjetaEl.style.left = cardLeft + "px";

            var bPrev = document.getElementById("tourBtnPrev");
            var bNext = document.getElementById("tourBtnNext");
            var bFin = document.getElementById("tourBtnFinish");
            var bClose = document.getElementById("tourBtnClose");

            if (bPrev) bPrev.addEventListener("click", function () { indiceActual--; mostrarPaso(indiceActual); });
            if (bNext) bNext.addEventListener("click", function () { indiceActual++; mostrarPaso(indiceActual); });
            if (bFin) bFin.addEventListener("click", cerrarTour);
            if (bClose) bClose.addEventListener("click", cerrarTour);
        }, 220);
    }

    /**
     * Cierra el recorrido guiado.
     */
    function cerrarTour() {
        if (!overlayEl) return;
        overlayEl.classList.add("hidden");
        focoEl.classList.add("hidden");
        tarjetaEl.classList.add("hidden");
    }

    function conectarTour() {
        var btn = document.getElementById("btnIniciarTourGuia");
        if (btn) {
            btn.addEventListener("click", iniciarTour);
        }

        // Navegación por teclado mientras el tour está abierto
        document.addEventListener("keydown", function (e) {
            if (!tarjetaEl || tarjetaEl.classList.contains("hidden")) return;
            if (e.key === "Escape") {
                cerrarTour();
            } else if (e.key === "ArrowRight" && indiceActual + 1 < pasosActivos.length) {
                indiceActual++;
                mostrarPaso(indiceActual);
            } else if (e.key === "ArrowLeft" && indiceActual > 0) {
                indiceActual--;
                mostrarPaso(indiceActual);
            }
        });

        // Lanzamiento automático si la URL trae ?tour=dashboard o ?tour=guia
        try {
            var params = new URLSearchParams(window.location.search);
            if (params.has("tour")) {
                setTimeout(iniciarTour, 400);
            }
        } catch (e) { }
    }

    window.odinIniciarTour = iniciarTour;

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", conectarTour);
    } else {
        conectarTour();
    }
})();
