package com.odin.odin.service;

import com.odin.odin.model.Series;
import com.odin.odin.model.Subseries;
import com.odin.odin.model.Tramites;
import com.odin.odin.repository.SeriesRepository;
import com.odin.odin.repository.SubseriesRepository;
import com.odin.odin.repository.TramitesRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Optional;

/**
 * Servicio de Radicación Documental para determinar automáticamente la Serie y Subserie
 * documental (CCD/TRD) en función del Trámite y el Tipo de Documento.
 *
 * Trabaja exclusivamente con las entidades y catálogos reales de la base de datos:
 * - ccd_series
 * - ccd_subseries
 * - tramites
 */
@Service
public class ClasificacionDocumentalService {

    private final TramitesRepository tramitesRepository;
    private final SeriesRepository seriesRepository;
    private final SubseriesRepository subseriesRepository;

    public ClasificacionDocumentalService(
            TramitesRepository tramitesRepository,
            SeriesRepository seriesRepository,
            SubseriesRepository subseriesRepository) {
        this.tramitesRepository = tramitesRepository;
        this.seriesRepository = seriesRepository;
        this.subseriesRepository = subseriesRepository;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResultadoClasificacion {
        private String codigoSerie;
        private String nombreSerie;
        private String textoSerie;
        private String codigoSubserie;
        private String nombreSubserie;
        private String textoSubserie;
        private boolean determinada;
        private String criterio;
    }

    /**
     * Determina de forma automática la serie y subserie documental a partir del trámite
     * y el tipo de documento seleccionados en el formulario de radicación.
     *
     * @param idTramite     Identificador del trámite seleccionado (opcional)
     * @param tipoDocumento Tipo de documento seleccionado (OFICIO, MEMORANDO, PETICION, QUEJA, OTRO)
     * @return ResultadoClasificacion con los códigos y descripciones oficiales de la TRD/CCD
     */
    public ResultadoClasificacion determinarClasificacion(Long idTramite, String tipoDocumento) {
        String nombreTramite = "";
        if (idTramite != null && idTramite > 0) {
            Optional<Tramites> optTramite = tramitesRepository.findById(idTramite);
            if (optTramite.isPresent()) {
                nombreTramite = normalizar(optTramite.get().getNombre());
            }
        }

        String tipoDocNorm = normalizar(tipoDocumento);

        // 1. Clasificación por trámite específico de Tutela
        if (nombreTramite.contains("TUTELA")) {
            return resolverPorCodigos("1.1012-27", "1.1012-27-03", "Trámite de Acción de Tutela");
        }

        // 2. Clasificación por solicitud de información / copias
        if (nombreTramite.contains("INFORMACION") && nombreTramite.contains("COPIA")) {
            return resolverPorCodigos("1.0010-27", "1.1013-27-01", "Solicitud de Información y Copias");
        }

        // 3. Trámites de Quejas, Reclamos, Sugerencias o Denuncias
        if (nombreTramite.contains("QUEJA") || nombreTramite.contains("RECLAMO")
                || nombreTramite.contains("SUGERENCIA") || nombreTramite.contains("CORRUPCION")) {
            return resolverPorCodigos("1.1012-27", "1.1012-27-02", "Trámite de Quejas, Reclamos y Sugerencias");
        }

        // 4. Trámite de Petición / PQRS general
        if (nombreTramite.contains("PETICION") || nombreTramite.equals("PQRS")) {
            return resolverPorCodigos("1.1012-27", "1.1012-27-01", "Trámite de Derechos de Petición");
        }

        // 5. Trámite de Convenios
        if (nombreTramite.contains("CONVENIO")) {
            return resolverPorCodigos("1.1013-27", "1.1010-25-02", "Trámite de Convenios");
        }

        // 6. Trámites administrativos internos: OFICIO, MEMORANDO, CIRCULAR, SOLICITUD INTERNA, ACTA, CERTIFICACION
        if (nombreTramite.contains("OFICIO") || nombreTramite.contains("MEMORANDO")
                || nombreTramite.contains("CIRCULAR") || nombreTramite.contains("SOLICITUD INTERNA")
                || nombreTramite.contains("ACTA") || nombreTramite.contains("CERTIFICACION")) {
            return resolverPorCodigos("1.1010-42", null, "Comunicaciones Oficiales / Informes Administrativos");
        }

        // 7. Si el trámite no definió la clasificación directamente, evaluar por Tipo de Documento
        if ("PETICION".equals(tipoDocNorm)) {
            return resolverPorCodigos("1.1012-27", "1.1012-27-01", "Tipo documental Derecho de Petición");
        }
        if ("QUEJA".equals(tipoDocNorm)) {
            return resolverPorCodigos("1.1012-27", "1.1012-27-02", "Tipo documental Queja / Reclamo");
        }
        if ("OFICIO".equals(tipoDocNorm) || "MEMORANDO".equals(tipoDocNorm)) {
            return resolverPorCodigos("1.1010-42", null, "Tipo documental " + tipoDocNorm);
        }

        // Si no se cuenta con datos suficientes para clasificar automáticamente
        return ResultadoClasificacion.builder()
                .codigoSerie(null)
                .nombreSerie(null)
                .textoSerie("Sin clasificación automática")
                .codigoSubserie(null)
                .nombreSubserie(null)
                .textoSubserie("Sin subserie aplicable")
                .determinada(false)
                .criterio("Sin combinación coincidente en catálogo TRD/CCD")
                .build();
    }

    private ResultadoClasificacion resolverPorCodigos(String codSerie, String codSubserie, String criterio) {
        String nombreSerie = "";
        String textoSerie = "";
        if (codSerie != null) {
            Optional<Series> optS = seriesRepository.findByCodigoSerie(codSerie);
            if (optS.isPresent()) {
                nombreSerie = optS.get().getNombre_serie();
                textoSerie = codSerie + " - " + nombreSerie;
            } else {
                textoSerie = codSerie;
            }
        }

        String nombreSubserie = "";
        String textoSubserie = "";
        if (codSubserie != null) {
            Optional<Subseries> optSub = subseriesRepository.findByCodigoSubserie(codSubserie);
            if (optSub.isPresent()) {
                nombreSubserie = optSub.get().getNombre_subserie();
                textoSubserie = codSubserie + " - " + nombreSubserie;
            } else {
                textoSubserie = codSubserie;
            }
        } else {
            textoSubserie = "No aplica subserie";
        }

        return ResultadoClasificacion.builder()
                .codigoSerie(codSerie)
                .nombreSerie(nombreSerie)
                .textoSerie(textoSerie)
                .codigoSubserie(codSubserie)
                .nombreSubserie(nombreSubserie)
                .textoSubserie(textoSubserie)
                .determinada(codSerie != null)
                .criterio(criterio)
                .build();
    }

    private String normalizar(String str) {
        if (str == null) return "";
        String clean = Normalizer.normalize(str, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return clean.trim().toUpperCase(Locale.ROOT);
    }
}
