package com.odin.odin.view;

import com.odin.odin.model.Radicados;
import com.odin.odin.repository.DependenciasRepository;
import com.odin.odin.repository.EstadosRepository;
import com.odin.odin.repository.RadicadosRepository;
import com.odin.odin.repository.SeriesRepository;
import com.odin.odin.repository.SubseriesRepository;
import com.odin.odin.repository.TramitesRepository;
import com.odin.odin.repository.UsuariosRepository;
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
public class RadicadosView
{
    @Autowired
    private RadicadosRepository radicadosRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private SubseriesRepository subseriesRepository;

    @Autowired
    private TramitesRepository tramitesRepository;

    @Autowired
    private EstadosRepository estadosRepository;

    @Autowired
    private DependenciasRepository dependenciasRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @GetMapping("/view/radicados")
    public String inicio(Model model)
    {
        Radicados radicado = new Radicados();
        prepararRadicadoNuevo(radicado);
        cargarCatalogos(model);
        model.addAttribute("radicado", radicado);
        return "radicados/radicados_entrada";
    }

    @GetMapping("/view/radicados/entrada")
    public String entrada(Model model)
    {
        Radicados radicado = new Radicados();
        prepararRadicadoNuevo(radicado);
        cargarCatalogos(model);
        model.addAttribute("radicado", radicado);
        return "radicados/radicados_entrada";
    }

    @GetMapping("/view/radicados/salida")
    public String salida(Model model)
    {
        Radicados radicado = new Radicados();
        prepararRadicadoNuevo(radicado);
        cargarCatalogos(model);
        model.addAttribute("radicado", radicado);
        return "radicados/radicados_salida";
    }

    @GetMapping("/view/radicados/interna")
    public String interna(Model model)
    {
        Radicados radicado = new Radicados();
        prepararRadicadoNuevo(radicado);
        cargarCatalogos(model);
        model.addAttribute("radicado", radicado);
        return "radicados/radicados_interno";
    }

    @GetMapping("/view/radicados/pqrs")
    public String pqrs(Model model)
    {
        Radicados radicado = new Radicados();
        radicado.setTipoPQRS("Peticion");
        prepararRadicadoNuevo(radicado);
        cargarCatalogos(model);
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
    public String save(@ModelAttribute Radicados radicado, RedirectAttributes ra, HttpServletRequest request)
    {
        boolean isUpdate = radicado.getId_radicado() != null && radicado.getId_radicado() > 0;
        prepararRadicadoParaGuardar(radicado, request.getRequestURI());

        Radicados savedRadicado = radicadosRepository.save(radicado);
        String mensaje = construirMensajeGuardado(isUpdate, request.getRequestURI());

        ra.addFlashAttribute("success", mensaje);
        ra.addFlashAttribute("mensaje", mensaje);
        ra.addFlashAttribute("savedRadicado", savedRadicado);

        return "redirect:" + obtenerDestinoFormulario(request.getRequestURI());
    }

    @GetMapping("/view/radicados/edit/{id}")
    public String edit(@PathVariable Long id, Model model)
    {
        Radicados radicado = radicadosRepository.findById(id).orElse(new Radicados());
        prepararRadicadoNuevo(radicado);
        cargarCatalogos(model);
        model.addAttribute("radicado", radicado);
        return resolverVistaFormulario(radicado);
    }

    @PostMapping("/view/radicados/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra)
    {
        radicadosRepository.deleteById(id);
        ra.addFlashAttribute("success", "Radicacion eliminada con exito");
        ra.addFlashAttribute("mensaje", "Radicacion eliminada con exito");
        return "redirect:/view/radicados/entrada";
    }

    private void cargarCatalogos(Model model)
    {
        model.addAttribute("seriesList", seriesRepository.findAll());
        model.addAttribute("subseriesList", subseriesRepository.findAll());
        model.addAttribute("tramitesList", tramitesRepository.findAll());
        model.addAttribute("estadosList", estadosRepository.findAll());
        model.addAttribute("dependenciasList", dependenciasRepository.findAll());
        model.addAttribute("usuariosList", usuariosRepository.findAll());
    }

    private static final DateTimeFormatter FORMATO_FECHA_INPUT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private void prepararRadicadoNuevo(Radicados radicado)
    {
        if (!tieneTexto(radicado.getFecha_radicado())) {
            radicado.setFecha_radicado(LocalDateTime.now().format(FORMATO_FECHA_INPUT));
        }
    }

    private void prepararRadicadoParaGuardar(Radicados radicado, String uri)
    {
        if (!tieneTexto(radicado.getNumero_radicado())) {
            String consecutivo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            radicado.setNumero_radicado(obtenerPrefijoRadicado(uri) + consecutivo);
        }

        if (!tieneTexto(radicado.getFecha_radicado())) {
            radicado.setFecha_radicado(LocalDateTime.now().format(FORMATO_FECHA_INPUT));
        }

        if (!tieneTexto(radicado.getRemitente())) {
            radicado.setRemitente("Anonimo");
        }

        if (!tieneTexto(radicado.getAsunto())) {
            radicado.setAsunto("Sin asunto");
        }
    }

    private String obtenerDestinoFormulario(String uri)
    {
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

    private String resolverVistaFormulario(Radicados radicado)
    {
        if (tieneTexto(radicado.getTipoPQRS())) {
            return "radicados/radicados_pqrs";
        }

        if (tieneTexto(radicado.getDependenciaOrigen())) {
            return "radicados/radicados_interno";
        }

        if (tieneTexto(radicado.getCanalRecepcion()) && radicado.getCanalRecepcion().toLowerCase().contains("salida")) {
            return "radicados/radicados_salida";
        }

        if (tieneTexto(radicado.getTipoDocumento()) && radicado.getTipoDocumento().equalsIgnoreCase("Interno")) {
            return "radicados/radicados_interno";
        }

        return "radicados/radicados_entrada";
    }

    private String construirMensajeGuardado(boolean isUpdate, String uri)
    {
        String accion = isUpdate ? "actualizada" : "registrada";

        if (uri.contains("/view/radicados/pqrs/save")) {
            return "Radicacion PQRS " + accion + " con exito";
        }

        if (uri.contains("/view/radicados/interna/save")) {
            return "Radicacion interna " + accion + " con exito";
        }

        if (uri.contains("/view/radicados/salida/save")) {
            return "Radicacion de salida " + accion + " con exito";
        }

        return "Radicacion de entrada " + accion + " con exito";
    }

    private String obtenerPrefijoRadicado(String uri)
    {
        if (uri.contains("/view/radicados/salida/save")) {
            return "SAL-";
        }

        if (uri.contains("/view/radicados/interna/save")) {
            return "INT-";
        }

        if (uri.contains("/view/radicados/pqrs/save")) {
            return "PQR-";
        }

        return "ENT-";
    }

    private boolean tieneTexto(String valor)
    {
        return valor != null && !valor.trim().isEmpty();
    }
}
