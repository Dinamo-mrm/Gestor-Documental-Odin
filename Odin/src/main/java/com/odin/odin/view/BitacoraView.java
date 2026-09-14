package com.odin.odin.view;

import com.odin.odin.model.HistorialRadicado;
import com.odin.odin.repository.HistorialRadicadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BitacoraView {

    @Autowired
    private HistorialRadicadoRepository historialRepository;

    @GetMapping("/view/bitacora")
    public String bitacora(
            @RequestParam(required = false) Long radicado,
            @RequestParam(required = false) Long usuario,
            @RequestParam(required = false) String accion,
            Model model) {

        List<HistorialRadicado> registros;

        if (radicado != null) {
            registros = historialRepository.findById_radicadoOrderByFechaDesc(radicado);
        } else if (usuario != null) {
            registros = historialRepository.findById_usuarioOrderByFechaDesc(usuario);
        } else if (accion != null && !accion.isBlank()) {
            registros = historialRepository.findByAccionIgnoreCaseOrderByFechaDesc(accion.trim());
        } else {
            registros = historialRepository.findTop100ByOrderByFechaDesc();
        }

        model.addAttribute("registros", registros);
        model.addAttribute("radicadoFiltro", radicado);
        model.addAttribute("usuarioFiltro", usuario);
        model.addAttribute("accionFiltro", accion);

        return "bitacora/bitacora";
    }
}
