package com.odin.odin.service;

import com.odin.odin.dto.ayuda.*;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Servicio centralizado del Centro de Ayuda de ODIN.
 *
 * NOTA DE CONFIGURACIÓN DE VIDEOS:
 * Para configurar los videos definitivos de YouTube (públicos o no listados),
 * modifique las constantes en la sección "CONFIGURACIÓN DE URLs DE VIDEOS".
 * Si la URL se deja vacía (""), el sistema mostrará automáticamente un estado
 * pendiente de video con diseño institucional, sin romper la interfaz.
 */
@Service
public class CentroAyudaService {

    // =========================================================================
    // CONFIGURACIÓN CENTRALIZADA DE URLs DE VIDEOS (YOUTUBE)
    // Ingrese aquí la URL de YouTube (ejemplo: https://youtu.be/xyz o https://www.youtube.com/watch?v=xyz)
    // Deje en blanco ("") para indicar contenido pendiente de producción.
    // =========================================================================
    public static final String URL_VIDEO_INTRODUCTORIO = ""; // Marcador: Video General ODIN

    public static final String URL_VIDEO_DASHBOARD = "";     // Marcador: Tutorial Dashboard
    public static final String URL_VIDEO_USUARIOS = "";      // Marcador: Tutorial Usuarios
    public static final String URL_VIDEO_ROLES = "";         // Marcador: Tutorial Roles y Permisos
    public static final String URL_VIDEO_DEPENDENCIAS = "";  // Marcador: Tutorial Dependencias
    public static final String URL_VIDEO_RADICADOS = "";     // Marcador: Tutorial Radicación
    public static final String URL_VIDEO_SERIES = "";        // Marcador: Tutorial Series Documentales
    public static final String URL_VIDEO_SUBSERIES = "";     // Marcador: Tutorial Subseries
    public static final String URL_VIDEO_DOCUMENTOS = "";    // Marcador: Tutorial Documentos y Expedientes
    public static final String URL_VIDEO_TRAMITES = "";      // Marcador: Tutorial Gestión de Trámites
    public static final String URL_VIDEO_CONSULTA = "";      // Marcador: Tutorial Consulta y Seguimiento
    public static final String URL_VIDEO_BITACORA = "";      // Marcador: Tutorial Bitácora y Auditoría
    public static final String URL_VIDEO_REPORTES = "";      // Marcador: Tutorial Reportes y Estadísticas

    private final VideoIntroductorioDto videoGeneral;
    private final Map<String, TutorialModuloDto> tutorialesMap = new LinkedHashMap<>();
    private final List<ManualProcedimientoDto> manuales = new ArrayList<>();
    private final List<PreguntaFrecuenteDto> preguntasFrecuentes = new ArrayList<>();

    public CentroAyudaService() {
        this.videoGeneral = inicializarVideoGeneral();
        inicializarTutoriales();
        inicializarManuales();
        inicializarPreguntasFrecuentes();
    }

    // =========================================================================
    // MÉTODOS PÚBLICOS DE CONSULTA
    // =========================================================================

    public VideoIntroductorioDto obtenerVideoGeneral() {
        return videoGeneral;
    }

    public List<TutorialModuloDto> obtenerTodosTutoriales() {
        return new ArrayList<>(tutorialesMap.values());
    }

    public TutorialModuloDto obtenerTutorialPorSlug(String slug) {
        if (slug == null) return null;
        return tutorialesMap.get(slug.toLowerCase().trim());
    }

    public List<ManualProcedimientoDto> obtenerTodosManuales() {
        return Collections.unmodifiableList(manuales);
    }

    public List<PreguntaFrecuenteDto> obtenerTodasPreguntas() {
        return Collections.unmodifiableList(preguntasFrecuentes);
    }

    public List<String> obtenerCategoriasFaq() {
        return preguntasFrecuentes.stream()
                .map(PreguntaFrecuenteDto::getCategoria)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<ResultadoBusquedaDto> buscar(String query) {
        if (query == null || query.trim().length() < 2) {
            return Collections.emptyList();
        }

        String qNorm = normalizarTexto(query);
        List<ResultadoBusquedaDto> resultados = new ArrayList<>();

        // 1. Buscar en Tutoriales
        for (TutorialModuloDto t : tutorialesMap.values()) {
            boolean matchTitulo = normalizarTexto(t.getTitulo()).contains(qNorm);
            boolean matchModulo = normalizarTexto(t.getModuloNombre()).contains(qNorm);
            boolean matchDesc = normalizarTexto(t.getDescripcionDetallada()).contains(qNorm);
            boolean matchTags = t.getPalabrasClave().stream().anyMatch(k -> normalizarTexto(k).contains(qNorm));

            if (matchTitulo || matchModulo || matchDesc || matchTags) {
                resultados.add(new ResultadoBusquedaDto(
                        "Tutorial",
                        t.getTitulo(),
                        t.getModuloNombre(),
                        t.getDescripcionCorta(),
                        "/view/ayuda/tutoriales/" + t.getSlug(),
                        "bg-blue-100 text-blue-800 dark:bg-blue-900/40 dark:text-blue-300"
                ));
            }
        }

        // 2. Buscar en Procedimientos del Manual
        for (ManualProcedimientoDto m : manuales) {
            boolean matchNombre = normalizarTexto(m.getNombreProcedimiento()).contains(qNorm);
            boolean matchObj = normalizarTexto(m.getObjetivo()).contains(qNorm);
            boolean matchPasos = m.getPasos().stream().anyMatch(p -> normalizarTexto(p.getInstruccion()).contains(qNorm));

            if (matchNombre || matchObj || matchPasos) {
                resultados.add(new ResultadoBusquedaDto(
                        "Procedimiento",
                        m.getNombreProcedimiento(),
                        m.getModulo(),
                        m.getObjetivo(),
                        "/view/ayuda/manual#" + m.getId(),
                        "bg-emerald-100 text-emerald-800 dark:bg-emerald-900/40 dark:text-emerald-300"
                ));
            }
        }

        // 3. Buscar en Preguntas Frecuentes
        for (PreguntaFrecuenteDto f : preguntasFrecuentes) {
            boolean matchPreg = normalizarTexto(f.getPregunta()).contains(qNorm);
            boolean matchResp = normalizarTexto(f.getRespuesta()).contains(qNorm);
            boolean matchTags = f.getPalabrasClave().stream().anyMatch(k -> normalizarTexto(k).contains(qNorm));

            if (matchPreg || matchResp || matchTags) {
                resultados.add(new ResultadoBusquedaDto(
                        "Pregunta Frecuente",
                        f.getPregunta(),
                        f.getCategoria(),
                        resumirTexto(f.getRespuesta(), 140),
                        "/view/ayuda/faq#" + f.getId(),
                        "bg-amber-100 text-amber-800 dark:bg-amber-900/40 dark:text-amber-300"
                ));
            }
        }

        return resultados;
    }

    // =========================================================================
    // UTILIDADES DE TRANSFORMACIÓN Y SEGURIDAD DE VIDEO
    // =========================================================================

    /**
     * Extrae de forma segura el ID de un video de YouTube y devuelve su URL de embed segura
     * en el dominio no-cookie: https://www.youtube-nocookie.com/embed/ID
     */
    public static String sanitizarEmbedUrl(String rawUrl) {
        if (rawUrl == null || rawUrl.trim().isEmpty()) {
            return null;
        }

        String url = rawUrl.trim();
        String videoId = null;

        // Formatos soportados:
        // 1. https://www.youtube.com/watch?v=VIDEO_ID
        // 2. https://youtu.be/VIDEO_ID
        // 3. https://www.youtube.com/embed/VIDEO_ID
        // 4. VIDEO_ID directamente (11 caracteres alfanuméricos)
        Pattern watchPattern = Pattern.compile("(?:v=|/v/|/embed/|youtu\\.be/)([a-zA-Z0-9_-]{11})");
        Matcher matcher = watchPattern.matcher(url);
        if (matcher.find()) {
            videoId = matcher.group(1);
        } else if (url.matches("^[a-zA-Z0-9_-]{11}$")) {
            videoId = url;
        }

        if (videoId != null && !videoId.isBlank()) {
            return "https://www.youtube-nocookie.com/embed/" + videoId + "?rel=0&modestbranding=1";
        }

        return null;
    }

    private static String normalizarTexto(String s) {
        if (s == null) return "";
        return Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .trim();
    }

    private static String resumirTexto(String texto, int longitud) {
        if (texto == null) return "";
        if (texto.length() <= longitud) return texto;
        return texto.substring(0, longitud) + "…";
    }

    // =========================================================================
    // INICIALIZACIÓN DE CONTENIDO REAL DEL SISTEMA ODIN
    // =========================================================================

    private VideoIntroductorioDto inicializarVideoGeneral() {
        VideoIntroductorioDto v = new VideoIntroductorioDto();
        v.setTitulo("Bienvenido a ODIN: Sistema Integral de Gestión Documental");
        v.setDescripcion("Conozca los pilares de la gestión de correspondencia y archivo en ODIN: radicación estandarizada, atención en bandeja de trámites, control del Cuadro de Clasificación Documental y trazabilidad en bitácora inmutable.");
        v.setDuracionEstimada("08:30 min");
        v.setVideoUrl(URL_VIDEO_INTRODUCTORIO);
        v.setEmbedVideoUrl(sanitizarEmbedUrl(URL_VIDEO_INTRODUCTORIO));
        v.setTemasTratados(Arrays.asList(
                "¿Qué es el Sistema ODIN y cuál es su marco funcional?",
                "Navegación general por el menú lateral y barra superior",
                "Estructura de radicación (Entrada, Salida e Internos)",
                "Bandejas de trabajo y flujo del ciclo de vida del radicado",
                "Consulta avanzada y semáforos de vencimiento normativo",
                "Seguridad, notificaciones y alternancia de modo oscuro/claro"
        ));
        return v;
    }

    private void inicializarTutoriales() {
        // 1. Dashboard
        TutorialModuloDto mDashboard = new TutorialModuloDto();
        mDashboard.setSlug("dashboard");
        mDashboard.setTitulo("Monitoreo y Panel de Inicio (Dashboard)");
        mDashboard.setModuloNombre("Dashboard");
        mDashboard.setCategoria("Monitoreo General");
        mDashboard.setDescripcionCorta("Visualice indicadores clave de rendimiento, estados de radicados y alertas de vencimiento en tiempo real.");
        mDashboard.setDescripcionDetallada("El módulo Dashboard concentra la visión ejecutiva del estado documental de la institución. Permite auditar de forma inmediata el volumen de radicados pendientes, en trámite, vencidos, próximos a vencer y finalizados en la jornada.");
        mDashboard.setIcono("chart-pie");
        mDashboard.setColorBadge("bg-emerald-100 text-emerald-800 dark:bg-emerald-900/40 dark:text-emerald-300");
        mDashboard.setVideoUrl(URL_VIDEO_DASHBOARD);
        mDashboard.setEmbedVideoUrl(sanitizarEmbedUrl(URL_VIDEO_DASHBOARD));
        mDashboard.setRutaSistema("/view/dashboard");
        mDashboard.setObjetivo("Interpretar los tableros de control de ODIN para priorizar la atención de comunicaciones con vencimiento inminente.");
        mDashboard.setFuncionalidadesExplicadas(Arrays.asList(
                "Tarjetas KPI de estado (Pendientes, En Trámite, Finalizados, Rechazados)",
                "Monitor de alertas de vencimiento (Vencidos vs Próximos a vencer ≤3 días)",
                "Contador de usuarios activos y documentos cargados",
                "Tabla resumen de los últimos 5 radicados registrados en el sistema"
        ));
        mDashboard.setPasos(Arrays.asList(
                new PasoGuiaDto(1, "Acceso al Dashboard", "Haga clic en 'Dashboard' desde el menú lateral principal.", "El sistema actualiza las métricas en cada carga de página.", "Visualización del panel con todas las métricas institucionales."),
                new PasoGuiaDto(2, "Interpretación de los semáforos", "Examine las tarjetas de color: verde (finalizados), amarillo (en trámite/próximos) y rojo (vencidos).", "Los radicados vencidos superan el tiempo de respuesta establecido por ley.", "Identificación del volumen prioritario de atención."),
                new PasoGuiaDto(3, "Navegación rápida", "Utilice la tabla inferior para consultar los últimos 5 radicados y acceder directamente a su gestión.", "Puede hacer clic en cualquier radicado listado.", "Redirección a la vista de detalle o trámite del radicado.")
        ));
        mDashboard.setPalabrasClave(Arrays.asList("dashboard", "kpi", "metricas", "estadisticas", "indicadores", "resumen", "vencimientos"));
        tutorialesMap.put(mDashboard.getSlug(), mDashboard);

        // 2. Radicación de Documentos
        TutorialModuloDto mRadicacion = new TutorialModuloDto();
        mRadicacion.setSlug("radicacion");
        mRadicacion.setTitulo("Radicación Oficial de Documentos");
        mRadicacion.setModuloNombre("Radicación");
        mRadicacion.setCategoria("Gestión de Correspondencia");
        mRadicacion.setDescripcionCorta("Registre comunicaciones oficiales con consecutivo único, remitente, anexos digitales y fecha de vencimiento.");
        mRadicacion.setDescripcionDetallada("El proceso de radicación formaliza el ingreso o egreso de documentos. Asigna un número consecutivo irrepetible, fecha oficial de recepción, tipificación documental y responsable de trámite.");
        mRadicacion.setIcono("document-text");
        mRadicacion.setColorBadge("bg-green-100 text-green-800 dark:bg-green-900/40 dark:text-green-300");
        mRadicacion.setVideoUrl(URL_VIDEO_RADICADOS);
        mRadicacion.setEmbedVideoUrl(sanitizarEmbedUrl(URL_VIDEO_RADICADOS));
        mRadicacion.setRutaSistema("/view/radicados");
        mRadicacion.setObjetivo("Aprender a radicar comunicaciones de Entrada, Salida e Internas cumpliendo con los estándares de correspondencia oficial.");
        mRadicacion.setFuncionalidadesExplicadas(Arrays.asList(
                "Generación automática del número de radicado consecutivo",
                "Selección de tipo de radicado (Entrada, Salida, Interno)",
                "Asociación de remitentes externos con autocompletado y validación",
                "Definición de asunto, medio de recepción y fecha límite legal",
                "Carga de anexos digitales (PDF, imágenes, documentos)",
                "Generación de rótulo / comprobante de radicado"
        ));
        mRadicacion.setPasos(Arrays.asList(
                new PasoGuiaDto(1, "Iniciar nuevo radicado", "Ingrese a 'Radicación de Documentos' y presione el botón 'Nuevo Radicado'.", "Verifique tener a mano los datos del remitente y los documentos en digital.", "Apertura del formulario de radicación oficial."),
                new PasoGuiaDto(2, "Completar información básica", "Indique el Tipo de Radicado, Medio de Recepción, Asunto descriptivo y Dependencia de destino.", "El asunto debe ser claro y conciso para facilitar búsquedas posteriores.", "Formulario validado con campos obligatorios diligenciados."),
                new PasoGuiaDto(3, "Vincular remitente y anexos", "Busque el remitente en la base de datos o agregue uno nuevo; adjunte el archivo principal digitalizado.", "Los archivos deben ser legibles y estar en formato permitido.", "Documentos asociados al registro temporal."),
                new PasoGuiaDto(4, "Guardar y generar comprobante", "Haga clic en 'Guardar Radicado'. El sistema generará el consecutivo oficial y registrará el evento en la bitácora.", "El radicado pasa automáticamente a estado 'Pendiente'.", "Radicado creado con su número oficial y disponible en bandeja.")
        ));
        mRadicacion.setPalabrasClave(Arrays.asList("radicado", "radicacion", "entrada", "salida", "interno", "consecutivo", "rotulo", "remitente"));
        tutorialesMap.put(mRadicacion.getSlug(), mRadicacion);

        // 3. Trámites y Reasignaciones
        TutorialModuloDto mTramites = new TutorialModuloDto();
        mTramites.setSlug("tramites");
        mTramites.setTitulo("Gestión de Trámites y Reasignaciones");
        mTramites.setModuloNombre("Trámites");
        mTramites.setCategoria("Flujo Operativo");
        mTramites.setDescripcionCorta("Gestione la bandeja de comunicaciones asignadas, atienda solicitudes y traslade radicados entre dependencias.");
        mTramites.setDescripcionDetallada("Permite a los funcionarios y responsables de dependencia dar curso a las solicitudes documentales, emitir respuestas, adjuntar evidencias y reasignar correspondencia que deba ser atendida por otra unidad organizativa.");
        mTramites.setIcono("arrow-path-rounded-square");
        mTramites.setColorBadge("bg-purple-100 text-purple-800 dark:bg-purple-900/40 dark:text-purple-300");
        mTramites.setVideoUrl(URL_VIDEO_TRAMITES);
        mTramites.setEmbedVideoUrl(sanitizarEmbedUrl(URL_VIDEO_TRAMITES));
        mTramites.setRutaSistema("/view/tramites");
        mTramites.setObjetivo("Dominar el flujo de vida de un radicado desde su asignación hasta su respuesta definitiva o reasignación.");
        mTramites.setFuncionalidadesExplicadas(Arrays.asList(
                "Bandeja de radicados pendientes y en trámite de la dependencia",
                "Cambio de estado: Pendiente -> En Trámite -> Finalizado / Rechazado",
                "Registro de comentarios y observaciones de gestión",
                "Reasignación o traslado justificado hacia otra dependencia responsable",
                "Consulta de historial y trazabilidad de reasignaciones"
        ));
        mTramites.setPasos(Arrays.asList(
                new PasoGuiaDto(1, "Acceder a la bandeja de trámites", "Diríjase a 'Gestión de Trámites' en el menú lateral.", "Podrá filtrar por dependencias o estados específicos.", "Lista de radicados asignados a su unidad."),
                new PasoGuiaDto(2, "Tomar en trámite un radicado", "Haga clic en la acción de tramitar. El estado cambiará a 'En Trámite', indicando que se encuentra en análisis.", "Esta acción queda asentada con fecha y hora en auditoría.", "El radicado se marca en proceso formal."),
                new PasoGuiaDto(3, "Finalizar o responder", "Una vez resuelta la solicitud, redacte la respuesta final o cargue el documento de respuesta y marque 'Finalizar'.", "Verifique que todos los puntos solicitados fueron atendidos.", "El radicado queda en estado 'Finalizado' y se cierra el ciclo de atención."),
                new PasoGuiaDto(4, "Reasignar por incompetencia funcional", "Si el radicado no corresponde a su dependencia, elija 'Reasignar', seleccione la dependencia correcta y especifique el motivo obligatorio.", "El traslado no elimina el historial previo.", "El radicado se transfiere a la nueva dependencia con notificación.")
        ));
        mTramites.setPalabrasClave(Arrays.asList("tramite", "tramites", "reasignacion", "traslado", "atencion", "finalizar", "rechazar", "bandeja"));
        tutorialesMap.put(mTramites.getSlug(), mTramites);

        // 4. Consulta y Seguimiento
        TutorialModuloDto mConsulta = new TutorialModuloDto();
        mConsulta.setSlug("consulta");
        mConsulta.setTitulo("Consulta y Seguimiento de Radicados");
        mConsulta.setModuloNombre("Consulta");
        mConsulta.setCategoria("Búsqueda y Monitoreo");
        mConsulta.setDescripcionCorta("Búsqueda multicriterio, filtros por fecha, semáforo de vencimientos y seguimiento en tiempo real.");
        mConsulta.setDescripcionDetallada("Herramienta diseñada para ubicar de inmediato cualquier radicado en el archivo institucional mediante filtros de número, texto, remitente, rango de fechas, estados y dependencias, evaluando los días restantes de vencimiento.");
        mConsulta.setIcono("magnifying-glass");
        mConsulta.setColorBadge("bg-cyan-100 text-cyan-800 dark:bg-cyan-900/40 dark:text-cyan-300");
        mConsulta.setVideoUrl(URL_VIDEO_CONSULTA);
        mConsulta.setEmbedVideoUrl(sanitizarEmbedUrl(URL_VIDEO_CONSULTA));
        mConsulta.setRutaSistema("/view/consulta");
        mConsulta.setObjetivo("Localizar expedientes rápidamente y auditar el cumplimiento de los tiempos normativos de respuesta.");
        mConsulta.setFuncionalidadesExplicadas(Arrays.asList(
                "Búsqueda por texto libre en número de radicado, asunto y remitente",
                "Filtro combinado por Estado, Dependencia y Tipo de Trámite",
                "Indicadores cuantitativos de radicados encontrados, pendientes y vencidos",
                "Semáforo de días restantes: Verde (en plazo), Naranja (≤3 días), Rojo (vencido)",
                "Enlace directo al expediente completo y su bitácora"
        ));
        mConsulta.setPasos(Arrays.asList(
                new PasoGuiaDto(1, "Ingresar a Consulta", "Seleccione 'Consulta y Seguimiento' en la barra lateral.", "No se requieren permisos de administrador para consultar.", "Apertura de la consola de búsqueda avanzada."),
                new PasoGuiaDto(2, "Aplicar criterios de filtro", "Escriba palabras clave del asunto o el número de radicado; filtre por estado o dependencia según sea necesario.", "Puede dejar campos en blanco para ver todo el universo de radicados.", "Resultados filtrados en la tabla de datos."),
                new PasoGuiaDto(3, "Revisar columna de vencimiento", "Observe el indicador de días restantes para identificar casos urgentes.", "Los radicados en rojo ya están fuera del término legal.", "Diagnóstico rápido del estado de atención."),
                new PasoGuiaDto(4, "Abrir detalle del radicado", "Haga clic en el botón de acción para ver los metadatos completos y anexos.", "Permite descargar los archivos cargados originalmente.", "Visualización exhaustiva de la comunicación.")
        ));
        mConsulta.setPalabrasClave(Arrays.asList("consulta", "seguimiento", "busqueda", "filtros", "semaforo", "vencimiento", "plazos"));
        tutorialesMap.put(mConsulta.getSlug(), mConsulta);

        // 5. CCD: Series Documentales
        TutorialModuloDto mSeries = new TutorialModuloDto();
        mSeries.setSlug("series");
        mSeries.setTitulo("Series Documentales (CCD)");
        mSeries.setModuloNombre("Series");
        mSeries.setCategoria("Clasificación Archivística");
        mSeries.setDescripcionCorta("Administre las categorías principales del Cuadro de Clasificación Documental y códigos archivísticos.");
        mSeries.setDescripcionDetallada("Las Series Documentales constituyen el primer nivel jerárquico del archivo institucional, agrupando conjuntos de documentos producidos en el ejercicio de una misma función administrativa.");
        mSeries.setIcono("folder");
        mSeries.setColorBadge("bg-amber-100 text-amber-800 dark:bg-amber-900/40 dark:text-amber-300");
        mSeries.setVideoUrl(URL_VIDEO_SERIES);
        mSeries.setEmbedVideoUrl(sanitizarEmbedUrl(URL_VIDEO_SERIES));
        mSeries.setRutaSistema("/view/series");
        mSeries.setObjetivo("Estructurar las Series del Cuadro de Clasificación Documental conforme a las directrices de la entidad.");
        mSeries.setFuncionalidadesExplicadas(Arrays.asList(
                "Catálogo oficial de Series Documentales activas",
                "Código numérico institucional de serie",
                "Nombre descriptivo de la serie archivística",
                "Estado de la serie (Activa / Inactiva)",
                "Vinculación jerárquica hacia Subseries Documentales"
        ));
        mSeries.setPasos(Arrays.asList(
                new PasoGuiaDto(1, "Acceder a Series", "Despliegue 'Gestión Documental' en el menú y seleccione 'Series Documentales'.", "Requiere rol con privilegios de administración documental.", "Listado de series configuradas en la entidad."),
                new PasoGuiaDto(2, "Crear una nueva serie", "Presione el botón 'Nueva Serie'. Ingrese el código numérico y nombre representativo.", "El código no debe colisionar con series preexistentes.", "Formulario diligenciado."),
                new PasoGuiaDto(3, "Guardar y verificar", "Haga clic en 'Guardar'. La serie quedará disponible para la creación de subseries y clasificación.", "Las series inactivas no se muestran en formularios de radicación.", "Serie incorporada en el CCD.")
        ));
        mSeries.setPalabrasClave(Arrays.asList("series", "ccd", "clasificacion", "archivo", "codigo", "cuadro", "documental"));
        tutorialesMap.put(mSeries.getSlug(), mSeries);

        // 6. CCD: Subseries Documentales
        TutorialModuloDto mSubseries = new TutorialModuloDto();
        mSubseries.setSlug("subseries");
        mSubseries.setTitulo("Subseries Documentales (CCD)");
        mSubseries.setModuloNombre("Subseries");
        mSubseries.setCategoria("Clasificación Archivística");
        mSubseries.setDescripcionCorta("Defina el segundo nivel archivístico subordinado a una Serie para una clasificación precisa.");
        mSubseries.setDescripcionDetallada("Las Subseries son divisiones de una serie documental correspondientes a tipos de trámites y asuntos específicos, facilitando la organización de expedientes y la aplicación de tablas de retención.");
        mSubseries.setIcono("folder-plus");
        mSubseries.setColorBadge("bg-amber-100 text-amber-800 dark:bg-amber-900/40 dark:text-amber-300");
        mSubseries.setVideoUrl(URL_VIDEO_SUBSERIES);
        mSubseries.setEmbedVideoUrl(sanitizarEmbedUrl(URL_VIDEO_SUBSERIES));
        mSubseries.setRutaSistema("/view/subseries");
        mSubseries.setObjetivo("Crear y vincular subseries a su serie documental padre para tipificar expedientes.");
        mSubseries.setFuncionalidadesExplicadas(Arrays.asList(
                "Asociación obligatoria con una Serie Documental padre",
                "Asignación de código archivístico de subserie",
                "Definición del nombre específico del trámite documental",
                "Gestión del estado de vigencia de la subserie"
        ));
        mSubseries.setPasos(Arrays.asList(
                new PasoGuiaDto(1, "Acceder a Subseries", "En 'Gestión Documental' seleccione 'Subseries Documentales'.", "Debe existir previamente al menos una serie registrada.", "Tabla con las subseries existentes."),
                new PasoGuiaDto(2, "Registrar subserie", "Haga clic en 'Nueva Subserie', seleccione la Serie padre e introduzca el código y nombre.", "Asegúrese de respetar la codificación oficial de la entidad.", "Formulario completado."),
                new PasoGuiaDto(3, "Confirmar registro", "Guarde los cambios para disponibilizar la subserie en el etiquetado de documentos.", "Los documentos podrán asociarse a esta subserie.", "Subserie lista para uso en el sistema.")
        ));
        mSubseries.setPalabrasClave(Arrays.asList("subseries", "ccd", "serie", "trd", "retencion", "clasificacion"));
        tutorialesMap.put(mSubseries.getSlug(), mSubseries);

        // 7. Documentos y Expedientes
        TutorialModuloDto mDocumentos = new TutorialModuloDto();
        mDocumentos.setSlug("documentos");
        mDocumentos.setTitulo("Documentos y Expedientes Digitales");
        mDocumentos.setModuloNombre("Documentos");
        mDocumentos.setCategoria("Gestión Documental");
        mDocumentos.setDescripcionCorta("Almacene, clasifique y consulte los archivos digitales adjuntos con control de metadatos.");
        mDocumentos.setDescripcionDetallada("Administra el repositorio documental digital de ODIN. Permite gestionar expedientes, visualizar anexos electrónicos y controlar el tipo y peso de los archivos garantizando su integridad.");
        mDocumentos.setIcono("document-duplicate");
        mDocumentos.setColorBadge("bg-teal-100 text-teal-800 dark:bg-teal-900/40 dark:text-teal-300");
        mDocumentos.setVideoUrl(URL_VIDEO_DOCUMENTOS);
        mDocumentos.setEmbedVideoUrl(sanitizarEmbedUrl(URL_VIDEO_DOCUMENTOS));
        mDocumentos.setRutaSistema("/view/documentos");
        mDocumentos.setObjetivo("Aprender a gestionar los documentos adjuntos y expedientes electrónicos vinculados a los radicados.");
        mDocumentos.setFuncionalidadesExplicadas(Arrays.asList(
                "Catálogo central de documentos digitales cargados",
                "Asociación de documentos a radicados específicos",
                "Control de tipos MIME y validación de extensiones permitidas",
                "Descarga y previsualización segura de archivos",
                "Historial de carga y fecha de registro"
        ));
        mDocumentos.setPasos(Arrays.asList(
                new PasoGuiaDto(1, "Ingresar a Documentos", "Seleccione 'Documentos' en el menú de Gestión Documental.", "Los documentos también se visualizan desde el detalle del radicado.", "Listado del repositorio documental."),
                new PasoGuiaDto(2, "Consultar anexos de un radicado", "Utilice el buscador para ingresar el número de radicado y ver sus archivos.", "Se listan todos los PDFs, imágenes o documentos asociados.", "Muestra de anexos disponibles."),
                new PasoGuiaDto(3, "Descargar y verificar", "Haga clic en el botón de descarga del documento requerido.", "El archivo se descarga en su equipo manteniendo su nombre y formato original.", "Descarga completada y verificada.")
        ));
        mDocumentos.setPalabrasClave(Arrays.asList("documentos", "expedientes", "anexos", "pdf", "archivos", "digitalizacion"));
        tutorialesMap.put(mDocumentos.getSlug(), mDocumentos);

        // 8. Dependencias
        TutorialModuloDto mDependencias = new TutorialModuloDto();
        mDependencias.setSlug("dependencias");
        mDependencias.setTitulo("Dependencias Organizacionales");
        mDependencias.setModuloNombre("Dependencias");
        mDependencias.setCategoria("Estructura Administrativa");
        mDependencias.setDescripcionCorta("Configure las dependencias, sedes y áreas responsables del flujo de correspondencia.");
        mDependencias.setDescripcionDetallada("Permite registrar y mantener las unidades operativas de la entidad. Las dependencias determinan las bandejas a las que se dirigen los radicados y delimitan las responsabilidades de los funcionarios.");
        mDependencias.setIcono("building-office-2");
        mDependencias.setColorBadge("bg-blue-100 text-blue-800 dark:bg-blue-900/40 dark:text-blue-300");
        mDependencias.setVideoUrl(URL_VIDEO_DEPENDENCIAS);
        mDependencias.setEmbedVideoUrl(sanitizarEmbedUrl(URL_VIDEO_DEPENDENCIAS));
        mDependencias.setRutaSistema("/view/dependencias");
        mDependencias.setObjetivo("Mantener actualizada la estructura de áreas y dependencias para el direccionamiento correcto de las comunicaciones.");
        mDependencias.setFuncionalidadesExplicadas(Arrays.asList(
                "Listado de dependencias organizacionales",
                "Código de dependencia y nombre institucional",
                "Estado activo / inactivo de la dependencia",
                "Asignación de usuarios y responsables por dependencia"
        ));
        mDependencias.setPasos(Arrays.asList(
                new PasoGuiaDto(1, "Ir a Dependencias", "Haga clic en 'Dependencias' en el menú lateral principal.", "Se requiere rol administrativo para realizar modificaciones.", "Visualización del mapa de dependencias."),
                new PasoGuiaDto(2, "Registrar nueva dependencia", "Presione 'Nueva Dependencia', diligencie el código y nombre del área.", "El código debe coincidir con el organigrama institucional.", "Datos de la dependencia listos."),
                new PasoGuiaDto(3, "Guardar registro", "Confirme la operación para que el área figure de inmediato en los formularios de radicación.", "Asegúrese de activar la dependencia.", "Dependencia habilitada.")
        ));
        mDependencias.setPalabrasClave(Arrays.asList("dependencias", "areas", "sedes", "organigrama", "estructura", "oficinas"));
        tutorialesMap.put(mDependencias.getSlug(), mDependencias);

        // 9. Control de Acceso: Usuarios
        TutorialModuloDto mUsuarios = new TutorialModuloDto();
        mUsuarios.setSlug("usuarios");
        mUsuarios.setTitulo("Gestión de Usuarios");
        mUsuarios.setModuloNombre("Usuarios");
        mUsuarios.setCategoria("Seguridad y Acceso");
        mUsuarios.setDescripcionCorta("Administre cuentas institucionales, credenciales, activación y asignación de cargos.");
        mUsuarios.setDescripcionDetallada("Módulo encargado del ciclo de vida de los usuarios en ODIN: creación de perfiles, asignación de dependencia de adscripción, cargos, activación/inactivación y restablecimiento de contraseñas.");
        mUsuarios.setIcono("users");
        mUsuarios.setColorBadge("bg-indigo-100 text-indigo-800 dark:bg-indigo-900/40 dark:text-indigo-300");
        mUsuarios.setVideoUrl(URL_VIDEO_USUARIOS);
        mUsuarios.setEmbedVideoUrl(sanitizarEmbedUrl(URL_VIDEO_USUARIOS));
        mUsuarios.setRutaSistema("/view/usuarios");
        mUsuarios.setObjetivo("Gestionar de manera segura el acceso de los funcionarios al sistema documental.");
        mUsuarios.setFuncionalidadesExplicadas(Arrays.asList(
                "Creación y edición de cuentas de usuario",
                "Asignación de dependencia y cargo institucional",
                "Estado de la cuenta: Activo / Inactivo",
                "Políticas de contraseñas seguras y auditoría de accesos"
        ));
        mUsuarios.setPasos(Arrays.asList(
                new PasoGuiaDto(1, "Acceder a Usuarios", "Despliegue 'Control de Acceso' y seleccione 'Gestión de Usuarios'.", "Exclusivo para administradores del sistema.", "Tabla de usuarios registrados."),
                new PasoGuiaDto(2, "Crear o editar usuario", "Pulse 'Nuevo Usuario' e ingrese nombre, correo institucional, cédula, cargo y dependencia.", "El correo institucional se utiliza para notificaciones y recuperación de acceso.", "Formulario validado."),
                new PasoGuiaDto(3, "Asignar rol y estado", "Seleccione el rol correspondiente y active la cuenta.", "La contraseña inicial debe ser robusta.", "Usuario creado con acceso seguro.")
        ));
        mUsuarios.setPalabrasClave(Arrays.asList("usuarios", "cuentas", "seguridad", "cargos", "contraseñas", "accesos"));
        tutorialesMap.put(mUsuarios.getSlug(), mUsuarios);

        // 10. Control de Acceso: Roles y Permisos
        TutorialModuloDto mRoles = new TutorialModuloDto();
        mRoles.setSlug("roles");
        mRoles.setTitulo("Roles y Permisos del Sistema");
        mRoles.setModuloNombre("Roles");
        mRoles.setCategoria("Seguridad y Acceso");
        mRoles.setDescripcionCorta("Configure la matriz de privilegios y permisos funcionales por perfil de usuario.");
        mRoles.setDescripcionDetallada("Garantiza el principio de menor privilegio, definiendo qué acciones puede realizar cada perfil: radicar, tramitar, consultar, reasignar, administrar el archivo o exportar reportes.");
        mRoles.setIcono("shield-check");
        mRoles.setColorBadge("bg-indigo-100 text-indigo-800 dark:bg-indigo-900/40 dark:text-indigo-300");
        mRoles.setVideoUrl(URL_VIDEO_ROLES);
        mRoles.setEmbedVideoUrl(sanitizarEmbedUrl(URL_VIDEO_ROLES));
        mRoles.setRutaSistema("/view/roles");
        mRoles.setObjetivo("Configurar los perfiles de usuario garantizando la seguridad y separación de responsabilidades.");
        mRoles.setFuncionalidadesExplicadas(Arrays.asList(
                "Catálogo de roles institucionales (Administrador, Funcionario, Radicador)",
                "Asignación granular de permisos funcionales",
                "Control de accesos a vistas y endpoints de API",
                "Protección contra escalamiento no autorizado"
        ));
        mRoles.setPasos(Arrays.asList(
                new PasoGuiaDto(1, "Ingresar a Roles", "En 'Control de Acceso', seleccione 'Roles y Permisos'.", "Solo accesible por administradores de seguridad.", "Matriz de roles disponibles."),
                new PasoGuiaDto(2, "Consultar permisos de un rol", "Haga clic en el rol a inspeccionar para visualizar sus permisos asignados.", "Cada permiso habilita botones y menús específicos.", "Detalle de permisos activos."),
                new PasoGuiaDto(3, "Guardar configuraciones", "Ajuste los permisos requeridos y guarde los cambios.", "Los cambios aplican en la siguiente sesión de los usuarios.", "Matriz actualizada.")
        ));
        mRoles.setPalabrasClave(Arrays.asList("roles", "permisos", "privilegios", "seguridad", "matriz", "autorizacion"));
        tutorialesMap.put(mRoles.getSlug(), mRoles);

        // 11. Bitácora y Trazabilidad
        TutorialModuloDto mBitacora = new TutorialModuloDto();
        mBitacora.setSlug("bitacora");
        mBitacora.setTitulo("Bitácora y Trazabilidad Forense");
        mBitacora.setModuloNombre("Bitácora");
        mBitacora.setCategoria("Auditoría y Control");
        mBitacora.setDescripcionCorta("Consulte el historial inmutable de todos los eventos ejecutados sobre los radicados.");
        mBitacora.setDescripcionDetallada("La bitácora registra cada cambio, creación, edición, traslado o finalización de radicado, registrando usuario responsable, fecha exacta con segundos, acción efectuada y detalle técnico para validez jurídica.");
        mBitacora.setIcono("clipboard-document-list");
        mBitacora.setColorBadge("bg-slate-100 text-slate-800 dark:bg-slate-800 dark:text-slate-200");
        mBitacora.setVideoUrl(URL_VIDEO_BITACORA);
        mBitacora.setEmbedVideoUrl(sanitizarEmbedUrl(URL_VIDEO_BITACORA));
        mBitacora.setRutaSistema("/view/bitacora");
        mBitacora.setObjetivo("Realizar seguimiento y auditoría forense a cualquier comunicación registrada en ODIN.");
        mBitacora.setFuncionalidadesExplicadas(Arrays.asList(
                "Registro cronológico inmutable de eventos",
                "Filtro por número de radicado específico",
                "Filtro por usuario ejecutor y tipo de acción",
                "Paginación y visualización del detalle del evento"
        ));
        mBitacora.setPasos(Arrays.asList(
                new PasoGuiaDto(1, "Acceder a Bitácora", "Seleccione 'Bitácora' en la barra lateral.", "Permite supervisar la cadena de custodia documental.", "Muestra los últimos 100 eventos del sistema."),
                new PasoGuiaDto(2, "Filtrar por radicado", "Introduzca el identificador del radicado para aislar su historial completo.", "Permite ver desde la radicación hasta el cierre.", "Historial del radicado en orden cronológico inverso."),
                new PasoGuiaDto(3, "Analizar detalle del evento", "Revise la columna de acción y descripción para conocer los motivos y cambios efectuados.", "Información con valor probatorio.", "Comprensión de la trazabilidad del expediente.")
        ));
        mBitacora.setPalabrasClave(Arrays.asList("bitacora", "auditoria", "historial", "trazabilidad", "eventos", "seguridad", "cambios"));
        tutorialesMap.put(mBitacora.getSlug(), mBitacora);

        // 12. Reportes y Estadísticas
        TutorialModuloDto mReportes = new TutorialModuloDto();
        mReportes.setSlug("reportes");
        mReportes.setTitulo("Reportes y Estadísticas");
        mReportes.setModuloNombre("Reportes");
        mReportes.setCategoria("Analítica y Control");
        mReportes.setDescripcionCorta("Genere informes gerenciales de radicación, tiempos de respuesta y exporte datos a Excel/PDF.");
        mReportes.setDescripcionDetallada("Proporciona herramientas analíticas para evaluar el desempeño de la gestión de correspondencia por dependencia, identificar cuellos de botella y generar reportes oficiales para entes de control.");
        mReportes.setIcono("chart-bar");
        mReportes.setColorBadge("bg-rose-100 text-rose-800 dark:bg-rose-900/40 dark:text-rose-300");
        mReportes.setVideoUrl(URL_VIDEO_REPORTES);
        mReportes.setEmbedVideoUrl(sanitizarEmbedUrl(URL_VIDEO_REPORTES));
        mReportes.setRutaSistema("/view/reportes");
        mReportes.setObjetivo("Construir y descargar reportes analíticos para la toma de decisiones institucionales.");
        mReportes.setFuncionalidadesExplicadas(Arrays.asList(
                "Gráficas de volumen de radicados por dependencia",
                "Consolidados por tipo de trámite y estado",
                "Reporte de radicados vencidos y tiempos promedio de respuesta",
                "Exportación de reportes tabulares a formato Excel y PDF"
        ));
        mReportes.setPasos(Arrays.asList(
                new PasoGuiaDto(1, "Ingresar a Reportes", "Haga clic en 'Reportes y Estadísticas' en el menú lateral.", "Disponible para roles con permiso de visualización de reportes.", "Consola de analítica documental."),
                new PasoGuiaDto(2, "Seleccionar tipo de reporte", "Escoja entre radicados por dependencia, vencimientos o estados en un rango de fechas.", "Filtre por fechas para reportes mensuales o trimestrales.", "Generación del gráfico y tabla consolidada."),
                new PasoGuiaDto(3, "Exportar informe", "Presione el botón de exportación deseado (Excel o PDF).", "El archivo se descargará listo para anexar a actas o informes gerenciales.", "Descarga del archivo oficial generado.")
        ));
        mReportes.setPalabrasClave(Arrays.asList("reportes", "estadisticas", "excel", "pdf", "exportar", "indicadores", "tiempos"));
        tutorialesMap.put(mReportes.getSlug(), mReportes);
    }

    private void inicializarManuales() {
        // Procedimiento 1: Iniciar Sesión y Seguridad
        ManualProcedimientoDto pLogin = new ManualProcedimientoDto();
        pLogin.setId("proc-login");
        pLogin.setCodigo("MAN-SEG-01");
        pLogin.setModulo("Seguridad y Acceso");
        pLogin.setNombreProcedimiento("Inicio de Sesión y Recuperación de Contraseña");
        pLogin.setObjetivo("Acceder de forma segura a ODIN y recuperar el acceso en caso de olvido de contraseña.");
        pLogin.setRutaSistema("/login");
        pLogin.setRequisitosPrevios(Arrays.asList(
                "Tener una cuenta institucional creada y activa en ODIN.",
                "Disponer de acceso al correo electrónico institucional asociado."
        ));
        pLogin.setPasos(Arrays.asList(
                new PasoGuiaDto(1, "Abrir el portal de ingreso", "Ingrese a la URL de ODIN en su navegador web.", "", "Se muestra la pantalla de inicio de sesión institucional."),
                new PasoGuiaDto(2, "Ingresar credenciales", "Escriba su nombre de usuario o correo y su contraseña en los campos correspondientes.", "Las contraseñas distinguen mayúsculas y minúsculas.", "Campos completados."),
                new PasoGuiaDto(3, "Iniciar sesión", "Presione el botón 'Iniciar Sesión'.", "", "El sistema valida las credenciales y redirige al Dashboard principal."),
                new PasoGuiaDto(4, "Recuperación de contraseña (si aplica)", "Si olvidó su contraseña, haga clic en '¿Olvidó su contraseña?'. Ingrese su correo institucional y siga las instrucciones del mensaje recibido.", "El enlace de restablecimiento tiene vigencia limitada.", "Nueva contraseña establecida y acceso recuperado.")
        ));
        pLogin.setResultadoEsperado("Acceso concedido al entorno seguro de ODIN con los privilegios asignados a su rol.");
        manuales.add(pLogin);

        // Procedimiento 2: Radicar una Comunicación de Entrada
        ManualProcedimientoDto pRadicarEntrada = new ManualProcedimientoDto();
        pRadicarEntrada.setId("proc-radicar-entrada");
        pRadicarEntrada.setCodigo("MAN-RAD-01");
        pRadicarEntrada.setModulo("Radicación de Documentos");
        pRadicarEntrada.setNombreProcedimiento("Radicación de Comunicación Oficial de Entrada");
        pRadicarEntrada.setObjetivo("Registrar formalmente una comunicación recibida de un remitente externo en el sistema ODIN.");
        pRadicarEntrada.setRutaSistema("/view/radicados");
        pRadicarEntrada.setRequisitosPrevios(Arrays.asList(
                "Documento físico digitalizado en formato PDF o documento digital recibido.",
                "Identificación completa del remitente (nombre, documento, correo, dirección).",
                "Permiso funcional 'crear_radicado' asignado al usuario."
        ));
        pRadicarEntrada.setPasos(Arrays.asList(
                new PasoGuiaDto(1, "Abrir formulario de radicación", "Diríjase a 'Radicación de Documentos' y presione el botón 'Nuevo Radicado'.", "", "Se despliega el formulario oficial de radicación."),
                new PasoGuiaDto(2, "Seleccionar tipo 'Entrada'", "En el campo 'Tipo de Radicado', elija la opción 'Entrada'.", "", "El formulario adapta los campos para remitente externo."),
                new PasoGuiaDto(3, "Registrar remitente", "Busque el remitente por número de documento; si no existe, pulse 'Agregar Remitente' y diligencie los datos.", "Verifique la exactitud del correo para el envío de acuse de recibo.", "Remitente vinculado correctamente."),
                new PasoGuiaDto(4, "Diligenciar asunto y dependencia", "Escriba un asunto claro y seleccione la dependencia responsable de dar respuesta al trámite.", "Evite asuntos vagos como 'Solicitud' o 'Documento'.", "Campos obligatorios completos."),
                new PasoGuiaDto(5, "Adjuntar archivo digital", "Cargue el archivo PDF digitalizado del documento recibido y sus anexos.", "El límite de peso estándar por archivo es de 20 MB.", "Archivo cargado en la lista de anexos."),
                new PasoGuiaDto(6, "Confirmar radicación", "Haga clic en 'Guardar Radicado'.", "El consecutivo generado no se puede modificar ni reutilizar.", "Radicado creado con su consecutivo oficial y rótulo disponible.")
        ));
        pRadicarEntrada.setResultadoEsperado("Radicado registrado con estado 'Pendiente' en la bandeja de la dependencia asignada y constancia oficial generada.");
        manuales.add(pRadicarEntrada);

        // Procedimiento 3: Atender y Finalizar un Trámite
        ManualProcedimientoDto pTramitar = new ManualProcedimientoDto();
        pTramitar.setId("proc-atender-tramite");
        pTramitar.setCodigo("MAN-TRA-01");
        pTramitar.setModulo("Gestión de Trámites");
        pTramitar.setNombreProcedimiento("Atención y Cierre de Trámite de un Radicado");
        pTramitar.setObjetivo("Dar trámite a una solicitud asignada, registrar avances y marcar la finalización de la respuesta.");
        pTramitar.setRutaSistema("/view/tramites");
        pTramitar.setRequisitosPrevios(Arrays.asList(
                "Estar adscrito a la dependencia asignada o tener rol de gestión.",
                "Haber generado el documento o respuesta oficial correspondiente."
        ));
        pTramitar.setPasos(Arrays.asList(
                new PasoGuiaDto(1, "Ubicar el radicado en bandeja", "Acceda a 'Gestión de Trámites' y ubique el radicado en la pestaña 'Pendientes'.", "", "Visualización del radicado en la lista."),
                new PasoGuiaDto(2, "Cambiar estado a 'En Trámite'", "Haga clic en 'Iniciar Trámite'.", "Esto indica a la institución que la solicitud está siendo gestionada.", "El estado cambia a 'En Trámite' y queda registrado en bitácora."),
                new PasoGuiaDto(3, "Revisar anexos y antecedentes", "Consulte los documentos cargados por el radicador haciendo clic en 'Ver Detalle'.", "", "Comprensión integral de la solicitud del ciudadano o entidad."),
                new PasoGuiaDto(4, "Cargar respuesta y observaciones", "Adjunte el documento de respuesta emitido y redacte una síntesis de la atención prestada.", "", "Evidencia de respuesta adjunta al radicado."),
                new PasoGuiaDto(5, "Finalizar trámite", "Seleccione la opción 'Finalizar Radicado' y confirme.", "Una vez finalizado, el tiempo de respuesta deja de contabilizarse.", "Radicado pasa a estado 'Finalizado' y se cierra el término.")
        ));
        pTramitar.setResultadoEsperado("Radicado finalizado oportunamente dentro del término normativo con trazabilidad completa de respuesta.");
        manuales.add(pTramitar);

        // Procedimiento 4: Reasignar un Radicado
        ManualProcedimientoDto pReasignar = new ManualProcedimientoDto();
        pReasignar.setId("proc-reasignar-radicado");
        pReasignar.setCodigo("MAN-TRA-02");
        pReasignar.setModulo("Gestión de Trámites");
        pReasignar.setNombreProcedimiento("Reasignación o Traslado de Radicado entre Dependencias");
        pReasignar.setObjetivo("Transferir la responsabilidad de atención de un radicado a otra dependencia por competencia funcional.");
        pReasignar.setRutaSistema("/view/reasignaciones");
        pReasignar.setRequisitosPrevios(Arrays.asList(
                "Radicado en estado 'Pendiente' o 'En Trámite'.",
                "Permiso 'trasladar_radicado' o rol de administrador."
        ));
        pReasignar.setPasos(Arrays.asList(
                new PasoGuiaDto(1, "Identificar radicado a trasladar", "En 'Gestión de Trámites', ubique el radicado que no corresponde a su área.", "", "Radicado seleccionado."),
                new PasoGuiaDto(2, "Seleccionar opción 'Reasignar'", "Haga clic en el botón 'Reasignar' en la columna de acciones.", "", "Apertura de la ventana modal de traslado."),
                new PasoGuiaDto(3, "Elegir dependencia de destino", "Seleccione la dependencia competente en el menú desplegable.", "Verifique que la dependencia elegida sea la adecuada según sus funciones.", "Dependencia destino seleccionada."),
                new PasoGuiaDto(4, "Justificar el traslado", "Redacte en el campo de justificación el motivo técnico o legal por el cual se traslada la comunicación.", "La justificación es obligatoria y queda grabada en la auditoría.", "Motivo documentado."),
                new PasoGuiaDto(5, "Confirmar reasignación", "Presione 'Confirmar Traslado'.", "", "El radicado desaparece de su bandeja y aparece en la de la nueva dependencia.")
        ));
        pReasignar.setResultadoEsperado("Radicado trasladado formalmente a la nueva dependencia sin perder su consecutivo ni sus antecedentes.");
        manuales.add(pReasignar);

        // Procedimiento 5: Consulta Avanzada y Auditoría en Bitácora
        ManualProcedimientoDto pConsulta = new ManualProcedimientoDto();
        pConsulta.setId("proc-consulta-auditoria");
        pConsulta.setCodigo("MAN-CON-01");
        pConsulta.setModulo("Consulta y Seguimiento");
        pConsulta.setNombreProcedimiento("Búsqueda Avanzada y Auditoría de un Radicado en Bitácora");
        pConsulta.setObjetivo("Localizar un radicado en el archivo histórico y auditar minuciosamente cada evento ejecutado sobre él.");
        pConsulta.setRutaSistema("/view/consulta");
        pConsulta.setRequisitosPrevios(Arrays.asList(
                "Estar autenticado en ODIN.",
                "Disponer de algún criterio de búsqueda (número, asunto, fecha, remitente)."
        ));
        pConsulta.setPasos(Arrays.asList(
                new PasoGuiaDto(1, "Ir a Consulta y Seguimiento", "Seleccione 'Consulta y Seguimiento' en el menú lateral.", "", "Formulario de filtros avanzados en pantalla."),
                new PasoGuiaDto(2, "Ejecutar búsqueda", "Ingrese el número de radicado o parte del asunto en el campo 'Texto' y pulse 'Filtrar'.", "", "Se listan los resultados coincidentes."),
                new PasoGuiaDto(3, "Consultar semáforo", "Revise la columna de estado y días restantes.", "Verde: en tiempo; Naranja: por vencer; Rojo: vencido.", "Diagnóstico visual inmediato."),
                new PasoGuiaDto(4, "Ver trazabilidad en Bitácora", "Copie el número de radicado, diríjase a 'Bitácora' e introdúzcalo en el filtro de radicado.", "", "Historial completo de acciones, fechas, horas y usuarios que intervinieron.")
        ));
        pConsulta.setResultadoEsperado("Acceso a la información documental y verificación inalterable de la cadena de custodia del radicado.");
        manuales.add(pConsulta);
    }

    private void inicializarPreguntasFrecuentes() {
        // Categoría 1: Radicación y Correspondencia
        preguntasFrecuentes.add(new PreguntaFrecuenteDto(
                "faq-rad-01",
                "Radicación y Correspondencia",
                "¿Cómo se genera el número consecutivo de un radicado?",
                "El número de radicado es generado de manera automática y correlativa por el sistema en el instante exacto en que se presiona 'Guardar Radicado'. Ningún usuario puede ingresar o alterar manualmente este número, garantizando la transparencia y la normatividad de correspondencia oficial.",
                "Radicación",
                "/view/radicados"
        ));
        preguntasFrecuentes.add(new PreguntaFrecuenteDto(
                "faq-rad-02",
                "Radicación y Correspondencia",
                "¿Puedo modificar el asunto o el remitente después de radicado?",
                "Por seguridad jurídica y archivística, los datos principales de un radicado quedan blindados. Solo los usuarios con permisos especiales de edición ('editar_radicado' o Administradores) pueden ajustar metadatos descriptivos, y cualquier cambio genera automáticamente un registro inmutable en la Bitácora.",
                "Radicación",
                "/view/radicados"
        ));
        preguntasFrecuentes.add(new PreguntaFrecuenteDto(
                "faq-rad-03",
                "Radicación y Correspondencia",
                "¿Cuáles formatos y pesos máximos de anexos son permitidos?",
                "ODIN admite archivos en formato PDF, imágenes (PNG, JPG) y documentos estándar de oficina. El peso máximo configurado por archivo es de 20 MB. Si requiere anexar archivos mayores, se recomienda comprimir o particionar el documento garantizando su legibilidad.",
                "Documentos",
                "/view/documentos"
        ));

        // Categoría 2: Gestión de Trámites y Reasignaciones
        preguntasFrecuentes.add(new PreguntaFrecuenteDto(
                "faq-tra-01",
                "Gestión de Trámites",
                "¿Qué significa cada estado de un radicado?",
                "• Pendiente: El documento fue radicado y asignado a una dependencia, pero aún no se ha iniciado su análisis formal.\n• En Trámite: El funcionario responsable tomó el radicado para elaborar respuesta o ejecutar la actuación correspondiente.\n• Finalizado: Se emitió respuesta formal o se cerró satisfactoriamente el trámite administrativo.\n• Rechazado: Se desestimó la solicitud por incumplimiento formal o duplicidad, mediando justificación explícita.",
                "Trámites",
                "/view/tramites"
        ));
        preguntasFrecuentes.add(new PreguntaFrecuenteDto(
                "faq-tra-02",
                "Gestión de Trámites",
                "¿Qué debo hacer si recibo un radicado que no corresponde a mi dependencia?",
                "No debe finalizar ni archivar el documento. Ingrese a la opción 'Reasignar', seleccione la dependencia competente y detalle el motivo de la transferencia en el campo de justificación obligatoria. El radicado se trasladará inmediatamente a la nueva bandeja.",
                "Trámites",
                "/view/reasignaciones"
        ));

        // Categoría 3: Consultas y Vencimientos
        preguntasFrecuentes.add(new PreguntaFrecuenteDto(
                "faq-con-01",
                "Consultas y Vencimientos",
                "¿Cómo funciona el semáforo de días restantes?",
                "El semáforo evalúa la fecha límite legal calculada al radicar:\n• Verde: El radicado cuenta con más de 3 días hábiles para su vencimiento.\n• Naranja (Alerta): Quedan 3 días o menos para el vencimiento.\n• Rojo (Vencido): Se ha superado el plazo normativo sin haber marcado el trámite como finalizado.",
                "Consulta",
                "/view/consulta"
        ));
        preguntasFrecuentes.add(new PreguntaFrecuenteDto(
                "faq-con-02",
                "Consultas y Vencimientos",
                "¿Quiénes pueden buscar y consultar radicados?",
                "Todos los usuarios autenticados de ODIN pueden utilizar la pantalla de 'Consulta y Seguimiento' para ubicar radicados mediante palabras clave, estados, dependencias y fechas.",
                "Consulta",
                "/view/consulta"
        ));

        // Categoría 4: Archivo y Clasificación Documental (CCD)
        preguntasFrecuentes.add(new PreguntaFrecuenteDto(
                "faq-ccd-01",
                "Clasificación Documental (CCD)",
                "¿Cuál es la diferencia entre una Serie y una Subserie?",
                "Una Serie Documental es una categoría amplia correspondiente a una función institucional (ej: 100 - Actas). La Subserie es una subdivisión específica dentro de esa función que agrupa tipos documentales semejantes (ej: 100.1 - Actas de Comité Directivo).",
                "Series",
                "/view/series"
        ));
        preguntasFrecuentes.add(new PreguntaFrecuenteDto(
                "faq-ccd-02",
                "Clasificación Documental (CCD)",
                "¿Por qué no puedo eliminar una Serie o Subserie?",
                "Si una Serie o Subserie ya tiene radicados o expedientes vinculados, el sistema bloquea su eliminación para proteger la integridad del archivo histórico. En su lugar, puede inactivarla para que no se use en nuevos registros.",
                "Series",
                "/view/subseries"
        ));

        // Categoría 5: Seguridad, Cuenta y Aspectos Generales
        preguntasFrecuentes.add(new PreguntaFrecuenteDto(
                "faq-seg-01",
                "Seguridad y Usuarios",
                "¿Cómo cambio entre modo claro y modo oscuro en ODIN?",
                "En la barra superior (topbar), al lado de las notificaciones, encontrará un botón con el icono de sol/luna. Al presionarlo, la interfaz cambiará inmediatamente de modo, y su preferencia se conservará para futuros accesos en ese navegador.",
                "General",
                "/view/dashboard"
        ));
        preguntasFrecuentes.add(new PreguntaFrecuenteDto(
                "faq-seg-02",
                "Seguridad y Usuarios",
                "¿Qué debo hacer si olvido mi contraseña de acceso?",
                "En la pantalla de ingreso (/login), haga clic en '¿Olvidó su contraseña?'. Digite su correo institucional registrado y el sistema le enviará un enlace de un solo uso para configurar una nueva clave.",
                "Seguridad",
                "/login"
        ));
    }
}
