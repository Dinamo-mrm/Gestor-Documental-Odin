/**
 * ============================================================================
 * ARCHIVO: guia.js
 * MÓDULO: Guía Interactiva de ODIN (Lógica principal, Progreso y Navegación)
 * ============================================================================
 * PROPÓSITO PARA SUSTENTACIÓN:
 * Controla la interfaz principal de la guía sin escribir nada en la base de datos:
 * 1. Lee los permisos reales del usuario desde el atributo data-permisos en el HTML.
 * 2. Gestiona el progreso del usuario en localStorage ('odin-guia-progreso').
 * 3. Renderiza las tarjetas del Hub, las rutas de aprendizaje y las lecciones.
 * 4. Filtra en tiempo real mediante el buscador (con atajo de teclado '/').
 * 5. Controla el Glosario interactivo, las Preguntas Frecuentes (FAQ) y el Scroll-Spy.
 * ============================================================================
 */

(function () {
    "use strict";

    // Clave única en localStorage solicitada en los requerimientos
    var STORAGE_KEY = "odin-guia-progreso";

    // Estado en memoria del módulo de guía
    var estado = {
        permisosUsuario: [],
        esAdmin: false,
        rutaSeleccionada: "todos",
        textoBusqueda: "",
        progreso: {
            completados: [],       // IDs de módulos completados
            ultimoModulo: null,    // Último módulo visitado o completado
            puntajeQuiz: null      // Mejor o último puntaje del quiz
        }
    };

    /**
     * Inicializa toda la guía cuando el DOM termina de cargar.
     */
    function inicializarGuia() {
        var contenedor = document.getElementById("guiaApp");
        if (!contenedor || !window.ODIN_GUIA_DATOS) return;

        // 1. Leer permisos reales enviados por GuiaView.java
        var csv = contenedor.getAttribute("data-permisos") || "";
        estado.permisosUsuario = csv.split(",").map(function (s) { return s.trim(); }).filter(Boolean);
        estado.esAdmin = contenedor.getAttribute("data-admin") === "true"
            || estado.permisosUsuario.indexOf("ROLE_ADMIN") !== -1
            || estado.permisosUsuario.indexOf("ROLE_ROOT") !== -1;

        // 2. Cargar progreso guardado en localStorage
        cargarProgreso();

        // 3. Renderizar componentes dinámicos desde guia-datos.js
        renderizarRutasAprendizaje();
        renderizarHubLecciones();
        renderizarDetalleLecciones();
        renderizarGlosario();
        renderizarFaq();
        actualizarBarraProgreso();

        // 4. Conectar eventos de búsqueda, botones y teclado
        conectarEventosGenerales();
        inicializarScrollSpy();
    }

    /**
     * Lee el objeto de progreso desde localStorage.
     */
    function cargarProgreso() {
        try {
            var guardado = localStorage.getItem(STORAGE_KEY);
            if (guardado) {
                var parsed = JSON.parse(guardado);
                if (parsed && Array.isArray(parsed.completados)) {
                    estado.progreso.completados = parsed.completados;
                }
                if (parsed && parsed.ultimoModulo) {
                    estado.progreso.ultimoModulo = parsed.ultimoModulo;
                }
                if (parsed && typeof parsed.puntajeQuiz === "number") {
                    estado.progreso.puntajeQuiz = parsed.puntajeQuiz;
                }
            }
        } catch (e) {
            // Si localStorage está bloqueado, funciona en memoria sin fallar
        }
    }

    /**
     * Guarda el estado de progreso actual en localStorage.
     */
    function guardarProgreso() {
        try {
            localStorage.setItem(STORAGE_KEY, JSON.stringify(estado.progreso));
        } catch (e) { }
    }

    /**
     * Verifica si el usuario autenticado tiene permiso para usar un módulo real.
     */
    function usuarioPuedeUsarModulo(modulo) {
        if (estado.esAdmin) return true;
        if (!modulo.permisosRequeridos || modulo.permisosRequeridos.length === 0) {
            return true; // Disponible para cualquier autenticado
        }
        for (var i = 0; i < modulo.permisosRequeridos.length; i++) {
            if (estado.permisosUsuario.indexOf(modulo.permisosRequeridos[i]) !== -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Renderiza los botones de las Rutas de Aprendizaje por perfil.
     */
    function renderizarRutasAprendizaje() {
        var wrap = document.getElementById("guiaRutasContainer");
        var desc = document.getElementById("guiaRutaDescripcion");
        if (!wrap) return;

        var rutas = window.ODIN_GUIA_DATOS.rutasAprendizaje;
        var html = "";

        for (var i = 0; i < rutas.length; i++) {
            var r = rutas[i];
            var activa = r.id === estado.rutaSeleccionada;
            html += '<button type="button" class="guia-ruta-btn ' + (activa ? 'activa' : '') + '" '
                + 'data-ruta="' + r.id + '" aria-pressed="' + activa + '">'
                + '<span aria-hidden="true">' + r.icono + '</span> '
                + '<span>' + r.nombre + '</span>'
                + '</button>';
        }
        wrap.innerHTML = html;

        // Mostrar descripción de la ruta activa
        var rutaObj = rutas.find(function (x) { return x.id === estado.rutaSeleccionada; }) || rutas[0];
        if (desc && rutaObj) {
            desc.textContent = rutaObj.descripcion + " (" + rutaObj.modulos.length + " módulos en esta ruta)";
        }

        // Eventos clic en cada ruta
        var botones = wrap.querySelectorAll(".guia-ruta-btn");
        botones.forEach(function (btn) {
            btn.addEventListener("click", function () {
                estado.rutaSeleccionada = btn.getAttribute("data-ruta");
                renderizarRutasAprendizaje();
                renderizarHubLecciones();
                renderizarDetalleLecciones();
            });
        });
    }

    /**
     * Obtiene la lista de módulos filtrados por la ruta seleccionada y el buscador en vivo.
     */
    function obtenerModulosFiltrados() {
        var datos = window.ODIN_GUIA_DATOS;
        var rutaObj = datos.rutasAprendizaje.find(function (r) { return r.id === estado.rutaSeleccionada; })
            || datos.rutasAprendizaje[0];

        var q = estado.textoBusqueda.trim().toLowerCase();

        // Ordenar según el orden de la ruta de aprendizaje seleccionada
        var lista = [];
        rutaObj.modulos.forEach(function (idMod) {
            var encontrado = datos.modulos.find(function (m) { return m.id === idMod; });
            if (encontrado) lista.push(encontrado);
        });

        if (!q) return lista;

        return lista.filter(function (m) {
            var texto = (
                m.titulo + " " + m.resumen + " " + m.paraQueSirve + " "
                + m.permisoTexto + " " + m.pasos.join(" ")
            ).toLowerCase();
            return texto.indexOf(q) !== -1;
        });
    }

    /**
     * Renderiza las tarjetas resumen (Hub) en la parte superior.
     */
    function renderizarHubLecciones() {
        var grid = document.getElementById("guiaHubGrid");
        if (!grid) return;

        var modulos = obtenerModulosFiltrados();
        if (modulos.length === 0) {
            grid.innerHTML = '<div class="guia-empty-box">No se encontraron módulos que coincidan con tu búsqueda. Prueba con otra palabra clave.</div>';
            return;
        }

        var html = "";
        modulos.forEach(function (m) {
            var completado = estado.progreso.completados.indexOf(m.id) !== -1;
            var disponible = usuarioPuedeUsarModulo(m);

            html += '<article class="guia-hub-card ' + (completado ? 'completado' : '') + '">'
                + '<div class="guia-hub-top">'
                + '  <span class="guia-badge-cat">' + m.categoria + '</span>'
                + '  <span class="guia-badge-rol ' + (disponible ? 'disponible' : 'restringido') + '">'
                + (disponible ? '✓ Disponible para tu rol' : '🔒 No disponible para tu rol')
                + '  </span>'
                + '</div>'
                + '<h3 class="guia-hub-title">' + m.titulo + '</h3>'
                + '<p class="guia-hub-desc">' + m.resumen + '</p>'
                + '<div class="guia-hub-footer">'
                + '  <a href="#leccion-' + m.id + '" class="guia-link-ir" data-ir-modulo="' + m.id + '">Ver pasos →</a>'
                + '  <span class="guia-estado-leccion ' + (completado ? 'ok' : '') + '">'
                + (completado ? 'Completado ✓' : 'Pendiente')
                + '  </span>'
                + '</div>'
                + '</article>';
        });

        grid.innerHTML = html;

        // Registrar último módulo visitado al hacer clic en "Ver pasos"
        grid.querySelectorAll("[data-ir-modulo]").forEach(function (enlace) {
            enlace.addEventListener("click", function () {
                estado.progreso.ultimoModulo = enlace.getAttribute("data-ir-modulo");
                guardarProgreso();
                actualizarBarraProgreso();
            });
        });
    }

    /**
     * Renderiza el contenido completo paso a paso de cada módulo.
     */
    function renderizarDetalleLecciones() {
        var contenedor = document.getElementById("guiaLeccionesContainer");
        if (!contenedor) return;

        var modulos = obtenerModulosFiltrados();
        if (modulos.length === 0) {
            contenedor.innerHTML = "";
            return;
        }

        var html = "";
        modulos.forEach(function (m) {
            var completado = estado.progreso.completados.indexOf(m.id) !== -1;
            var disponible = usuarioPuedeUsarModulo(m);

            var pasosHtml = m.pasos.map(function (p, idx) {
                return '<li class="guia-paso-item"><span class="guia-paso-num">' + (idx + 1) + '</span><span>' + resaltarTerminosGlosario(p) + '</span></li>';
            }).join("");

            var erroresHtml = m.erroresComunes.map(function (err) {
                return '<div class="guia-error-item">'
                    + '  <div class="guia-error-q">⚠️ <strong>Problema común:</strong> ' + err.problema + '</div>'
                    + '  <div class="guia-error-a">💡 <strong>Solución:</strong> ' + err.solucion + '</div>'
                    + '</div>';
            }).join("");

            html += '<section id="leccion-' + m.id + '" class="guia-leccion-card ' + (completado ? 'leccion-completada' : '') + '">'
                + '<div class="guia-leccion-header">'
                + '  <div>'
                + '    <div class="guia-leccion-badges">'
                + '      <span class="guia-badge-cat">' + m.categoria + '</span>'
                + '      <code class="guia-badge-ruta">' + m.ruta + '</code>'
                + '      <span class="guia-badge-rol ' + (disponible ? 'disponible' : 'restringido') + '">'
                + (disponible ? '✓ Habilitado para tu rol actual' : '🔒 No disponible para tu rol (modo aprendizaje)')
                + '      </span>'
                + '    </div>'
                + '    <h3 class="guia-leccion-titulo">' + m.titulo + '</h3>'
                + '  </div>'
                + '  <div class="guia-leccion-actions">'
                + '    <button type="button" class="guia-btn-check ' + (completado ? 'activo' : '') + '" data-toggle-modulo="' + m.id + '">'
                + (completado ? '✓ Completado (clic para desmarcar)' : 'Marcar como completado')
                + '    </button>'
                + (disponible ? '<a href="' + m.ruta + '" class="guia-btn-outline">Ir al módulo real ↗</a>' : '')
                + '  </div>'
                + '</div>'
                + '<div class="guia-leccion-grid">'
                + '  <div class="guia-leccion-col-info">'
                + '    <div class="guia-info-bloque">'
                + '      <h4>¿Para qué sirve este módulo?</h4>'
                + '      <p>' + resaltarTerminosGlosario(m.paraQueSirve) + '</p>'
                + '    </div>'
                + '    <div class="guia-info-bloque">'
                + '      <h4>¿Quién puede usarlo? (Permisos en SecurityConfig)</h4>'
                + '      <p>Requiere: <code class="guia-code-perm">' + m.permisoTexto + '</code> (o rol <code>ROLE_ADMIN</code> / <code>ROLE_ROOT</code>).</p>'
                + '    </div>'
                + '    <div class="guia-info-bloque">'
                + '      <h4>Pasos numerados de uso</h4>'
                + '      <ol class="guia-pasos-lista">' + pasosHtml + '</ol>'
                + '    </div>'
                + '    <div class="guia-info-bloque">'
                + '      <h4>Errores comunes y cómo resolverlos</h4>'
                + '      <div class="guia-errores-wrap">' + erroresHtml + '</div>'
                + '    </div>'
                + '  </div>'
                + '  <div class="guia-leccion-col-img">'
                + '    <figure class="guia-captura-wrap">'
                + '      <img src="' + m.imagen + '" alt="Captura real de ' + m.titulo + '" loading="lazy" class="guia-captura-img">'
                + '      <figcaption>Vista de referencia: ' + m.titulo + ' (' + m.ruta + ')</figcaption>'
                + '    </figure>'
                + '  </div>'
                + '</div>'
                + '</section>';
        });

        contenedor.innerHTML = html;

        // Conectar botones "Marcar como completado"
        contenedor.querySelectorAll("[data-toggle-modulo]").forEach(function (btn) {
            btn.addEventListener("click", function () {
                var id = btn.getAttribute("data-toggle-modulo");
                alternarCompletado(id);
            });
        });

        // Conectar tooltips de términos subrayados del glosario
        conectarTooltipsGlosario();
    }

    /**
     * Marca o desmarca un módulo como completado y actualiza el progreso.
     */
    function alternarCompletado(idModulo) {
        var idx = estado.progreso.completados.indexOf(idModulo);
        if (idx === -1) {
            estado.progreso.completados.push(idModulo);
            estado.progreso.ultimoModulo = idModulo;
        } else {
            estado.progreso.completados.splice(idx, 1);
        }
        guardarProgreso();
        actualizarBarraProgreso();
        renderizarHubLecciones();
        renderizarDetalleLecciones();
    }

    /**
     * Actualiza la barra de porcentaje, el certificado visual y el botón "Continuar donde lo dejé".
     */
    function actualizarBarraProgreso() {
        var total = window.ODIN_GUIA_DATOS.modulos.length;
        var hechos = estado.progreso.completados.length;
        var porcentaje = total > 0 ? Math.round((hechos / total) * 100) : 0;

        var barra = document.getElementById("guiaProgressBar");
        var texto = document.getElementById("guiaProgressText");
        var cert = document.getElementById("guiaCertificadoBox");
        var btnContinuar = document.getElementById("btnContinuarGuia");

        if (barra) {
            barra.style.width = porcentaje + "%";
            barra.setAttribute("aria-valuenow", String(porcentaje));
        }
        if (texto) {
            texto.textContent = porcentaje + "% completado (" + hechos + " de " + total + " lecciones)";
        }
        if (cert) {
            if (porcentaje === 100) {
                cert.classList.remove("hidden");
            } else {
                cert.classList.add("hidden");
            }
        }
        if (btnContinuar) {
            // Buscar el último visitado o la primera lección pendiente
            var destino = estado.progreso.ultimoModulo;
            if (!destino) {
                var pendiente = window.ODIN_GUIA_DATOS.modulos.find(function (m) {
                    return estado.progreso.completados.indexOf(m.id) === -1;
                });
                if (pendiente) destino = pendiente.id;
            }
            if (destino) {
                btnContinuar.setAttribute("href", "#leccion-" + destino);
            }
        }
    }

    /**
     * Subraya términos clave del glosario dentro de un texto para mostrar su definición al pasar el mouse.
     */
    function resaltarTerminosGlosario(texto) {
        var glosario = window.ODIN_GUIA_DATOS.glosario;
        var resultado = texto;

        glosario.forEach(function (item) {
            var regex = new RegExp("\\b(" + item.termino + ")\\b", "i");
            resultado = resultado.replace(regex, function (match) {
                return '<span class="guia-term-inline" tabindex="0" data-definicion="'
                    + item.definicion.replace(/"/g, "&quot;") + '">' + match + '</span>';
            });
        });
        return resultado;
    }

    /**
     * Muestra el tooltip flotante para los términos subrayados.
     */
    function conectarTooltipsGlosario() {
        var tooltip = document.getElementById("guiaFloatingTooltip");
        if (!tooltip) return;

        var terminos = document.querySelectorAll(".guia-term-inline");
        terminos.forEach(function (el) {
            function mostrar() {
                var def = el.getAttribute("data-definicion");
                if (!def) return;
                tooltip.textContent = def;
                tooltip.classList.remove("hidden");
                var rect = el.getBoundingClientRect();
                var top = Math.max(12, rect.top - tooltip.offsetHeight - 10);
                var left = Math.min(window.innerWidth - 290, Math.max(12, rect.left));
                tooltip.style.top = top + "px";
                tooltip.style.left = left + "px";
            }
            function ocultar() {
                tooltip.classList.add("hidden");
            }
            el.addEventListener("mouseenter", mostrar);
            el.addEventListener("focus", mostrar);
            el.addEventListener("mouseleave", ocultar);
            el.addEventListener("blur", ocultar);
        });
    }

    /**
     * Renderiza la cuadrícula completa del Glosario Archivístico y Técnico.
     */
    function renderizarGlosario() {
        var grid = document.getElementById("guiaGlosarioGrid");
        if (!grid) return;

        var html = "";
        window.ODIN_GUIA_DATOS.glosario.forEach(function (g) {
            html += '<div class="guia-glosario-card">'
                + '  <dt class="guia-glosario-term">📌 ' + g.termino + '</dt>'
                + '  <dd class="guia-glosario-def">' + g.definicion + '</dd>'
                + '</div>';
        });
        grid.innerHTML = html;
    }

    /**
     * Renderiza el acordeón de Preguntas Frecuentes (FAQ) y Solución de Problemas.
     */
    function renderizarFaq() {
        var wrap = document.getElementById("guiaFaqContainer");
        if (!wrap) return;

        var q = estado.textoBusqueda.trim().toLowerCase();
        var lista = window.ODIN_GUIA_DATOS.faq.filter(function (f) {
            if (!q) return true;
            return (f.pregunta + " " + f.respuesta).toLowerCase().indexOf(q) !== -1;
        });

        if (lista.length === 0) {
            wrap.innerHTML = '<div class="guia-empty-box">No hay preguntas frecuentes que coincidan con el filtro actual.</div>';
            return;
        }

        var html = "";
        lista.forEach(function (f, idx) {
            html += '<div class="guia-faq-item">'
                + '  <button type="button" class="guia-faq-btn" aria-expanded="' + (idx === 0 ? 'true' : 'false') + '">'
                + '    <span>' + f.pregunta + '</span>'
                + '    <span class="guia-faq-arrow" aria-hidden="true">' + (idx === 0 ? '−' : '+') + '</span>'
                + '  </button>'
                + '  <div class="guia-faq-panel ' + (idx === 0 ? '' : 'hidden') + '">'
                + '    <p>' + f.respuesta + '</p>'
                + '  </div>'
                + '</div>';
        });
        wrap.innerHTML = html;

        wrap.querySelectorAll(".guia-faq-btn").forEach(function (btn) {
            btn.addEventListener("click", function () {
                var panel = btn.nextElementSibling;
                var arrow = btn.querySelector(".guia-faq-arrow");
                var abierto = btn.getAttribute("aria-expanded") === "true";
                btn.setAttribute("aria-expanded", abierto ? "false" : "true");
                if (panel) panel.classList.toggle("hidden", abierto);
                if (arrow) arrow.textContent = abierto ? "+" : "−";
            });
        });
    }

    /**
     * Conecta buscador en vivo, atajo de teclado '/', reiniciar progreso y volver arriba.
     */
    function conectarEventosGenerales() {
        var inputBuscar = document.getElementById("guiaBuscadorInput");
        var btnLimpiarBusqueda = document.getElementById("btnLimpiarBusquedaGuia");
        var btnReiniciar = document.getElementById("btnReiniciarProgresoGuia");
        var btnImprimir = document.getElementById("btnImprimirGuia");
        var btnSubir = document.getElementById("btnVolverArribaGuia");

        if (inputBuscar) {
            inputBuscar.addEventListener("input", function () {
                estado.textoBusqueda = inputBuscar.value || "";
                renderizarHubLecciones();
                renderizarDetalleLecciones();
                renderizarFaq();
            });
        }

        if (btnLimpiarBusqueda && inputBuscar) {
            btnLimpiarBusqueda.addEventListener("click", function () {
                inputBuscar.value = "";
                estado.textoBusqueda = "";
                renderizarHubLecciones();
                renderizarDetalleLecciones();
                renderizarFaq();
                inputBuscar.focus();
            });
        }

        // Atajo de teclado '/' para enfocar el buscador rápidamente
        document.addEventListener("keydown", function (e) {
            var tag = (e.target && e.target.tagName) ? e.target.tagName.toLowerCase() : "";
            var enInput = tag === "input" || tag === "textarea" || tag === "select";
            if (e.key === "/" && !enInput && inputBuscar) {
                e.preventDefault();
                inputBuscar.focus();
                inputBuscar.select();
            }
        });

        if (btnReiniciar) {
            btnReiniciar.addEventListener("click", function () {
                estado.progreso = { completados: [], ultimoModulo: null, puntajeQuiz: null };
                try { localStorage.removeItem(STORAGE_KEY); } catch (e) { }
                actualizarBarraProgreso();
                renderizarHubLecciones();
                renderizarDetalleLecciones();
                if (window.odinReiniciarQuizGuia) window.odinReiniciarQuizGuia();
            });
        }

        if (btnImprimir) {
            btnImprimir.addEventListener("click", function () {
                window.print();
            });
        }

        if (btnSubir) {
            window.addEventListener("scroll", function () {
                if (window.scrollY > 380) {
                    btnSubir.classList.remove("hidden");
                } else {
                    btnSubir.classList.add("hidden");
                }
            });
            btnSubir.addEventListener("click", function () {
                window.scrollTo({ top: 0, behavior: "smooth" });
            });
        }
    }

    /**
     * Resalta en la tabla de contenido lateral la sección visible actualmente (Scroll-Spy).
     */
    function inicializarScrollSpy() {
        var links = document.querySelectorAll(".guia-toc-link");
        if (!links.length) return;

        var ids = Array.from(links).map(function (a) {
            return a.getAttribute("href").replace("#", "");
        });

        window.addEventListener("scroll", function () {
            var scrollPos = window.scrollY + 140;
            var actual = ids[0];

            ids.forEach(function (id) {
                var sec = document.getElementById(id);
                if (sec && sec.offsetTop <= scrollPos) {
                    actual = id;
                }
            });

            links.forEach(function (a) {
                var href = a.getAttribute("href").replace("#", "");
                if (href === actual) {
                    a.classList.add("activo");
                } else {
                    a.classList.remove("activo");
                }
            });
        });
    }

    // Exponer función para que el Quiz guarde su puntaje en el mismo objeto de localStorage
    window.odinGuardarPuntajeQuiz = function (puntaje, total) {
        estado.progreso.puntajeQuiz = puntaje;
        guardarProgreso();
        var badgeQuiz = document.getElementById("guiaMejorPuntajeQuiz");
        if (badgeQuiz) {
            badgeQuiz.textContent = "Último puntaje Quiz: " + puntaje + " / " + total;
            badgeQuiz.classList.remove("hidden");
        }
    };

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", inicializarGuia);
    } else {
        inicializarGuia();
    }
})();
