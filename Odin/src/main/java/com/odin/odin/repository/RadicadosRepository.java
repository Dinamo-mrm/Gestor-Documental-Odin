package com.odin.odin.repository;

import com.odin.odin.model.Radicados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RadicadosRepository extends JpaRepository<Radicados, Long> {

    /*Long countVencidos();*/
}