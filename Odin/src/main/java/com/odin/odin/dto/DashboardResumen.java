package com.odin.odin.dto;

import lombok.Data;

@Data
public class DashboardResumen {
    private Long totalRadicados;
    private Long pendientes;
    private Long enTramite;
    private Long finalizados;
    private Long rechazados;
    private Long vencidos;
    private Long proximosAVencer;
    private Long sinAsignar;
    private Long finalizadosHoy;
    private Long usuariosActivos;
    private Long documentosCargados;
    private Long anexosPendientes;
    public Long getTotalRadicados() { return totalRadicados; }
    public void setTotalRadicados(Long value) { this.totalRadicados = value; }
    public Long getPendientes() { return pendientes; }
    public void setPendientes(Long value) { this.pendientes = value; }
    public Long getEnTramite() { return enTramite; }
    public void setEnTramite(Long value) { this.enTramite = value; }
    public Long getFinalizados() { return finalizados; }
    public void setFinalizados(Long value) { this.finalizados = value; }
    public Long getRechazados() { return rechazados; }
    public void setRechazados(Long value) { this.rechazados = value; }
    public Long getVencidos() { return vencidos; }
    public void setVencidos(Long value) { this.vencidos = value; }
    public Long getProximosAVencer() { return proximosAVencer; }
    public void setProximosAVencer(Long value) { this.proximosAVencer = value; }
    public Long getSinAsignar() { return sinAsignar; }
    public void setSinAsignar(Long value) { this.sinAsignar = value; }
    public Long getFinalizadosHoy() { return finalizadosHoy; }
    public void setFinalizadosHoy(Long value) { this.finalizadosHoy = value; }
    public Long getUsuariosActivos() { return usuariosActivos; }
    public void setUsuariosActivos(Long value) { this.usuariosActivos = value; }
    public Long getDocumentosCargados() { return documentosCargados; }
    public void setDocumentosCargados(Long value) { this.documentosCargados = value; }
    public Long getAnexosPendientes() { return anexosPendientes; }
    public void setAnexosPendientes(Long value) { this.anexosPendientes = value; }
}
