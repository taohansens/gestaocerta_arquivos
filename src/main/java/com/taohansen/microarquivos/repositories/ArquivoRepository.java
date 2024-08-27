package com.taohansen.microarquivos.repositories;

import com.taohansen.microarquivos.entities.Arquivo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArquivoRepository extends MongoRepository<Arquivo, String> {
    List<Arquivo> findByEmpregadoId(Long empregadoId);
}
