package com.odin.odin.repository;

import com.odin.odin.model.documentos;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface documentosRepository extends JpaRepository<documentos, Long>
{

    @Nullable Object deleteById();
}
