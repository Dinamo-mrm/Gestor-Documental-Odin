package com.odin.odin.view;

import com.odin.odin.model.Tramites;
import com.odin.odin.repository.DependenciasRepository;
import com.odin.odin.repository.EstadosRepository;
import com.odin.odin.repository.RadicadosRepository;
import com.odin.odin.repository.TramitesRepository;
import com.odin.odin.repository.UsuariosRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/view/tramites")
public class TramitesView {

    @Autowired
    private TramitesRepository tramitesRepository;

    @Autowired
    private RadicadosRepository radicadosRepository;

    @Autowired
    private EstadosRepository estadosRepository;

    @Autowired
    private DependenciasRepository dependenciasRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;


    /*
     * =========================================================
     * LISTADO / GESTIÓN DE TRÁMITES
     * =========================================================
     */

    @GetMapping
    public String lista(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) Integer estado,
            @RequestParam(required = false) Long dependencia,
            @RequestParam(required = false) Long tramite,
            @RequestParam(required = false) String vencimiento,
            Model model) {

        /*
         * RADICADOS
         */
        if ("vencidos".equalsIgnoreCase(vencimiento)) {

            model.addAttribute(
                    "radicados",
                    radicadosRepository.findVencidos()
            );

        } else if ("proximos".equalsIgnoreCase(vencimiento)) {

            model.addAttribute(
                    "radicados",
                    radicadosRepository.findProximosAVencer(3)
            );

        } else {

            model.addAttribute(
                    "radicados",
                    radicadosRepository.buscar(
                            texto,
                            estado,
                            dependencia,
                            tramite
                    )
            );
        }


        /*
         * CATÁLOGOS
         */
        model.addAttribute(
                "tramites",
                tramitesRepository.findAll()
        );

        model.addAttribute(
                "estados",
                estadosRepository.findAll()
        );

        model.addAttribute(
                "dependencias",
                dependenciasRepository.findAll()
        );

        model.addAttribute(
                "usuarios",
                usuariosRepository.findAll()
        );


        /*
         * FILTROS
         */
        model.addAttribute(
                "textoFiltro",
                texto
        );

        model.addAttribute(
                "estadoFiltro",
                estado
        );

        model.addAttribute(
                "dependenciaFiltro",
                dependencia
        );

        model.addAttribute(
                "tramiteFiltro",
                tramite
        );

        model.addAttribute(
                "vencimientoFiltro",
                vencimiento
        );


        /*
         * INDICADORES
         */
        model.addAttribute(
                "totalRadicados",
                radicadosRepository.count()
        );

        model.addAttribute(
                "totalTramites",
                tramitesRepository.count()
        );

        model.addAttribute(
                "pendientes",
                radicadosRepository.countPendientes()
        );

        model.addAttribute(
                "enProceso",
                radicadosRepository.countEnTramite()
        );

        model.addAttribute(
                "finalizados",
                radicadosRepository.countFinalizados()
        );

        model.addAttribute(
                "vencidos",
                radicadosRepository.countVencidos()
        );


        /*
         * ALERTAS DE VENCIMIENTO
         */
        model.addAttribute(
                "radicadosVencidos",
                radicadosRepository.findVencidos()
        );

        model.addAttribute(
                "proximosAVencer",
                radicadosRepository.findProximosAVencer(3)
        );

        model.addAttribute(
                "diasAlertaVencimiento",
                3
        );

        return "tramites/ges_tramites";
    }


    /*
     * =========================================================
     * NUEVO TRÁMITE
     * =========================================================
     */

    @GetMapping("/form")
    public String form(Model model) {

        model.addAttribute(
                "tramites",
                new Tramites()
        );

        model.addAttribute(
                "estados",
                estadosRepository.findAll()
        );

        model.addAttribute(
                "dependencias",
                dependenciasRepository.findAll()
        );

        return "tramites/tramitesForm";
    }


    /*
     * =========================================================
     * EDITAR TRÁMITE
     * =========================================================
     */

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            Model model,
            RedirectAttributes ra) {

        Tramites tramite = tramitesRepository
                .findById(id)
                .orElse(null);

        if (tramite == null) {

            ra.addFlashAttribute(
                    "mensaje",
                    "El trámite solicitado no existe"
            );

            return "redirect:/view/tramites";
        }

        model.addAttribute(
                "tramites",
                tramite
        );

        model.addAttribute(
                "estados",
                estadosRepository.findAll()
        );

        model.addAttribute(
                "dependencias",
                dependenciasRepository.findAll()
        );

        return "tramites/tramitesForm";
    }


    /*
     * =========================================================
     * GUARDAR TRÁMITE
     * =========================================================
     */

    @PostMapping("/save")
    public String save(
            @ModelAttribute Tramites tramites,
            RedirectAttributes ra) {

        tramitesRepository.save(tramites);

        ra.addFlashAttribute(
                "mensaje",
                "Trámite registrado con éxito"
        );

        return "redirect:/view/tramites";
    }


    /*
     * =========================================================
     * ELIMINAR TRÁMITE
     * =========================================================
     */

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes ra) {

        if (!tramitesRepository.existsById(id)) {

            ra.addFlashAttribute(
                    "mensaje",
                    "El trámite solicitado no existe"
            );

            return "redirect:/view/tramites";
        }

        tramitesRepository.deleteById(id);

        ra.addFlashAttribute(
                "mensaje",
                "Trámite eliminado con éxito"
        );

        return "redirect:/view/tramites";
    }
}