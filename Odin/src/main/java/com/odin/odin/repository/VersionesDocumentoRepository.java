package com.odin.odin.repository;

import com.odin.odin.model.VersionesDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VersionesDocumentoRepository extends JpaRepository<VersionesDocumento, Long> {

    @Query("SELECT v FROM VersionesDocumento v WHERE v.id_documento = :idDocumento ORDER BY v.version_numero DESC")
    List<VersionesDocumento> findById_documentoOrderByVersion_numeroDesc(@Param("idDocumento") Long idDocumento);

    @Query("SELECT COUNT(v) FROM VersionesDocumento v WHERE v.id_documento = :idDocumento")
    Integer countById_documento(@Param("idDocumento") Long idDocumento);
}
