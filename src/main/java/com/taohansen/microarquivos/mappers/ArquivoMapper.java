package com.taohansen.microarquivos.mappers;

import com.taohansen.microarquivos.dtos.ArquivoDTO;
import com.taohansen.microarquivos.dtos.ArquivoMinDTO;
import com.taohansen.microarquivos.entities.Arquivo;
import org.springframework.stereotype.Component;

@Component
public class ArquivoMapper {

        public ArquivoMinDTO toMinDto(Arquivo arquivo) {
        if (arquivo == null) {
            return null;
        }
        ArquivoMinDTO dto = new ArquivoMinDTO();
        dto.setId(arquivo.getId());
        dto.setNome(arquivo.getNome());
        dto.setFilename(arquivo.getFilename());
        dto.setDescricao(arquivo.getDescricao());
        dto.setTamanho(arquivo.getTamanho());
        dto.setTipoMime(arquivo.getTipoMime());
        return dto;
    }

    public ArquivoDTO toDto(Arquivo arquivo) {
        if (arquivo == null) {
            return null;
        }
        ArquivoDTO dto = new ArquivoDTO();
        dto.setId(arquivo.getId());
        dto.setNome(arquivo.getNome());
        dto.setFilename(arquivo.getFilename());
        dto.setDescricao(arquivo.getDescricao());
        dto.setTamanho(arquivo.getTamanho());
        dto.setTipoMime(arquivo.getTipoMime());
        dto.setConteudo(arquivo.getConteudo());
        return dto;
    }

    public Arquivo toEntity(ArquivoDTO dto) {
        if (dto == null) {
            return null;
        }
        Arquivo arquivo = new Arquivo();
        arquivo.setId(dto.getId());
        arquivo.setNome(dto.getNome());
        arquivo.setTamanho(dto.getTamanho());
        arquivo.setFilename(dto.getFilename());
        arquivo.setDescricao(dto.getDescricao());
        arquivo.setTipoMime(dto.getTipoMime());
        arquivo.setConteudo(dto.getConteudo());
        return arquivo;
    }
}
