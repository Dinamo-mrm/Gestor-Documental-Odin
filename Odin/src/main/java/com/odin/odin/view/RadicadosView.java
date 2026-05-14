package com.odin.odin.view;

import com.odin.odin.model.Radicados;
import com.odin.odin.repository.RadicadosRepository;
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

    // Pantalla principal -> ahora abre directamente radicación de entrada
    @GetMapping("/view/radicados")
    public String inicio(Model model) {
        model.addAttribute("radicado", new Radicados());
        return "radicados/radicados_entrada"; // primera pantalla
    }

    // Radicación de entrada
    @GetMapping("/view/radicados/entrada")
    public String entrada(Model model) {
        model.addAttribute("radicado", new Radicados());
        return "radicados/radicados_entrada";
    }

    // Radicación de salida
    @GetMapping("/view/radicados/salida")
    public String salida(Model model) {
        model.addAttribute("radicado", new Radicados());
        return "radicados/radicados_salida";
    }

    // Radicación interna
    @GetMapping("/view/radicados/interna")
    public String interna(Model model) {
        model.addAttribute("radicado", new Radicados());
        return "radicados/radicados_interno";
    }

    // PQRS
    @GetMapping("/view/radicados/pqrs")
    public String pqrs(Model model) {
        Radicados radicado = new Radicados();
        radicado.setTipoPQRS("Petición");
        model.addAttribute("radicado", radicado);
        return "radicados/radicados_pqrs";
    }

    // Guardar radicado (común para todos)
    @PostMapping({
            "/view/radicados/save",
            "/view/radicados/entrada/save",
            "/view/radicados/salida/save",
            "/view/radicados/interna/save",
            "/view/radicados/pqrs/save"
    })
    public String save(@ModelAttribute Radicados radicado, RedirectAttributes ra) {
        prepararRadicado(radicado);
        radicadosRepository.save(radicado);
        ra.addFlashAttribute("mensaje", "Radicación registrada con éxito");
        // redirige a la pantalla de entrada
        return "redirect:/view/radicados/entrada";
    }

    private void prepararRadicado(Radicados radicado) {
        if (!tieneTexto(radicado.getNumero_radicado())) {
            String consecutivo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            radicado.setNumero_radicado("ENT-" + consecutivo);
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
            radicado.setRemitente(valorODefecto(radicado.getResponsable(), "Anónimo"));
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

    // Editar radicado
    @GetMapping("/view/radicados/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Radicados radicado = radicadosRepository.findById(id).orElse(null);
        model.addAttribute("radicado", radicado);
        // por defecto abre el formulario de entrada
        return "radicados/radicados_entrada";
    }

    // Eliminar radicado
    @PostMapping("/view/radicados/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        radicadosRepository.deleteById(id);
        ra.addFlashAttribute("mensaje", "Radicación eliminada con éxito");
        // redirige a la pantalla de entrada
        return "redirect:/view/radicados/entrada";
    }
}
