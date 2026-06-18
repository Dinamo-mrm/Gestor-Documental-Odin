package com.odin.odin.dto;

import lombok.Data;

@Data
public class DashboardResumen {

    private Long pendientes;
    private Long enTramite;
    private Long finalizados;
    private Long rechazados;
    private Long vencidos;

    private Long usuariosActivos;
    private Long documentosCargados;
    private Long anexosPendientes;
}