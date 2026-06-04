package com.odin.odin.view;

import com.odin.odin.model.Radicados;
import com.odin.odin.repository.RadicadosRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class RadicadosView {

    @Autowired
    private RadicadosRepository radicadosRepository;

    @GetMapping("/view/radicados")
    public String inicio(Model model) {
        model.addAttribute("radicado", new Radicados());
        return "radicados/radicados_entrada";
    }

    @GetMapping("/view/radicados/entrada")
    public String entrada(Model model) {
        model.addAttribute("radicado", new Radicados());
        return "radicados/radicados_entrada";
    }

    @GetMapping("/view/radicados/salida")
    public String salida(Model model) {
        model.addAttribute("radicado", new Radicados());
        return "radicados/radicados_salida";
    }

    @GetMapping("/view/radicados/interna")
    public String interna(Model model) {
        model.addAttribute("radicado", new Radicados());
        return "radicados/radicados_interno";
    }

    @GetMapping("/view/radicados/pqrs")
    public String pqrs(Model model) {
        Radicados radicado = new Radicados();
        radicado.setTipoPQRS("Petici\u00f3n");
        model.addAttribute("radicado", radicado);
        return "radicados/radicados_pqrs";
    }

    @PostMapping({
            "/view/radicados/save",
            "/view/radicados/entrada/save",
            "/view/radicados/salida/save",
            "/view/radicados/interna/save",
            "/view/radicados/pqrs/save"
    })
    public String save(@ModelAttribute Radicados radicado, RedirectAttributes ra, HttpServletRequest request) {
        boolean isUpdate = radicado.getId_radicado() > 0;
        prepararRadicado(radicado, request.getRequestURI());

        Radicados savedRadicado = radicadosRepository.save(radicado);
        String mensaje = construirMensajeGuardado(isUpdate, request.getRequestURI(), savedRadicado);

        ra.addFlashAttribute("success", mensaje);
        ra.addFlashAttribute("mensaje", mensaje);
        ra.addFlashAttribute("savedRadicado", savedRadicado);

        return "redirect:" + obtenerDestinoFormulario(request.getRequestURI());
    }

    @GetMapping("/view/radicados/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Radicados radicado = radicadosRepository.findById(id).orElse(new Radicados());
        model.addAttribute("radicado", radicado);
        return "radicados/radicados_entrada";
    }

    @PostMapping("/view/radicados/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        radicadosRepository.deleteById(id);
        ra.addFlashAttribute("success", "Radicacion eliminada con exito");
        ra.addFlashAttribute("mensaje", "Radicacion eliminada con exito");
        return "redirect:/view/radicados/entrada";
    }

    private String obtenerDestinoFormulario(String uri) {
        if (uri.contains("/view/radicados/salida/save")) {
            return "/view/radicados/salida";
        }

        if (uri.contains("/view/radicados/interna/save")) {
            return "/view/radicados/interna";
        }

        if (uri.contains("/view/radicados/pqrs/save")) {
            return "/view/radicados/pqrs";
        }

        return "/view/radicados/entrada";
    }

    private String construirMensajeGuardado(boolean isUpdate, String uri, Radicados radicado) {
        String accion = isUpdate ? "actualizada" : "guardada";

        if (uri.contains("/view/radicados/pqrs/save")) {
            return "Radicacion PQRS " + accion + " con exito";
        }

        if (uri.contains("/view/radicados/interna/save")) {
            return "Radicacion interna " + accion + " con exito";
        }

        if (uri.contains("/view/radicados/salida/save")) {
            return "Radicacion de salida " + accion + " con exito";
        }

        if (tieneTexto(radicado.getTipoPQRS())) {
            return "Radicacion PQRS " + accion + " con exito";
        }

        if (tieneTexto(radicado.getDependenciaOrigen())) {
            return "Radicacion interna " + accion + " con exito";
        }

        if (tieneTexto(radicado.getCanalRecepcion()) && radicado.getCanalRecepcion().toLowerCase().contains("salida")) {
            return "Radicacion de salida " + accion + " con exito";
        }

        if (tieneTexto(radicado.getTipoDocumento()) && radicado.getTipoDocumento().equalsIgnoreCase("Interno")) {
            return "Radicacion interna " + accion + " con exito";
        }

        return "Radicacion de entrada " + accion + " con exito";
    }

    private String obtenerPrefijoRadicado(String uri, Radicados radicado) {
        if (uri.contains("/view/radicados/salida/save")) {
            return "SAL-";
        }

        if (uri.contains("/view/radicados/interna/save")) {
            return "INT-";
        }

        if (uri.contains("/view/radicados/pqrs/save")) {
            return "PQR-";
        }

        if (tieneTexto(radicado.getTipoPQRS())) {
            return "PQR-";
        }

        if (tieneTexto(radicado.getDependenciaOrigen())) {
            return "INT-";
        }

        if (tieneTexto(radicado.getCanalRecepcion()) && radicado.getCanalRecepcion().toLowerCase().contains("salida")) {
            return "SAL-";
        }

        if (tieneTexto(radicado.getTipoDocumento()) && radicado.getTipoDocumento().equalsIgnoreCase("Interno")) {
            return "INT-";
        }

        return "ENT-";
    }

    private void prepararRadicado(Radicados radicado, String uri) {
        if (!tieneTexto(radicado.getNumero_radicado())) {
            String consecutivo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            radicado.setNumero_radicado(obtenerPrefijoRadicado(uri, radicado) + consecutivo);
        }

        if (!tieneTexto(radicado.getFecha_radicado())) {
            radicado.setFecha_radicado(tieneTexto(radicado.getFechaDocumento())
                    ? radicado.getFechaDocumento()
                    : LocalDateTime.now().toLocalDate().toString());
        }

        if (!tieneTexto(radicado.getId_tramite())) {
            radicado.setId_tramite(primerTexto(radicado.getTipoPQRS(), radicado.getTipoDocumento(),
                    radicado.getPrioridad(), "Entrada"));
        }

        if (!tieneTexto(radicado.getId_estado())) {
            radicado.setId_estado(valorODefecto(radicado.getCanalRecepcion(), "Recibido"));
        }

        if (!tieneTexto(radicado.getId_dependencia())) {
            radicado.setId_dependencia(primerTexto(radicado.getDependencia(), radicado.getDependenciaDestino(),
                    radicado.getDependenciaOrigen(), "Sin asignar"));
        }

        if (!tieneTexto(radicado.getId_usuario())) {
            radicado.setId_usuario(valorODefecto(radicado.getResponsable(), "sistema"));
        }

        if (!tieneTexto(radicado.getRemitente())) {
            radicado.setRemitente(valorODefecto(radicado.getResponsable(), "An\u00f3nimo"));
        }

        if (!tieneTexto(radicado.getAsunto())) {
            radicado.setAsunto(valorODefecto(radicado.getObservaciones(), "Sin asunto"));
        }
    }

    private String primerTexto(String... valores) {
        for (String valor : valores) {
            if (tieneTexto(valor)) {
                return valor;
            }
        }

        return "";
    }

    private String valorODefecto(String valor, String valorPorDefecto) {
        return tieneTexto(valor) ? valor : valorPorDefecto;
    }

    private boolean tieneTexto(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }
}
