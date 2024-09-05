package com.taohansen.microarquivos.controllers;

import com.taohansen.microarquivos.controllers.exceptions.StandardError;
import com.taohansen.microarquivos.dtos.ArquivoDTO;
import com.taohansen.microarquivos.dtos.ArquivoMinDTO;
import com.taohansen.microarquivos.dtos.ArquivoUploadDTO;
import com.taohansen.microarquivos.services.ArquivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/arquivos/{empregadoId}")
public class ArquivoController {
    private final ArquivoService service;

    @Operation(summary = "Retorna uma lista de todos os arquivos enviados do empregado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retorna a lista com os arquivos relacionados com o ID do empregado, vazia ou não",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ArquivoMinDTO.class)))}
            )})
    @GetMapping
    public ResponseEntity<List<ArquivoMinDTO>> listarArquivos(
            @Parameter(description = "ID do empregado a ser buscado", required = true)
            @PathVariable Long empregadoId) {
        List<ArquivoMinDTO> arquivos = service.getByEmpregadoId(empregadoId);
        return ResponseEntity.ok(arquivos);
    }

    @Operation(summary = "Retorna o arquivo para download")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Download do arquivo", content = {@Content(
                    mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ArquivoMinDTO.class)))}),
            @ApiResponse(responseCode = "404", description = "ID do Arquivo não encontrado no banco de dados", content = {@Content(
                    mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StandardError.class)))}),
            @ApiResponse(responseCode = "403", description = "Forbidden. Arquivo não pertence ao ID do funcionário enviado", content = {@Content(
                    mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StandardError.class)))}),
    })
    @GetMapping("/{arquivoId}")
    public ResponseEntity<Resource> downloadArquivo(
            @Parameter(description = "ID do empregado a ser buscado", required = true)
            @PathVariable Long empregadoId,
            @Parameter(description = "ID do arquivo registrado", required = true)
            @PathVariable String arquivoId) {
        ArquivoDTO arquivo = service.baixarArquivo(arquivoId, empregadoId);
        ByteArrayResource resource = new ByteArrayResource(arquivo.getConteudo());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(arquivo.getTipoMime()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo.getFilename() + "\"")
                .body(resource);
    }

    @Operation(summary = "Upload de arquivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Arquivo enviado com sucesso.", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ArquivoMinDTO.class))}),
            @ApiResponse(responseCode = "500", description = "I/O Error. Erro ao enviar arquivo.", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = StandardError.class))}),
            @ApiResponse(responseCode = "404", description = "Empregado ID não encontrado.", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = StandardError.class))})})
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArquivoMinDTO> uploadArquivo(
            @Parameter(description = "ID do empregado a ser vinculado ao arquivo", required = true)
            @PathVariable Long empregadoId,
            @Parameter(description = "Arquivo de fato a ser enviado", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "Identificador do arquivo a ser salvo no Metadado", required = true)
            @RequestParam("nome") String nome,
            @Parameter(description = "Descrição do arquivo a ser salvo no Metadado", required = true)
            @RequestParam("descricao") String descricao) {
        ArquivoUploadDTO arquivoDTO = new ArquivoUploadDTO();
        arquivoDTO.setNome(nome);
        arquivoDTO.setDescricao(descricao);
        ArquivoMinDTO entity = service.insert(empregadoId, arquivoDTO, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    @Operation(summary = "Deleta arquivo pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Arquivo deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Operação não realizada, ID do Arquivo Não Encontrado", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = StandardError.class))}),
            @ApiResponse(responseCode = "400", description = "Operação não realizada, inconsistência no Banco de Dados.", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = StandardError.class))})})
    @DeleteMapping("/{arquivoId}")
    public ResponseEntity<Void> deleteArquivo(@PathVariable String arquivoId, @PathVariable Long empregadoId) {
        service.deleteArquivo(arquivoId, empregadoId);
        return ResponseEntity.noContent().build();
    }
}