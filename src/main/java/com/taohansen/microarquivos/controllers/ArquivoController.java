package com.taohansen.microarquivos.controllers;

import com.taohansen.microarquivos.dtos.ArquivoDTO;
import com.taohansen.microarquivos.dtos.ArquivoMinDTO;
import com.taohansen.microarquivos.dtos.ArquivoUploadDTO;
import com.taohansen.microarquivos.services.ArquivoService;
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

    @GetMapping
    public ResponseEntity<List<ArquivoMinDTO>> listarArquivos(@PathVariable Long empregadoId) {
        List<ArquivoMinDTO> arquivos = service.getByEmpregadoId(empregadoId);
        return ResponseEntity.ok(arquivos);
    }

    @GetMapping("/{arquivoId}")
    public ResponseEntity<Resource> downloadArquivo(@PathVariable Long empregadoId, @PathVariable String arquivoId) {
        ArquivoDTO arquivo = service.baixarArquivo(arquivoId, empregadoId);
        ByteArrayResource resource = new ByteArrayResource(arquivo.getConteudo());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(arquivo.getTipoMime()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArquivoMinDTO> uploadArquivo(@PathVariable Long empregadoId,
                                                       @RequestParam("file") MultipartFile file,
                                                       @RequestParam("nome") String nome,
                                                       @RequestParam("descricao") String descricao) {
        ArquivoUploadDTO arquivoDTO = new ArquivoUploadDTO();
        arquivoDTO.setNome(nome);
        arquivoDTO.setDescricao(descricao);
        ArquivoMinDTO entity = service.insert(empregadoId, arquivoDTO, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    @DeleteMapping("/{arquivoId}")
    public ResponseEntity<Void> deleteArquivo(@PathVariable String arquivoId, @PathVariable Long empregadoId) {
        service.deleteArquivo(arquivoId, empregadoId);
        return ResponseEntity.noContent().build();
    }
}