package com.odin.odin.view;

import com.odin.odin.model.HistorialRadicado;
import com.odin.odin.repository.HistorialRadicadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
public class BitacoraView {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private HistorialRadicadoRepository historialRepository;

    @GetMapping("/view/bitacora")
    public String bitacora(
            @RequestParam(required = false) Long radicado,
            @RequestParam(required = false) Long usuario,
            @RequestParam(required = false) String accion,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        if (page < 0) page = 0;

        List<HistorialRadicado> registros;
        try {
            if (radicado != null) {
                registros = historialRepository.findByRadicadoOrderByFechaDesc(radicado);
            } else if (usuario != null) {
                registros = historialRepository.findByUsuarioOrderByFechaDesc(usuario);
            } else if (accion != null && !accion.isBlank()) {
                registros = historialRepository.findByAccionIgnoreCaseOrderByFechaDesc(accion.trim());
            } else {
                registros = historialRepository.findTop100ByOrderByFechaDesc();
            }
        } catch (Exception e) {
            registros = Collections.emptyList();
        }

        if (registros == null) registros = Collections.emptyList();
        int from = Math.min(page * PAGE_SIZE, registros.size());
        int to = Math.min(from + PAGE_SIZE, registros.size());
        List<HistorialRadicado> slice = registros.subList(from, to);
        int totalPages = Math.max(1, (int) Math.ceil(registros.size() / (double) PAGE_SIZE));

        model.addAttribute("registros", slice);
        model.addAttribute("radicadoFiltro", radicado);
        model.addAttribute("usuarioFiltro", usuario);
        model.addAttribute("accionFiltro", accion);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", registros.size());
        return "bitacora/bitacora";
    }
}
