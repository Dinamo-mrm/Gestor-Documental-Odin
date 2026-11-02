/**
 * ============================================================================
 * ARCHIVO: guia-simuladores.js
 * MÓDULO: Guía Interactiva de ODIN (Simuladores Sandbox y Quiz Evaluativo)
 * ============================================================================
 * PROPÓSITO PARA SUSTENTACIÓN:
 * Implementa las simulaciones prácticas 100 % en el navegador (en memoria):
 * 1. Simulador de Radicación (con validaciones didácticas y comprobante simulado).
 * 2. Línea de Tiempo Interactiva del Ciclo de Vida de un Radicado.
 * 3. Simulador de Semáforo de Plazos (cálculo de días restantes en vivo).
 * 4. Matriz de Permisos Interactiva RBAC (simulación de accesos 200 vs 403).
 * 5. Escenarios prácticos "¿Qué haría yo?".
 * 6. Quiz evaluativo con retroalimentación inmediata.
 *
 * REGLA DE SEGURIDAD: Ninguna función realiza peticiones POST/PUT/DELETE ni
 * escribe en la base de datos de Supabase.
 * ============================================================================
 */

(function () {
    "use strict";

    function inicializarSimuladores() {
        if (!window.ODIN_GUIA_DATOS) return;

        inicializarSimuladorRadicacion();
        inicializarCicloVidaInteractivo();
        inicializarSemaforoPlazos();
        inicializarMatrizPermisos();
        inicializarEscenariosPracticos();
        inicializarQuizEvaluativo();
    }

    /* ========================================================================
       1. SIMULADOR DE RADICACIÓN (SANDBOX 100 % LOCAL)
       ======================================================================== */
    function inicializarSimuladorRadicacion() {
        var form = document.getElementById("simRadicacionForm");
        if (!form) return;

        var chkAnonimo = document.getElementById("simAnonimo");
        var inputRemitente = document.getElementById("simRemitente");
        var inputDoc = document.getElementById("simDocumento");
        var inputCorreo = document.getElementById("simCorreo");
        var btnEjemplo = document.getElementById("btnSimCargarEjemplo");
        var btnLimpiar = document.getElementById("btnSimLimpiar");
        var cajaErrores = document.getElementById("simValidacionBox");
        var cajaComprobante = document.getElementById("simComprobanteBox");

        // Alternar campos de remitente cuando se marca "Radicación anónima"
        if (chkAnonimo) {
            chkAnonimo.addEventListener("change", function () {
                var esAnon = chkAnonimo.checked;
                if (inputRemitente) {
                    inputRemitente.value = esAnon ? "Anónimo" : "";
                    inputRemitente.disabled = esAnon;
                }
                if (inputDoc) {
                    inputDoc.value = esAnon ? "No aplica (Anónimo)" : "";
                    inputDoc.disabled = esAnon;
                }
                if (inputCorreo) {
                    inputCorreo.disabled = false;
                }
            });
        }

        // Botón "Cargar datos de ejemplo"
        if (btnEjemplo) {
            btnEjemplo.addEventListener("click", function () {
                if (chkAnonimo) chkAnonimo.checked = false;
                if (inputRemitente) { inputRemitente.disabled = false; inputRemitente.value = "Aprendiz Carlos Andrés Gómez"; }
                if (inputDoc) { inputDoc.disabled = false; inputDoc.value = "1005894321"; }
                if (inputCorreo) { inputCorreo.disabled = false; inputCorreo.value = "carlos.gomez@soy.sena.edu.co"; }

                setValor("simTipoRad", "ENTRADA");
                setValor("simCanal", "CORREO");
                setValor("simTramite", "DERECHO DE PETICIÓN (15 días)");
                setValor("simDependencia", "Coordinación Académica");
                setValor("simResponsable", "Gestor de Trámites");
                setValor("simSerie", "1.1012-27 · DERECHOS DE PETICIÓN");
                setValor("simSubserie", "1.1012-27-01 · Peticiones Generales");
                setValor("simPrioridad", "ALTA");
                setValor("simAsunto", "Solicitud de certificado de etapa productiva y paz y salvo académico");
                setValor("simFolios", "3");

                if (cajaErrores) cajaErrores.classList.add("hidden");
            });
        }

        // Botón "Limpiar simulador"
        if (btnLimpiar) {
            btnLimpiar.addEventListener("click", function () {
                form.reset();
                if (inputRemitente) inputRemitente.disabled = false;
                if (inputDoc) inputDoc.disabled = false;
                if (cajaErrores) cajaErrores.classList.add("hidden");
                if (cajaComprobante) cajaComprobante.classList.add("hidden");
            });
        }

        // Evento submit simulado (sin enviar nada al servidor)
        form.addEventListener("submit", function (e) {
            e.preventDefault();

            var tipo = getValor("simTipoRad");
            var canal = getValor("simCanal");
            var tramite = getValor("simTramite");
            var anonimo = chkAnonimo && chkAnonimo.checked;
            var remitente = getValor("simRemitente").trim();
            var dep = getValor("simDependencia");
            var resp = getValor("simResponsable");
            var serie = getValor("simSerie");
            var subserie = getValor("simSubserie");
            var prioridad = getValor("simPrioridad");
            var asunto = getValor("simAsunto").trim();
            var folios = getValor("simFolios") || "1";

            var erroresDidacticos = [];

            if (!anonimo && !remitente) {
                erroresDidacticos.push("<strong>Remitente obligatorio:</strong> Debes ingresar el nombre/razón social del remitente o marcar la casilla 'Radicación anónima'.");
            }
            if (!asunto || asunto.length < 5) {
                erroresDidacticos.push("<strong>Asunto obligatorio:</strong> Escribe una descripción clara del documento (mínimo 5 caracteres) para identificarlo en las bandejas.");
            }
            if (subserie && !serie) {
                erroresDidacticos.push("<strong>Clasificación CCD/TRD:</strong> No puedes seleccionar una Subserie sin elegir primero su Serie documental padre.");
            }

            if (erroresDidacticos.length > 0) {
                if (cajaErrores) {
                    cajaErrores.innerHTML = '<div class="guia-sim-error-title">⚠️ Validación didáctica antes de radicar:</div>'
                        + '<ul>' + erroresDidacticos.map(function (x) { return '<li>' + x + '</li>'; }).join("") + '</ul>';
                    cajaErrores.classList.remove("hidden");
                }
                if (cajaComprobante) cajaComprobante.classList.add("hidden");
                return;
            }

            if (cajaErrores) cajaErrores.classList.add("hidden");

            // Generar consecutivo ficticio según el tipo seleccionado
            var prefijo = tipo === "SALIDA" ? "S" : (tipo === "INTERNA" ? "I" : "E");
            var numAleatorio = String(Math.floor(100 + Math.random() * 899));
            var consecutivoSimulado = prefijo + "-2026-SIM-" + numAleatorio;

            // Calcular fecha estimada de vencimiento (15 días por defecto)
            var hoy = new Date();
            var diasPlazo = tramite.indexOf("10") !== -1 ? 10 : (tramite.indexOf("5") !== -1 ? 5 : 15);
            var venc = new Date(hoy.getTime() + diasPlazo * 86400000);

            if (cajaComprobante) {
                cajaComprobante.innerHTML = ''
                    + '<div class="guia-comprobante-sello">SIMULACIÓN EDUCATIVA — NO SE GUARDÓ NADA EN LA BASE DE DATOS</div>'
                    + '<div class="guia-comprobante-header">'
                    + '  <div>'
                    + '    <div class="guia-comprobante-sub">ORGANIZACIÓN DOCUMENTAL INSTITUCIONAL · SENA</div>'
                    + '    <h4 class="guia-comprobante-num">Radicado N.º ' + consecutivoSimulado + '</h4>'
                    + '    <span class="guia-badge-rol disponible">COPIA CONTROLADA (PRÁCTICA)</span>'
                    + '  </div>'
                    + '  <div class="guia-qr-simulado" aria-label="Código QR simulado">'
                    + '    <div class="qr-grid"><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span></div>'
                    + '    <small>QR VERIFICACIÓN</small>'
                    + '  </div>'
                    + '</div>'
                    + '<div class="guia-comprobante-datos">'
                    + '  <div><strong>Fecha radicación:</strong> ' + formatearFechaIso(hoy) + '</div>'
                    + '  <div><strong>Vencimiento estimado:</strong> ' + formatearFechaIso(venc) + ' (' + diasPlazo + ' días)</div>'
                    + '  <div><strong>Tipo / Canal:</strong> ' + tipo + ' · ' + canal + '</div>'
                    + '  <div><strong>Trámite:</strong> ' + tramite + '</div>'
                    + '  <div><strong>Remitente:</strong> ' + (anonimo ? 'Anónimo' : escaparHtml(remitente)) + '</div>'
                    + '  <div><strong>Dependencia / Responsable:</strong> ' + dep + ' → ' + resp + '</div>'
                    + '  <div><strong>Clasificación CCD:</strong> ' + (serie || 'Sin serie') + ' / ' + (subserie || 'Sin subserie') + '</div>'
                    + '  <div><strong>Prioridad / Folios:</strong> ' + prioridad + ' · ' + escaparHtml(folios) + ' folio(s)</div>'
                    + '  <div class="col-span-full"><strong>Asunto:</strong> ' + escaparHtml(asunto) + '</div>'
                    + '</div>';
                cajaComprobante.classList.remove("hidden");
            }
        });
    }

    /* ========================================================================
       2. LÍNEA DE TIEMPO INTERACTIVA DEL CICLO DE VIDA DE UN RADICADO
       ======================================================================== */
    function inicializarCicloVidaInteractivo() {
        var nav = document.getElementById("guiaCicloVidaStepNav");
        var detalle = document.getElementById("guiaCicloVidaDetalle");
        if (!nav || !detalle) return;

        var hitos = window.ODIN_GUIA_DATOS.hitosCicloVida;
        var pasoActivo = 1;

        function renderizar() {
            nav.innerHTML = hitos.map(function (h) {
                var activo = h.paso === pasoActivo;
                return '<button type="button" class="guia-hito-btn ' + (activo ? 'activo' : '') + '" data-paso="' + h.paso + '">'
                    + '<span class="guia-hito-icono">' + h.icono + '</span>'
                    + '<span class="guia-hito-nombre">' + h.titulo + '</span>'
                    + '</button>';
            }).join("");

            var actual = hitos.find(function (x) { return x.paso === pasoActivo; }) || hitos[0];
            detalle.innerHTML = ''
                + '<div class="guia-hito-card">'
                + '  <div class="guia-hito-head">'
                + '    <span class="guia-hito-big-icon">' + actual.icono + '</span>'
                + '    <div>'
                + '      <h4>' + actual.titulo + '</h4>'
                + '      <span class="guia-badge-cat">Estado en BD: ' + actual.estadoBd + '</span>'
                + '    </div>'
                + '  </div>'
                + '  <p class="guia-hito-desc">' + actual.descripcion + '</p>'
                + '  <div class="guia-hito-meta-grid">'
                + '    <div><strong>🖥️ Pantalla utilizada:</strong> <span>' + actual.pantalla + '</span></div>'
                + '    <div><strong>🔑 Permiso requerido:</strong> <code>' + actual.permiso + '</code></div>'
                + '    <div class="col-span-full"><strong>🕒 Registro en Línea Temporal y Bitácora:</strong> <span>' + actual.bitacora + '</span></div>'
                + '  </div>'
                + '</div>';

            nav.querySelectorAll(".guia-hito-btn").forEach(function (btn) {
                btn.addEventListener("click", function () {
                    pasoActivo = Number(btn.getAttribute("data-paso"));
                    renderizar();
                });
            });
        }

        renderizar();
    }

    /* ========================================================================
       3. SIMULADOR DE SEMÁFORO DE PLAZOS (DÍAS RESTANTES)
       ======================================================================== */
    function inicializarSemaforoPlazos() {
        var inputFecha = document.getElementById("simFechaVencimiento");
        var selectEstado = document.getElementById("simEstadoPlazo");
        var resultadoBox = document.getElementById("simSemaforoResultado");
        if (!inputFecha || !selectEstado || !resultadoBox) return;

        // Fecha inicial por defecto: dentro de 2 días (próximo a vencer)
        var hoy = new Date();
        hoy.setHours(0, 0, 0, 0);
        var inicial = new Date(hoy.getTime() + 2 * 86400000);
        inputFecha.value = formatearFechaIso(inicial);

        function calcularSemaforo() {
            var estadoVal = selectEstado.value;
            var partes = (inputFecha.value || "").split("-");
            if (partes.length !== 3) return;

            var fechaVenc = new Date(Number(partes[0]), Number(partes[1]) - 1, Number(partes[2]));
            fechaVenc.setHours(0, 0, 0, 0);

            var diffDias = Math.round((fechaVenc.getTime() - hoy.getTime()) / 86400000);
            var esCerrado = estadoVal === "finalizado" || estadoVal === "archivado"
                || estadoVal === "rechazado" || estadoVal === "devuelto";

            var claseColor = "ok";
            var etiqueta = "";
            var tarjetaConsulta = "";
            var explicacion = "";

            if (esCerrado) {
                claseColor = "cerrado";
                etiqueta = "— (Plazo detenido por cierre)";
                tarjetaConsulta = estadoVal === "finalizado" ? "Finalizados y Total" : "Total";
                explicacion = "Como el radicado está en estado '" + estadoVal + "', ConsultaView detiene el cálculo (diasRestantes = null) para que no cuente como vencido.";
            } else if (diffDias < 0) {
                claseColor = "vencido";
                etiqueta = "🔴 Vencido hace " + Math.abs(diffDias) + " día(s)";
                tarjetaConsulta = "Vencidos, Pendientes y Total";
                explicacion = "La fecha límite ya pasó (" + diffDias + " días) y el radicado sigue activo. Requiere atención prioritaria.";
            } else if (diffDias <= 3) {
                claseColor = "proximo";
                etiqueta = "🟡 Próximo a vencer (" + diffDias + " día(s) restantes)";
                tarjetaConsulta = "Próximos a vencer (≤3 días), Pendientes y Total";
                explicacion = "Faltan entre 0 y 3 días para el vencimiento. Aparece en la alerta preventiva de 'Próximos a vencer (≤3 días)'.";
            } else {
                claseColor = "ok";
                etiqueta = "🟢 En plazo (" + diffDias + " días restantes)";
                tarjetaConsulta = "Pendientes y Total";
                explicacion = "Faltan más de 3 días para la fecha de vencimiento. El trámite se encuentra dentro del término normal.";
            }

            resultadoBox.innerHTML = ''
                + '<div class="guia-semaforo-card ' + claseColor + '">'
                + '  <div class="guia-semaforo-indicador">' + etiqueta + '</div>'
                + '  <p class="guia-semaforo-exp">' + explicacion + '</p>'
                + '  <div class="guia-semaforo-meta">📌 <strong>Tarjetas donde aparece en Consulta y Seguimiento:</strong> ' + tarjetaConsulta + '</div>'
                + '</div>';
        }

        inputFecha.addEventListener("change", calcularSemaforo);
        selectEstado.addEventListener("change", calcularSemaforo);

        // Botones rápidos de prueba de fechas
        document.querySelectorAll("[data-sim-dias]").forEach(function (btn) {
            btn.addEventListener("click", function () {
                var offset = Number(btn.getAttribute("data-sim-dias"));
                var nueva = new Date(hoy.getTime() + offset * 86400000);
                inputFecha.value = formatearFechaIso(nueva);
                if (btn.getAttribute("data-sim-estado")) {
                    selectEstado.value = btn.getAttribute("data-sim-estado");
                } else {
                    selectEstado.value = "en tramite";
                }
                calcularSemaforo();
            });
        });

        calcularSemaforo();
    }

    /* ========================================================================
       4. MATRIZ DE PERMISOS INTERACTIVA (SIMULADOR RBAC)
       ======================================================================== */
    function inicializarMatrizPermisos() {
        var checkWrap = document.getElementById("simMatrizPermisosChecks");
        var pantallasWrap = document.getElementById("simMatrizPantallasResult");
        if (!checkWrap || !pantallasWrap) return;

        var datos = window.ODIN_GUIA_DATOS;
        var seleccionados = datos.rolesPredefinidos.recepcion.slice();

        function renderizarChecks() {
            checkWrap.innerHTML = datos.permisosDisponibles.map(function (p) {
                var marcado = seleccionados.indexOf(p.codigo) !== -1;
                return '<label class="guia-perm-check ' + (marcado ? 'activo' : '') + '">'
                    + '<input type="checkbox" value="' + p.codigo + '" ' + (marcado ? 'checked' : '') + '>'
                    + '<div>'
                    + '  <strong>' + p.nombre + '</strong>'
                    + '  <code>' + p.codigo + '</code>'
                    + '  <small>' + p.descripcion + '</small>'
                    + '</div>'
                    + '</label>';
            }).join("");

            checkWrap.querySelectorAll("input[type='checkbox']").forEach(function (chk) {
                chk.addEventListener("change", function () {
                    var cod = chk.value;
                    if (chk.checked) {
                        if (seleccionados.indexOf(cod) === -1) seleccionados.push(cod);
                    } else {
                        seleccionados = seleccionados.filter(function (x) { return x !== cod; });
                    }
                    renderizarChecks();
                    renderizarPantallas();
                });
            });
        }

        function renderizarPantallas() {
            pantallasWrap.innerHTML = datos.pantallasSeguridad.map(function (pant) {
                var permitido = pant.permisos.length === 0 || pant.permisos.some(function (req) {
                    return seleccionados.indexOf(req) !== -1;
                });
                return '<div class="guia-pantalla-row ' + (permitido ? 'ok' : 'denegado') + '">'
                    + '  <span class="guia-pantalla-nom">' + pant.nombre + '</span>'
                    + '  <span class="guia-pantalla-status">'
                    + (permitido ? '✓ 200 Acceso Permitido' : '⛔ 403 Acceso Denegado')
                    + '  </span>'
                    + '</div>';
            }).join("");
        }

        // Botones para cargar un rol predefinido con un clic
        document.querySelectorAll("[data-rol-preset]").forEach(function (btn) {
            btn.addEventListener("click", function () {
                var preset = btn.getAttribute("data-rol-preset");
                if (preset === "ninguno") {
                    seleccionados = [];
                } else if (datos.rolesPredefinidos[preset]) {
                    seleccionados = datos.rolesPredefinidos[preset].slice();
                }
                renderizarChecks();
                renderizarPantallas();
            });
        });

        renderizarChecks();
        renderizarPantallas();
    }

    /* ========================================================================
       5. ESCENARIOS PRÁCTICOS "¿QUÉ HARÍA YO?"
       ======================================================================== */
    function inicializarEscenariosPracticos() {
        var wrap = document.getElementById("guiaEscenariosContainer");
        if (!wrap) return;

        var escenarios = window.ODIN_GUIA_DATOS.escenarios;
        var html = "";

        escenarios.forEach(function (esc, idx) {
            var opcionesHtml = esc.opciones.map(function (op, opIdx) {
                return '<button type="button" class="guia-escenario-op" '
                    + 'data-esc="' + idx + '" data-op="' + opIdx + '">'
                    + op.texto
                    + '</button>';
            }).join("");

            html += '<div class="guia-escenario-card" id="esc-card-' + idx + '">'
                + '  <h4>' + esc.titulo + '</h4>'
                + '  <p class="guia-escenario-ctx">' + esc.contexto + '</p>'
                + '  <div class="guia-escenario-opciones">' + opcionesHtml + '</div>'
                + '  <div class="guia-escenario-feedback hidden" id="esc-fb-' + idx + '"></div>'
                + '</div>';
        });

        wrap.innerHTML = html;

        wrap.querySelectorAll(".guia-escenario-op").forEach(function (btn) {
            btn.addEventListener("click", function () {
                var eIdx = Number(btn.getAttribute("data-esc"));
                var oIdx = Number(btn.getAttribute("data-op"));
                var esc = escenarios[eIdx];
                var op = esc.opciones[oIdx];
                var fb = document.getElementById("esc-fb-" + eIdx);
                var card = document.getElementById("esc-card-" + eIdx);

                if (card) {
                    card.querySelectorAll(".guia-escenario-op").forEach(function (b) {
                        b.classList.remove("seleccionada-ok", "seleccionada-err");
                    });
                }
                btn.classList.add(op.correcta ? "seleccionada-ok" : "seleccionada-err");

                if (fb) {
                    fb.className = "guia-escenario-feedback " + (op.correcta ? "ok" : "err");
                    fb.innerHTML = (op.correcta ? "✅ " : "❌ ") + op.explicacion;
                }
            });
        });
    }

    /* ========================================================================
       6. QUIZ EVALUATIVO CON RETROALIMENTACIÓN Y PUNTAJE
       ======================================================================== */
    function inicializarQuizEvaluativo() {
        var wrap = document.getElementById("guiaQuizContainer");
        var btnCalificar = document.getElementById("btnCalificarQuizGuia");
        var btnReiniciar = document.getElementById("btnReiniciarQuizSolo");
        var resultadoQuiz = document.getElementById("guiaQuizResultadoBox");
        if (!wrap) return;

        var preguntas = window.ODIN_GUIA_DATOS.preguntasQuiz;

        function pintarPreguntas() {
            var html = "";
            preguntas.forEach(function (q, qIdx) {
                var opcionesHtml = q.opciones.map(function (textoOp, opIdx) {
                    return '<label class="guia-quiz-opcion">'
                        + '<input type="radio" name="quiz_p_' + qIdx + '" value="' + opIdx + '">'
                        + '<span>' + textoOp + '</span>'
                        + '</label>';
                }).join("");

                html += '<div class="guia-quiz-card" id="quiz-q-' + qIdx + '">'
                    + '  <p class="guia-quiz-pregunta">' + q.pregunta + '</p>'
                    + '  <div class="guia-quiz-opciones">' + opcionesHtml + '</div>'
                    + '  <div class="guia-quiz-exp hidden" id="quiz-exp-' + qIdx + '"></div>'
                    + '</div>';
            });
            wrap.innerHTML = html;
            if (resultadoQuiz) resultadoQuiz.classList.add("hidden");
        }

        if (btnCalificar) {
            btnCalificar.addEventListener("click", function () {
                var aciertos = 0;
                preguntas.forEach(function (q, qIdx) {
                    var seleccionado = wrap.querySelector('input[name="quiz_p_' + qIdx + '"]:checked');
                    var expBox = document.getElementById("quiz-exp-" + qIdx);
                    var valor = seleccionado ? Number(seleccionado.value) : -1;
                    var esCorrecta = valor === q.correcta;

                    if (esCorrecta) aciertos++;

                    if (expBox) {
                        expBox.className = "guia-quiz-exp " + (esCorrecta ? "ok" : "err");
                        expBox.innerHTML = (esCorrecta ? "✅ ¡Correcto! " : "❌ Respuesta correcta: \"" + q.opciones[q.correcta] + "\". ")
                            + q.explicacion;
                    }
                });

                if (resultadoQuiz) {
                    var porcentaje = Math.round((aciertos / preguntas.length) * 100);
                    resultadoQuiz.className = "guia-quiz-resumen " + (porcentaje >= 70 ? "aprobado" : "repasar");
                    resultadoQuiz.innerHTML = '<h4>Resultado de tu evaluación: ' + aciertos + ' de ' + preguntas.length + ' (' + porcentaje + ' %)</h4>'
                        + '<p>' + (porcentaje >= 70
                            ? '🎉 ¡Excelente dominio del sistema ODIN! Tu puntaje quedó guardado en este navegador.'
                            : '📘 Te recomendamos repasar las lecciones marcadas arriba y volver a intentarlo.') + '</p>';
                }

                if (window.odinGuardarPuntajeQuiz) {
                    window.odinGuardarPuntajeQuiz(aciertos, preguntas.length);
                }
            });
        }

        if (btnReiniciar) {
            btnReiniciar.addEventListener("click", pintarPreguntas);
        }

        window.odinReiniciarQuizGuia = pintarPreguntas;
        pintarPreguntas();
    }

    /* Utilidades simples */
    function getValor(id) {
        var el = document.getElementById(id);
        return el ? (el.value || "") : "";
    }
    function setValor(id, val) {
        var el = document.getElementById(id);
        if (el) el.value = val;
    }
    function formatearFechaIso(d) {
        var y = d.getFullYear();
        var m = String(d.getMonth() + 1).padStart(2, "0");
        var day = String(d.getDate()).padStart(2, "0");
        return y + "-" + m + "-" + day;
    }
    function escaparHtml(str) {
        return String(str)
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;");
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", inicializarSimuladores);
    } else {
        inicializarSimuladores();
    }
})();
