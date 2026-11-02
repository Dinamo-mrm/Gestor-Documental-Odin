/**
 * ============================================================================
 * ARCHIVO: guia-datos.js
 * MÓDULO: Guía Interactiva de ODIN (Organización Documental Institucional - SENA)
 * ============================================================================
 * PROPÓSITO PARA SUSTENTACIÓN:
 * Este archivo almacena ÚNICAMENTE los datos y textos de la guía como objetos
 * y arreglos de JavaScript (patrón de separación entre Datos y Lógica).
 * Así cualquier aprendiz o desarrollador puede actualizar textos, pasos,
 * preguntas del quiz o definiciones del glosario sin tocar la programación.
 * ============================================================================
 */

window.ODIN_GUIA_DATOS = {

    /* ========================================================================
       1. RUTAS DE APRENDIZAJE POR PERFIL (ROL)
       Permite filtrar y ordenar las lecciones según el cargo del usuario.
       ======================================================================== */
    rutasAprendizaje: [
        {
            id: "todos",
            nombre: "Todos los módulos",
            icono: "📚",
            descripcion: "Recorrido completo por todas las pantallas y herramientas del sistema ODIN.",
            modulos: [
                "acceso-seguridad", "dashboard", "radicacion", "consulta",
                "tramites", "reasignaciones", "ccd-documentos", "dependencias",
                "usuarios-roles", "bitacora", "reportes", "notificaciones-tema"
            ]
        },
        {
            id: "radicador",
            nombre: "Funcionario / Radicador",
            icono: "📝",
            descripcion: "Ideal para ventanilla única y recepción: ingreso al sistema, radicación de documentos, anexos, comprobante PDF y consulta.",
            modulos: ["acceso-seguridad", "dashboard", "radicacion", "consulta", "ccd-documentos", "notificaciones-tema"]
        },
        {
            id: "gestor",
            nombre: "Responsable de Trámites",
            icono: "⚙️",
            descripcion: "Enfocado en gestionar la bandeja operativa, controlar semáforos de vencimiento, trasladar radicados y cerrar trámites.",
            modulos: ["acceso-seguridad", "dashboard", "tramites", "reasignaciones", "consulta", "ccd-documentos", "notificaciones-tema"]
        },
        {
            id: "consulta",
            nombre: "Consulta / Auditor",
            icono: "🔍",
            descripcion: "Para seguimiento de radicados, línea de tiempo, bitácora de auditoría y exportación de reportes.",
            modulos: ["acceso-seguridad", "dashboard", "consulta", "bitacora", "reportes", "notificaciones-tema"]
        },
        {
            id: "admin",
            nombre: "Administrador",
            icono: "🛡️",
            descripcion: "Configuración integral: usuarios, matriz de roles y permisos, dependencias, series/subseries (CCD/TRD), bitácora y reportes.",
            modulos: [
                "acceso-seguridad", "usuarios-roles", "dependencias", "ccd-documentos",
                "radicacion", "tramites", "reasignaciones", "consulta", "bitacora", "reportes"
            ]
        }
    ],

    /* ========================================================================
       2. LECCIONES PASO A PASO POR MÓDULO (VERIFICADAS CONTRA EL CÓDIGO REAL)
       ======================================================================== */
    modulos: [
        {
            id: "acceso-seguridad",
            titulo: "1. Inicio de Sesión y Seguridad",
            categoria: "Acceso",
            ruta: "/login",
            permisosRequeridos: [], // Público / Autenticado
            permisoTexto: "Acceso público (/login) · Cualquier usuario registrado",
            imagen: "/img/guia/odin_login.png",
            resumen: "Ingreso flexible por correo, documento o nombre, recuperación de clave en 3 pasos y políticas de bloqueo.",
            paraQueSirve: "Autentica la identidad del funcionario antes de entrar a ODIN. Incluye protección contra fuerza bruta (bloqueo temporal tras 5 intentos fallidos durante 15 minutos) y control de sesión única concurrente por usuario.",
            pasos: [
                "Abre la pantalla de ingreso en /login.",
                "En el primer campo puedes escribir tu correo institucional, tu número de identificación o tu nombre de usuario (búsqueda flexible).",
                "Ingresa tu contraseña y presiona el botón 'Iniciar sesión'. Si las credenciales son válidas, el sistema te redirige a /view/dashboard.",
                "Si olvidaste tu clave, haz clic en '¿Olvidaste tu contraseña?' (/recuperar-password) para seguir los 3 pasos: (1) solicitar código al correo, (2) verificar código (vigencia de 30 min) y (3) definir nueva contraseña.",
                "Para salir de forma segura, usa el botón 'Cerrar sesión' en la barra superior, el cual invalida la sesión y limpia la cookie JSESSIONID."
            ],
            erroresComunes: [
                {
                    problema: "Cuenta bloqueada temporalmente al intentar ingresar.",
                    solucion: "Por seguridad, tras 5 intentos fallidos consecutivos la cuenta se bloquea por 15 minutos. Espera el tiempo reglamentario o solicita apoyo al Administrador."
                },
                {
                    problema: "Mi sesión se cerró sola al abrir ODIN en otro computador.",
                    solucion: "ODIN permite máximo 1 sesión activa por usuario (maximumSessions(1)). Iniciar sesión en un nuevo navegador cierra la sesión anterior."
                }
            ]
        },
        {
            id: "dashboard",
            titulo: "2. Dashboard e Indicadores (KPIs)",
            categoria: "Operación",
            ruta: "/view/dashboard",
            permisosRequeridos: [], // Cualquier autenticado
            permisoTexto: "Cualquier usuario autenticado",
            imagen: "/img/guia/odin_dashboard.png",
            resumen: "Tablero principal con tarjetas de indicadores operativos, gráficos de distribución y últimos radicados.",
            paraQueSirve: "Ofrece un panorama inmediato del estado documental: total de radicados, pendientes, en trámite, finalizados, rechazados, vencidos, próximos a vencer, sin asignar, usuarios activos y documentos cargados.",
            pasos: [
                "Al iniciar sesión llegarás automáticamente a /view/dashboard.",
                "Revisa las tarjetas superiores (KPIs) para identificar cuellos de botella, especialmente los radicados 'Vencidos' y 'Próximos a vencer'.",
                "Haz clic sobre cualquiera de las tarjetas del Dashboard para abrir directamente la bandeja filtrada según ese indicador.",
                "Consulta los gráficos de barras y dona para analizar la distribución por estados y la tabla inferior de 'Últimos radicados'."
            ],
            erroresComunes: [
                {
                    problema: "Al hacer clic en una tarjeta del Dashboard me aparece la pantalla 403.",
                    solucion: "Las tarjetas enlazan a Gestión de Trámites (/view/tramites). Si tu rol es únicamente de Consulta, usa el módulo 'Consulta y Seguimiento' (/view/consulta)."
                }
            ]
        },
        {
            id: "radicacion",
            titulo: "3. Radicación de Documentos",
            categoria: "Operación",
            ruta: "/view/radicados",
            permisosRequeridos: ["ver_radicados", "crear_radicado", "editar_radicado", "finalizar_radicado"],
            permisoTexto: "ver_radicados, crear_radicado, editar_radicado, finalizar_radicado",
            imagen: "/img/guia/odin_radicacion.png",
            resumen: "Registro oficial de comunicaciones de entrada (E-), salida (S-) e internas con anexos y comprobante PDF con QR.",
            paraQueSirve: "Permite radicar documentos oficiales en 4 bloques estructurados, asignando número consecutivo automático por vigencia, calculando la fecha de vencimiento y generando un comprobante PDF con sello 'COPIA CONTROLADA' y código QR.",
            pasos: [
                "Sección 1 (Tipo y canal): Selecciona el Tipo de radicación (ENTRADA 'E-', SALIDA 'S-' o INTERNA), el Canal de recepción (Físico, Correo, Web, Otro), Tipo de documento y el Trámite.",
                "Sección 2 (Remitente): Si es una petición anónima, marca la casilla 'Radicación anónima'. De lo contrario, completa nombre o razón social, identificación, correo, teléfono, ciudad y forma de respuesta preferida.",
                "Sección 3 (Asignación y clasificación CCD/TRD): Elige la Dependencia destino, el Usuario responsable, la Serie y Subserie documental, y la Prioridad (Baja, Media, Alta, Urgente).",
                "Sección 4 (Contenido y anexos): Escribe el Asunto, observaciones, número de folios, soporte, confidencialidad y adjunta los soportes (máx. 20 MB por archivo y 50 MB por solicitud).",
                "Haz clic en 'Guardar radicado' y descarga o imprime el Comprobante PDF oficial desde /api/radicados/{id}/comprobante."
            ],
            erroresComunes: [
                {
                    problema: "No me permite guardar cuando selecciono una Subserie.",
                    solucion: "Toda Subserie documental requiere que selecciones primero la Serie padre a la que pertenece dentro del Cuadro de Clasificación Documental (CCD)."
                },
                {
                    problema: "Error al subir un archivo adjunto.",
                    solucion: "Verifica que el archivo no supere 20 MB individuales (ni 50 MB en total) y que sea un formato permitido (PDF, DOCX, JPG, PNG)."
                }
            ]
        },
        {
            id: "consulta",
            titulo: "4. Consulta y Seguimiento",
            categoria: "Seguimiento",
            ruta: "/view/consulta",
            permisosRequeridos: ["ver_radicados"],
            permisoTexto: "ver_radicados",
            imagen: "/img/guia/odin_consulta.png",
            resumen: "Tarjetas interactivas por estado de plazo, filtros avanzados por fecha y vista detallada con línea de tiempo.",
            paraQueSirve: "Permite a cada usuario consultar los radicados asignados a su dependencia y a su usuario, filtrando rápidamente mediante 6 tarjetas-botón (Resultados, Pendientes, Vencidos, Próximos a vencer ≤3 días, Finalizados y Total) y abriendo la trazabilidad completa.",
            pasos: [
                "Ingresa a /view/consulta desde el menú lateral.",
                "Haz clic en cualquiera de las 6 tarjetas superiores ('Resultados', 'Pendientes', 'Vencidos', 'Próximos a vencer ≤3 días', 'Finalizados' o 'Total') para filtrar inmediatamente la tabla inferior; la tarjeta 'Resultados' adaptará su contador a la pestaña activa.",
                "Utiliza la barra de filtros (texto, estado, dependencia, trámite, fecha desde y fecha hasta) y presiona 'Buscar' para consultas específicas.",
                "Presiona el botón 'Ver' en cualquier fila para abrir el Detalle del Radicado (/view/radicados/detalle/{id}), donde verás la Línea Temporal Unificada, agregarás observaciones, solicitarás firmas o descargarás el comprobante PDF."
            ],
            erroresComunes: [
                {
                    problema: "No veo radicados de otras dependencias o de otros usuarios.",
                    solucion: "Por política de privacidad operativa en Consulta y Seguimiento, cada funcionario visualiza únicamente los radicados enviados hacia su dependencia y su usuario."
                }
            ]
        },
        {
            id: "tramites",
            titulo: "5. Gestión de Trámites",
            categoria: "Operación",
            ruta: "/view/tramites",
            permisosRequeridos: ["ver_tramites", "gestionar_tramites"],
            permisoTexto: "ver_tramites / gestionar_tramites",
            imagen: "/img/guia/odin_ges_tramites.png",
            resumen: "Bandeja operativa para asignar, cambiar estado, cerrar radicados y administrar el catálogo de tipos de trámite.",
            paraQueSirve: "Centraliza el trabajo diario de los gestores: permite ejecutar acciones masivas o individuales (Asignar, Cambiar Estado, Reasignar, Cerrar) y configurar los tipos de trámite con sus días de plazo de respuesta.",
            pasos: [
                "Abre /view/tramites. En la pestaña 'Bandeja operativa' verás los contadores de Pendientes, En proceso, Finalizados, Vencidos y Total.",
                "Si existen radicados vencidos, verás una alerta documental destacada para priorizar su atención.",
                "Selecciona uno o varios radicados en la tabla y usa los botones superiores ('Asignar', 'Cambiar Estado', 'Reasignar' o 'Cerrar').",
                "En las pestañas 'Tipos de trámite' y 'Nuevo tipo de trámite' puedes parametrizar el plazo en días hábiles/calendario, la dependencia responsable por defecto y la prioridad inicial."
            ],
            erroresComunes: [
                {
                    problema: "Un radicado finalizado sigue apareciendo como vencido.",
                    solucion: "Al cambiar el estado a 'finalizado' (ID 7), 'archivado' (ID 5), 'rechazado' (ID 4) o 'devuelto' (ID 3), el sistema detiene el reloj de vencimiento y muestra '—' en días restantes."
                }
            ]
        },
        {
            id: "reasignaciones",
            titulo: "6. Reasignaciones y Traslados",
            categoria: "Operación",
            ruta: "/view/reasignaciones",
            permisosRequeridos: ["trasladar_radicado"],
            permisoTexto: "trasladar_radicado",
            imagen: "/img/guia/odin_ges_tramites.png",
            resumen: "Traslado trazable de un radicado hacia otra dependencia o funcionario responsable.",
            paraQueSirve: "Cuando un radicado fue enviado a una dependencia que no es competente o requiere intervención de otra área, permite trasladarlo dejando constancia en el historial y en la línea de tiempo.",
            pasos: [
                "Ingresa a /view/reasignaciones o usa el botón 'Reasignar' desde la bandeja de Gestión de Trámites.",
                "Selecciona el radicado que deseas trasladar, la nueva Dependencia destino y el nuevo Usuario responsable.",
                "Registra el motivo u observación del traslado para que quede documentado en la auditoría.",
                "Al guardar, el radicado pasa a estado 'trasladado' (ID 6) y aparece en la línea temporal del radicado."
            ],
            erroresComunes: [
                {
                    problema: "No tengo habilitado el módulo de Reasignaciones.",
                    solucion: "Solicita al administrador que asigne el permiso 'trasladar_radicado' a tu rol en el módulo de Roles y Permisos."
                }
            ]
        },
        {
            id: "ccd-documentos",
            titulo: "7. Gestión Documental (Series, Subseries, Estados y Documentos)",
            categoria: "Clasificación CCD/TRD",
            ruta: "/view/series",
            permisosRequeridos: ["ver_dependencias", "admin_ccd", "gestionar_documentos"],
            permisoTexto: "ver_dependencias / admin_ccd · gestionar_documentos",
            imagen: "/img/guia/odin_radicacion.png",
            resumen: "Administración del Cuadro de Clasificación Documental (Series, Subseries), Estados y control de versiones de Documentos.",
            paraQueSirve: "Permite estructurar las Tablas de Retención Documental (TRD) con tiempos de retención, disposición final y nivel de acceso, además de gestionar los archivos digitales anexados y sus versiones.",
            pasos: [
                "Despliega 'Gestión Documental' en el menú lateral para acceder a Series (/view/series), Subseries (/view/subseries) y Documentos (/view/documentos).",
                "En Series Documentales crea primero la categoría principal con su código único (ej. Derechos de Petición).",
                "En Subseries Documentales vincula cada subserie a su Serie padre y define retención, disposición y valor documental.",
                "En Documentos (/view/documentos) consulta los archivos subidos a los radicados, su tamaño, tipo MIME y versión actual."
            ],
            erroresComunes: [
                {
                    problema: "No puedo crear una Subserie sin Serie padre.",
                    solucion: "Primero debes registrar la Serie en /view/series y luego seleccionarla al crear la Subserie."
                }
            ]
        },
        {
            id: "dependencias",
            titulo: "8. Dependencias Institucionales",
            categoria: "Administración",
            ruta: "/view/dependencias",
            permisosRequeridos: ["ver_dependencias", "admin_ccd"],
            permisoTexto: "ver_dependencias / admin_ccd",
            imagen: "/img/guia/odin_dashboard.png",
            resumen: "Catálogo de áreas o unidades administrativas (Coordinación, Recepción, Subdirección, etc.).",
            paraQueSirve: "Define las áreas organizacionales del centro de formación SENA a las cuales pertenecen los usuarios y hacia las cuales se asignan los trámites y radicados.",
            pasos: [
                "Accede a /view/dependencias desde el menú lateral.",
                "Revisa el listado de dependencias activas o utiliza el buscador para localizar un área por código o nombre.",
                "Haz clic en 'Nueva dependencia' para registrar un área indicando su nombre, código y estado."
            ],
            erroresComunes: [
                {
                    problema: "No puedo eliminar una dependencia existente.",
                    solucion: "Si la dependencia ya tiene usuarios o radicados asociados en la base de datos, no debe eliminarse para conservar la integridad referencial."
                }
            ]
        },
        {
            id: "usuarios-roles",
            titulo: "9. Control de Acceso: Usuarios, Roles y Matriz de Permisos",
            categoria: "Administración",
            ruta: "/view/roles",
            permisosRequeridos: ["admin_usuarios", "admin_roles"],
            permisoTexto: "admin_usuarios / admin_roles",
            imagen: "/img/guia/odin_roles.png",
            resumen: "Creación de cuentas de funcionarios, asignación de dependencia/rol y configuración visual de permisos RBAC.",
            paraQueSirve: "Administra quién puede entrar a ODIN (/view/usuarios) y qué acciones exactas puede realizar cada rol mediante la matriz de permisos (/view/roles).",
            pasos: [
                "Despliega 'Control de Acceso' en el menú lateral y entra a 'Roles y Permisos' (/view/roles).",
                "Selecciona o crea un rol y marca en la matriz visual los permisos deseados (ej. ver_radicados, crear_radicado, gestionar_tramites, ver_reportes).",
                "Luego entra a 'Gestión de Usuarios' (/view/usuarios) para registrar o editar funcionarios, asignándoles su Rol, su Dependencia y su estado (ACTIVO/INACTIVO)."
            ],
            erroresComunes: [
                {
                    problema: "Cambié los permisos de un rol pero el usuario sigue sin ver el módulo.",
                    solucion: "Los permisos (authorities) se cargan en la sesión al hacer login. El usuario debe cerrar sesión y volver a ingresar para actualizar sus permisos."
                }
            ]
        },
        {
            id: "bitacora",
            titulo: "10. Bitácora de Auditoría",
            categoria: "Auditoría",
            ruta: "/view/bitacora",
            permisosRequeridos: ["ver_bitacora"],
            permisoTexto: "ver_bitacora",
            imagen: "/img/guia/odin_consulta.png",
            resumen: "Registro cronológico e inmutable de inicios/cierres de sesión, intentos fallidos y operaciones sobre radicados.",
            paraQueSirve: "Garantiza la trazabilidad y el no repudio operativo exigidos por las normas archivísticas: permite auditar quién ingresó, desde qué dirección IP, cuándo cerró sesión y qué cambios realizó.",
            pasos: [
                "Ingresa a /view/bitacora desde el menú lateral.",
                "Filtra o revisa los eventos de seguridad (LOGIN_SUCCESS, LOGIN_FAILURE, LOGOUT) y los eventos de auditoría documental.",
                "Verifica la fecha y hora exacta, el usuario involucrado y el detalle de la acción ejecutada."
            ],
            erroresComunes: [
                {
                    problema: "Quiero borrar un registro de la bitácora.",
                    solucion: "Por principio de auditoría y trazabilidad institucional, la bitácora es de solo lectura y no permite alterar ni borrar el historial."
                }
            ]
        },
        {
            id: "reportes",
            titulo: "11. Reportes y Exportaciones (PDF, Excel, CSV)",
            categoria: "Seguimiento",
            ruta: "/view/reportes",
            permisosRequeridos: ["ver_reportes"],
            permisoTexto: "ver_reportes",
            imagen: "/img/guia/odin_dashboard.png",
            resumen: "Descarga inmediata de informes consolidados de radicados en formato PDF, Excel (.xlsx) y CSV.",
            paraQueSirve: "Permite presentar informes de gestión ante coordinación o subdirección con estadísticas de radicados totales, pendientes, en proceso, finalizados y vencidos.",
            pasos: [
                "Abre /view/reportes en el menú lateral.",
                "Verifica las 5 tarjetas de resumen superior (Total, Pendientes, En proceso, Finalizados y Vencidos).",
                "En la tarjeta 'Reporte general de radicados', presiona 'Descargar PDF', 'Descargar Excel' o 'Descargar CSV' según el formato que necesites."
            ],
            erroresComunes: [
                {
                    problema: "Al presionar Descargar PDF o Excel aparece error 403.",
                    solucion: "Los endpoints de descarga requieren el permiso 'ver_reportes' (o rol Administrador/Root)."
                }
            ]
        },
        {
            id: "notificaciones-tema",
            titulo: "12. Notificaciones y Modo Oscuro SENA",
            categoria: "Utilidades",
            ruta: "/view/dashboard",
            permisosRequeridos: [],
            permisoTexto: "Disponible en toda la aplicación para usuarios autenticados",
            imagen: "/img/guia/odin_dashboard.png",
            resumen: "Campana de alertas en tiempo real y selector de tema claro/oscuro con paleta institucional SENA.",
            paraQueSirve: "Facilita el trabajo diario avisando sobre radicados pendientes o vencidos desde la barra superior y cuidando la fatiga visual mediante el Modo Oscuro institucional SENA.",
            pasos: [
                "En la barra superior derecha (Topbar), haz clic en el ícono de la campana para desplegar tus notificaciones recientes y el enlace rápido a radicados vencidos.",
                "Haz clic en el ícono de Sol/Luna junto a la campana para alternar entre Modo Claro y Modo Oscuro institucional SENA (#39a900, #007832, #00304d, #71277a).",
                "Tu preferencia de tema se guarda automáticamente en el navegador (localStorage 'odin-theme') para mantenerse al cambiar de pantalla."
            ],
            erroresComunes: [
                {
                    problema: "Cambié de computador y volvió a salir en modo claro.",
                    solucion: "La preferencia de tema se guarda localmente en cada navegador (localStorage); basta con presionar el botón de Luna una vez en el nuevo equipo."
                }
            ]
        }
    ],

    /* ========================================================================
       3. LÍNEA DE TIEMPO INTERACTIVA DEL CICLO DE VIDA DE UN RADICADO
       ======================================================================== */
    hitosCicloVida: [
        {
            paso: 1,
            estadoBd: "radicado (ID 1)",
            titulo: "1. Radicación y Comprobante",
            icono: "📥",
            pantalla: "Radicación de Documentos (/view/radicados)",
            permiso: "crear_radicado",
            descripcion: "El documento ingresa por ventanilla física, correo o web. El sistema genera el consecutivo automático (E-, S- o I-), calcula la fecha de vencimiento según el trámite y emite el comprobante PDF con código QR.",
            bitacora: "Se registra el evento inicial 'RADICACION' en la línea temporal del radicado y se almacenan los documentos anexos con versión 1."
        },
        {
            paso: 2,
            estadoBd: "radicado (ID 1)",
            titulo: "2. Asignación a Dependencia y Usuario",
            icono: "👤",
            pantalla: "Radicación (/view/radicados) o Gestión de Trámites (/view/tramites)",
            permiso: "crear_radicado / gestionar_tramites",
            descripcion: "El radicado queda vinculado a una Dependencia responsable (ej. Coordinación, Recepción, Subdirección) y a un Usuario específico, quien podrá consultarlo en su bandeja.",
            bitacora: "Queda trazabilidad del responsable asignado y se genera notificación para el usuario."
        },
        {
            paso: 3,
            estadoBd: "en tramite (ID 2)",
            titulo: "3. Gestión Activa (En Trámite)",
            icono: "⚙️",
            pantalla: "Gestión de Trámites (/view/tramites) · Detalle (/view/radicados/detalle/{id})",
            permiso: "gestionar_tramites / ver_radicados",
            descripcion: "El funcionario revisa los anexos, redacta observaciones de avance y solicita o registra firmas electrónicas internas mientras el semáforo controla los días restantes.",
            bitacora: "Cada observación ('OBSERVACION'), anexo ('DOCUMENTO') o firma ('FIRMA') se añade cronológicamente a la línea de tiempo."
        },
        {
            paso: 4,
            estadoBd: "trasladado (ID 6) / devuelto (ID 3)",
            titulo: "4. Reasignación o Devolución (Opcional)",
            icono: "🔄",
            pantalla: "Reasignaciones (/view/reasignaciones)",
            permiso: "trasladar_radicado",
            descripcion: "Si el trámite compete a otra dependencia o requiere subsanación, se traslada a la nueva dependencia/usuario o se marca como devuelto dejando justificación obligatoria.",
            bitacora: "Se crea un registro en la tabla de reasignaciones y aparece el hito 'REASIGNACION' en la línea temporal."
        },
        {
            paso: 5,
            estadoBd: "finalizado (ID 7) / rechazado (ID 4)",
            titulo: "5. Respuesta y Cierre del Trámite",
            icono: "✅",
            pantalla: "Gestión de Trámites (/view/tramites)",
            permiso: "gestionar_tramites / finalizar_radicado",
            descripcion: "Una vez emitida la respuesta oficial al ciudadano o área solicitante, el radicado se marca como 'finalizado' (o 'rechazado' si no procede). El contador de días restantes se detiene.",
            bitacora: "Se registra la fecha_cierre del radicado y el cambio de estado en el historial."
        },
        {
            paso: 6,
            estadoBd: "archivado (ID 5)",
            titulo: "6. Archivo y Retención Documental (CCD/TRD)",
            icono: "🗄️",
            pantalla: "Series y Subseries (/view/series · /view/subseries)",
            permiso: "admin_ccd / gestionar_documentos",
            descripcion: "El expediente se conserva según los años de retención y disposición final definidos en su Serie y Subserie documental, quedando disponible para consulta histórica y auditoría.",
            bitacora: "El radicado permanece íntegro para reportes y auditorías en Bitácora."
        }
    ],

    /* ========================================================================
       4. MATRIZ DE PERMISOS INTERACTIVA (RBAC REAL DE SECURITYCONFIG)
       ======================================================================== */
    permisosDisponibles: [
        { codigo: "ver_radicados", nombre: "Ver y consultar radicados", descripcion: "Permite abrir Radicación (lectura), Consulta y Seguimiento y el Detalle del radicado." },
        { codigo: "crear_radicado", nombre: "Crear nuevos radicados", descripcion: "Permite guardar nuevos radicados y subir sus anexos." },
        { codigo: "editar_radicado", nombre: "Editar radicados", descripcion: "Permite actualizar datos de un radicado existente." },
        { codigo: "finalizar_radicado", nombre: "Finalizar / eliminar radicados", descripcion: "Permite cerrar o eliminar registros de radicación." },
        { codigo: "ver_tramites", nombre: "Ver bandeja de trámites", descripcion: "Permite visualizar el módulo de Gestión de Trámites." },
        { codigo: "gestionar_tramites", nombre: "Gestionar trámites", descripcion: "Permite asignar, cambiar estados y parametrizar tipos de trámite." },
        { codigo: "trasladar_radicado", nombre: "Trasladar / Reasignar radicados", descripcion: "Habilita el módulo de Reasignaciones entre dependencias." },
        { codigo: "gestionar_documentos", nombre: "Gestionar documentos y firmas", descripcion: "Da acceso al módulo de Documentos, anexos y registro de firmas." },
        { codigo: "ver_dependencias", nombre: "Ver estructura CCD y dependencias", descripcion: "Permite consultar Dependencias, Series, Subseries y Estados." },
        { codigo: "admin_ccd", nombre: "Administrar CCD / TRD", descripcion: "Permite crear y editar Dependencias, Series, Subseries y Estados." },
        { codigo: "ver_bitacora", nombre: "Ver bitácora de auditoría", descripcion: "Habilita la consulta de Bitácora y Plantillas." },
        { codigo: "ver_reportes", nombre: "Ver y exportar reportes", descripcion: "Permite abrir Reportes y descargar PDF, Excel y CSV." },
        { codigo: "admin_usuarios", nombre: "Administrar usuarios", descripcion: "Da acceso a Gestión de Usuarios y Roles." },
        { codigo: "admin_roles", nombre: "Administrar roles y permisos", descripcion: "Permite configurar la matriz de permisos por rol." }
    ],

    pantallasSeguridad: [
        { nombre: "Dashboard (/view/dashboard)", permisos: [] },
        { nombre: "Guía de uso (/view/guia)", permisos: [] },
        { nombre: "Consulta y Seguimiento (/view/consulta)", permisos: ["ver_radicados"] },
        { nombre: "Radicación - Vista (/view/radicados)", permisos: ["ver_radicados"] },
        { nombre: "Radicación - Guardar (POST /view/radicados/save)", permisos: ["crear_radicado", "editar_radicado"] },
        { nombre: "Gestión de Trámites (/view/tramites)", permisos: ["ver_tramites", "gestionar_tramites"] },
        { nombre: "Reasignaciones (/view/reasignaciones)", permisos: ["trasladar_radicado"] },
        { nombre: "Documentos y Firmas (/view/documentos)", permisos: ["gestionar_documentos"] },
        { nombre: "Dependencias, Series y Subseries (/view/dependencias)", permisos: ["ver_dependencias", "admin_ccd"] },
        { nombre: "Bitácora de Auditoría (/view/bitacora)", permisos: ["ver_bitacora"] },
        { nombre: "Reportes y Estadísticas (/view/reportes)", permisos: ["ver_reportes"] },
        { nombre: "Usuarios y Roles (/view/usuarios · /view/roles)", permisos: ["admin_usuarios", "admin_roles"] }
    ],

    rolesPredefinidos: {
        recepcion: ["ver_radicados", "crear_radicado", "editar_radicado", "gestionar_documentos"],
        gestor: ["ver_radicados", "ver_tramites", "gestionar_tramites", "trasladar_radicado", "gestionar_documentos"],
        consulta: ["ver_radicados", "ver_reportes"],
        admin: [
            "ver_radicados", "crear_radicado", "editar_radicado", "finalizar_radicado",
            "ver_tramites", "gestionar_tramites", "trasladar_radicado", "gestionar_documentos",
            "ver_dependencias", "admin_ccd", "ver_bitacora", "ver_reportes", "admin_usuarios", "admin_roles"
        ]
    },

    /* ========================================================================
       5. ESCENARIOS PRÁCTICOS "¿QUÉ HARÍA YO?"
       ======================================================================== */
    escenarios: [
        {
            id: "esc-1",
            titulo: "Caso 1: Llegó una queja anónima por correo electrónico",
            contexto: "Un ciudadano envía una queja sobre atención en ventanilla pero solicita expresamente no revelar su nombre ni documento de identidad.",
            opciones: [
                { texto: "Rechazar el correo porque todos los radicados exigen cédula.", correcta: false, explicacion: "Incorrecto. La normativa de PQRSF permite radicar solicitudes anónimas." },
                { texto: "Ir a Radicación, marcar la casilla 'Radicación anónima', seleccionar canal Correo y completar el asunto y trámite.", correcta: true, explicacion: "¡Correcto! ODIN incluye la casilla de radicación anónima que registra el remitente como 'Anónimo' sin exigir datos personales." },
                { texto: "Inventar un número de cédula ficticio como 123456 para poder guardar.", correcta: false, explicacion: "Incorrecto. Nunca se deben registrar datos falsos en un sistema oficial de gestión documental." }
            ]
        },
        {
            id: "esc-2",
            titulo: "Caso 2: Un radicado marca '2 días' en amarillo en Consulta y Seguimiento",
            contexto: "Al entrar a Consulta y Seguimiento ves que la tarjeta 'Próximos a vencer (≤3 días)' tiene 1 radicado asignado a tu usuario.",
            opciones: [
                { texto: "Hacer clic en la tarjeta 'Próximos a vencer (≤3 días)', abrir el radicado con 'Ver', gestionar la respuesta y actualizar su estado antes del vencimiento.", correcta: true, explicacion: "¡Excelente! Cuando faltan 3 días o menos (entre 0 y 3 días), el semáforo pasa a alerta preventiva para evitar que el trámite se venza." },
                { texto: "Esperar a que pase a color rojo para poder responderlo.", correcta: false, explicacion: "Incorrecto. El color rojo indica que el plazo legal ya se venció." },
                { texto: "Borrar el radicado para que no afecte las estadísticas.", correcta: false, explicacion: "Incorrecto. Los radicados oficiales no deben eliminarse para evadir plazos." }
            ]
        },
        {
            id: "esc-3",
            titulo: "Caso 3: Me aparece la pantalla 403 Acceso Denegado al intentar descargar un reporte",
            contexto: "Necesitas descargar el archivo Excel de radicados en /view/reportes, pero el sistema te muestra la página /403.",
            opciones: [
                { texto: "Crear una cuenta nueva desde cero.", correcta: false, explicacion: "Incorrecto. No debes duplicar cuentas de usuario." },
                { texto: "Solicitar al Administrador que habilite el permiso 'ver_reportes' para tu rol en Roles y Permisos, y luego volver a iniciar sesión.", correcta: true, explicacion: "¡Correcto! La pantalla /403 indica que tu sesión está activa pero tu rol no tiene la authority 'ver_reportes' en SecurityConfig." },
                { texto: "Reiniciar el computador porque es un fallo de internet.", correcta: false, explicacion: "Incorrecto. El código HTTP 403 es una restricción de permisos por rol (RBAC), no un fallo de red." }
            ]
        },
        {
            id: "esc-4",
            titulo: "Caso 4: Un radicado fue asignado a Recepción pero le corresponde a Coordinación",
            contexto: "Al revisar el contenido del documento, notas que el tema debe ser resuelto por el Gestor de Trámites de Coordinación.",
            opciones: [
                { texto: "Usar la opción de Reasignaciones (/view/reasignaciones) para trasladarlo a Coordinación dejando la observación del motivo.", correcta: true, explicacion: "¡Muy bien! El traslado actualiza la dependencia/responsable y deja constancia en la línea de tiempo y en la bitácora." },
                { texto: "Radicar el mismo documento por segunda vez para Coordinación.", correcta: false, explicacion: "Incorrecto. Duplicar radicados rompe la unicidad del consecutivo y distorsiona las métricas." }
            ]
        }
    ],

    /* ========================================================================
       6. QUIZ EVALUATIVO (OPCIÓN MÚLTIPLE Y VERDADERO / FALSO)
       ======================================================================== */
    preguntasQuiz: [
        {
            id: "q1",
            pregunta: "1. ¿Qué datos acepta el campo de usuario en la pantalla de Login de ODIN?",
            opciones: [
                "Únicamente el correo institucional.",
                "Correo electrónico, número de identificación o nombre del usuario.",
                "Solamente el ID numérico de la base de datos."
            ],
            correcta: 1,
            explicacion: "El repositorio UsuariosRepository implementa findByLoginFlexible(), permitiendo ingresar con correo, número de identificación o nombre."
        },
        {
            id: "q2",
            pregunta: "2. (Verdadero o Falso) Si un usuario falla su contraseña 5 veces seguidas, su cuenta se bloquea temporalmente por 15 minutos.",
            opciones: [
                "Verdadero",
                "Falso"
            ],
            correcta: 0,
            explicacion: "Verdadero. La política de seguridad bloquea la cuenta durante 15 minutos tras 5 intentos fallidos consecutivos."
        },
        {
            id: "q3",
            pregunta: "3. En el módulo de Consulta y Seguimiento, ¿cuándo se considera que un radicado está 'Próximo a vencer'?",
            opciones: [
                "Cuando le faltan entre 0 y 3 días para su fecha de vencimiento y sigue activo.",
                "Cuando ya pasaron más de 10 días desde su vencimiento.",
                "Cuando su estado es Finalizado."
            ],
            correcta: 0,
            explicacion: "Los radicados activos con 0 <= diasRestantes <= 3 se contabilizan en la tarjeta 'Próximos a vencer (≤3 días)'."
        },
        {
            id: "q4",
            pregunta: "4. ¿Qué ocurre con el contador de 'Días restantes' cuando un radicado pasa a estado 'finalizado' (ID 7)?",
            opciones: [
                "Sigue restando días hasta quedar vencido.",
                "Se detiene y muestra '—' porque el trámite ya fue cerrado.",
                "Se reinicia a 30 días."
            ],
            correcta: 1,
            explicacion: "En estados terminales o cerrados (finalizado, archivado, rechazado, devuelto o con fecha de cierre), diasRestantes pasa a null y nunca se marca como vencido."
        },
        {
            id: "q5",
            pregunta: "5. ¿Cuál es el tamaño máximo permitido para adjuntar archivos en la radicación?",
            opciones: [
                "1 MB por archivo.",
                "20 MB por archivo individual y hasta 50 MB por solicitud.",
                "Sin límite de tamaño."
            ],
            correcta: 1,
            explicacion: "En application.properties está configurado max-file-size=20MB y max-request-size=50MB."
        },
        {
            id: "q6",
            pregunta: "6. ¿Qué elementos de autenticidad incluye el Comprobante PDF de un radicado?",
            opciones: [
                "Número de radicado, datos del trámite, sello 'COPIA CONTROLADA' y código QR verificable.",
                "Solamente el nombre del remitente sin fecha.",
                "Un archivo de texto plano sin formato."
            ],
            correcta: 0,
            explicacion: "El comprobante PDF generado con OpenPDF y ZXing incluye el número consecutivo, sello institucional y código QR."
        },
        {
            id: "q7",
            pregunta: "7. En Consulta y Seguimiento, ¿qué radicados puede visualizar cada usuario que inicia sesión?",
            opciones: [
                "Únicamente los radicados enviados hacia su dependencia y su usuario.",
                "Los radicados privados de todas las demás dependencias sin restricción.",
                "Ningún radicado."
            ],
            correcta: 0,
            explicacion: "ConsultaView filtra los radicados verificando que coincidan la dependencia (id_dependencia) y el usuario autenticado (id_usuario)."
        }
    ],

    /* ========================================================================
       7. GLOSARIO ARCHIVÍSTICO Y TÉCNICO (TOOLTIPS INTERACTIVOS)
       ======================================================================== */
    glosario: [
        { termino: "Radicado", definicion: "Comprobante y código único consecutivo (ej. E-2026-00001) asignado a una comunicación oficial al ingresar o salir de la entidad." },
        { termino: "CCD", definicion: "Cuadro de Clasificación Documental: instrumento archivístico que agrupa los documentos en Series y Subseries según las funciones de la entidad." },
        { termino: "TRD", definicion: "Tablas de Retención Documental: listado de series y subseries con sus tiempos de permanencia en cada fase de archivo y su disposición final." },
        { termino: "Serie Documental", definicion: "Conjunto de unidades documentales de estructura y contenido homogéneos (ej. Derechos de Petición, Actas, Circulares)." },
        { termino: "Subserie Documental", definicion: "Conjunto de unidades documentales que forman parte de una Serie y se jerarquizan e identifican de forma específica (ej. Peticiones de Información)." },
        { termino: "Trámite", definicion: "Proceso administrativo parametrizado en ODIN con un plazo de respuesta en días, prioridad por defecto y dependencia competente." },
        { termino: "Anexo", definicion: "Archivo digital (PDF, DOCX, imagen) adjuntado como soporte de un radicado, con control de versión y hash." },
        { termino: "Dependencia", definicion: "Unidad administrativa o área funcional del centro (ej. Coordinación, Recepción, Subdirección) responsable de atender trámites." },
        { termino: "Vencimiento", definicion: "Fecha límite calculada automáticamente sumando los días de respuesta del trámite a la fecha de radicación." },
        { termino: "Trazabilidad", definicion: "Capacidad de reconstruir en la línea de tiempo y en la bitácora cada acción, traslado, observación y firma realizada sobre un radicado." },
        { termino: "RBAC", definicion: "Control de Acceso Basado en Roles (Role-Based Access Control): modelo donde los permisos se asignan a roles y los roles a los usuarios." }
    ],

    /* ========================================================================
       8. PREGUNTAS FRECUENTES (FAQ) Y SOLUCIÓN DE PROBLEMAS
       ======================================================================== */
    faq: [
        {
            pregunta: "¿Por qué no puedo iniciar sesión aunque mi contraseña sea correcta?",
            respuesta: "Verifica tres causas posibles: (1) Si fallaste 5 veces seguidas, el sistema aplica un bloqueo temporal de 15 minutos; (2) Tu usuario puede estar en estado INACTIVO en Gestión de Usuarios; (3) Puedes escribir tanto tu correo como tu número de identificación o tu nombre exacto."
        },
        {
            pregunta: "¿Cómo recupero mi contraseña si no tengo acceso a mi clave actual?",
            respuesta: "En la pantalla /login haz clic en '¿Olvidaste tu contraseña?'. El flujo consta de 3 pasos: (1) Solicitar código ingresando tu correo; (2) Ingresar el código de verificación antes de que expire (30 minutos por defecto); (3) Definir y confirmar tu nueva contraseña."
        },
        {
            pregunta: "¿Qué significa el error 403 (Acceso Denegado) y cómo se soluciona?",
            respuesta: "Significa que iniciaste sesión correctamente, pero tu rol actual no tiene el permiso específico requerido por esa pantalla (por ejemplo, 'admin_usuarios' o 'ver_reportes'). Un Administrador puede otorgar el permiso en Roles y Permisos (/view/roles); luego debes cerrar sesión y volver a entrar."
        },
        {
            pregunta: "¿Por qué al presionar las tarjetas de Consulta y Seguimiento cambia también la tarjeta 'Resultados'?",
            respuesta: "Las 6 tarjetas superiores (Resultados, Pendientes, Vencidos, Próximos a vencer, Finalizados y Total) funcionan como botones de filtro rápido. Al presionar cualquiera de ellas, la tabla inferior muestra los radicados de esa categoría y la tarjeta 'Resultados' se sincroniza mostrando cuántos radicados hay en la vista seleccionada."
        },
        {
            pregunta: "¿Las pruebas que haga en los simuladores de esta Guía afectan la base de datos de Supabase?",
            respuesta: "No. Todos los simuladores de esta Guía Interactiva (Simulador de Radicación, Semáforo de Plazos, Matriz de Permisos y Quiz) funcionan 100 % en la memoria del navegador y en localStorage. Ninguna acción dentro de /view/guia modifica la base de datos."
        },
        {
            pregunta: "¿Cómo activo o desactivo el Modo Oscuro con los colores del SENA?",
            respuesta: "En la barra superior verde (Topbar), presiona el botón con ícono de Luna/Sol. El sistema aplicará de inmediato la paleta institucional SENA en modo oscuro y recordará tu elección en todas las páginas."
        }
    ]
};
