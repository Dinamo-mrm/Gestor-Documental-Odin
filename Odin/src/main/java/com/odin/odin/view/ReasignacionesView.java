package com.odin.odin.view;

import com.odin.odin.model.Radicados;
import com.odin.odin.model.Reasignaciones;
import com.odin.odin.repository.DependenciasRepository;
import com.odin.odin.repository.RadicadosRepository;
import com.odin.odin.repository.ReasignacionesRepository;
import com.odin.odin.repository.UsuariosRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class ReasignacionesView {

    @Autowired
    private ReasignacionesRepository reasignacionesRepository;

    @Autowired
    private RadicadosRepository radicadosRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private DependenciasRepository dependenciasRepository;

    @GetMapping("/view/reasignaciones")
    public String lista(Model model) {

        model.addAttribute(
                "reasignaciones",
                reasignacionesRepository.findAll()
        );

        model.addAttribute(
                "dependencias",
                dependenciasRepository.findAll()
        );

        return "reasignaciones/reasignaciones";
    }

    @GetMapping("/view/reasignaciones/form")
    public String form(Model model) {

        Reasignaciones reasignacion = new Reasignaciones();

        reasignacion.setFecha(LocalDateTime.now());

        model.addAttribute(
                "reasignaciones",
                reasignacion
        );

        cargarCatalogos(model);

        return "reasignaciones/reasignacionesForm";
    }

    @PostMapping("/view/reasignaciones/save")
    @Transactional
    public String save(
            @ModelAttribute Reasignaciones reasignacion,
            RedirectAttributes ra
    ) {

        /*
         * Validamos los tres datos fundamentales.
         */
        if (reasignacion.getId_radicado() == null
                || reasignacion.getId_usuario_nuevo() == null
                || reasignacion.getId_dependencia_nueva() == null) {

            ra.addFlashAttribute(
                    "error",
                    "Radicado, usuario nuevo y dependencia son obligatorios."
            );

            return "redirect:/view/reasignaciones/form";
        }

        /*
         * Buscamos el radicado.
         */
        Radicados radicado = radicadosRepository
                .findById(
                        reasignacion
                                .getId_radicado()
                                .longValue()
                )
                .orElse(null);

        if (radicado == null) {

            ra.addFlashAttribute(
                    "error",
                    "El radicado seleccionado no existe."
            );

            return "redirect:/view/reasignaciones/form";
        }

        /*
         * Comprobamos que el nuevo usuario exista.
         */
        if (!usuariosRepository.existsById(
                reasignacion
                        .getId_usuario_nuevo()
                        .longValue()
        )) {

            ra.addFlashAttribute(
                    "error",
                    "El usuario de destino no existe."
            );

            return "redirect:/view/reasignaciones/form";
        }

        /*
         * Comprobamos que la dependencia exista.
         */
        if (!dependenciasRepository.existsById(
                reasignacion
                        .getId_dependencia_nueva()
                        .longValue()
        )) {

            ra.addFlashAttribute(
                    "error",
                    "La dependencia de destino no existe."
            );

            return "redirect:/view/reasignaciones/form";
        }

        /*
         * Si estamos creando una nueva reasignación,
         * guardamos automáticamente quién era
         * el responsable anterior.
         */
        if (reasignacion.getId_reasignacion() == null) {

            reasignacion.setId_usuario_anterior(
                    radicado.getId_usuario()
            );
        }

        /*
         * Si no viene fecha desde el formulario,
         * usamos la fecha actual.
         */
        if (reasignacion.getFecha() == null) {

            reasignacion.setFecha(
                    LocalDateTime.now()
            );
        }

        /*
         * AQUÍ OCURRE LA REASIGNACIÓN REAL.
         *
         * Cambiamos:
         * - usuario responsable
         * - dependencia responsable
         *
         * dentro del radicado.
         */
        radicadosRepository.actualizarAsignacion(
                radicado.getId_radicado(),
                reasignacion.getId_usuario_nuevo(),
                reasignacion.getId_dependencia_nueva()
        );

        /*
         * Finalmente guardamos el registro histórico
         * de la reasignación.
         */
        reasignacionesRepository.save(
                reasignacion
        );

        ra.addFlashAttribute(
                "success",
                "Reasignación registrada y radicado actualizado con éxito."
        );

        return "redirect:/view/reasignaciones";
    }

    @GetMapping("/view/reasignaciones/edit/{id}")
    public String edit(
            @PathVariable Long id,
            Model model,
            RedirectAttributes ra
    ) {

        Reasignaciones reasignacion =
                reasignacionesRepository
                        .findById(id)
                        .orElse(null);

        if (reasignacion == null) {

            ra.addFlashAttribute(
                    "error",
                    "La reasignación no existe."
            );

            return "redirect:/view/reasignaciones";
        }

        model.addAttribute(
                "reasignaciones",
                reasignacion
        );

        cargarCatalogos(model);

        return "reasignaciones/reasignacionesForm";
    }

    @PostMapping("/view/reasignaciones/delete/{id}")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes ra
    ) {

        if (reasignacionesRepository.existsById(id)) {

            reasignacionesRepository.deleteById(id);
        }

        ra.addFlashAttribute(
                "success",
                "Reasignación eliminada con éxito."
        );

        return "redirect:/view/reasignaciones";
    }

    /*
     * Carga los datos necesarios para los SELECT
     * del formulario.
     */
    private void cargarCatalogos(Model model) {

        model.addAttribute(
                "radicados",
                radicadosRepository.findAll()
        );

        model.addAttribute(
                "usuarios",
                usuariosRepository.findAll()
        );

        model.addAttribute(
                "dependencias",
                dependenciasRepository.findAll()
        );
    }
}