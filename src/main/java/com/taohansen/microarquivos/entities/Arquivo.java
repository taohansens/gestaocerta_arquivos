package com.taohansen.microarquivos.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "arquivos")
@Data
public class Arquivo {

    @Id
    private String id;

    @Field("nome")
    private String nome;

    @Field("filename")
    private String filename;

    @Field("descricao")
    private String descricao;

    @Field("tipo_mime")
    private String tipoMime;

    @Field("tamanho")
    private Long tamanho;

    @Field("conteudo")
    private byte[] conteudo;

    @Field("empregado_id")
    private Long empregadoId;
}
