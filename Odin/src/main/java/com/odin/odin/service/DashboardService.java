package com.odin.odin.service;

import com.odin.odin.repository.RadicadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private RadicadosRepository radicadosRepository;

    public Long pendientes() {
        return radicadosRepository.countPendientes();
    }

    public Long finalizados() {
        return radicadosRepository.countFinalizados();
    }

    public Long problemas() {
        return radicadosRepository.countProblemas();
    }

    public Long vencidos() {
        return radicadosRepository.countVencidos();

    }
}