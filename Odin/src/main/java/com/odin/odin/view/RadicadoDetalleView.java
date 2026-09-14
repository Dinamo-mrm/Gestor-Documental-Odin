package com.odin.odin.view;

import com.odin.odin.repository.DocumentosRepository;
import com.odin.odin.repository.RadicadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RadicadoDetalleView {

    @Autowired
    private RadicadosRepository radicadosRepository;

    @Autowired
    private DocumentosRepository documentosRepository;

    @GetMapping("/view/radicados/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        var radicado = radicadosRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Radicado no encontrado: " + id));

        model.addAttribute("radicado", radicado);
        model.addAttribute("documentos", documentosRepository.buscarPorRadicado(id));
        return "radicados/radicado-detalle";
    }
}
