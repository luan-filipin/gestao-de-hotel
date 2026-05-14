package com.api.gestaodehotel.controller;

import com.api.gestaodehotel.dto.request.QuartoRequestDTO;
import com.api.gestaodehotel.dto.request.QuartoUpdateRequestDTO;
import com.api.gestaodehotel.dto.response.QuartoResponseDTO;
import com.api.gestaodehotel.service.QuartoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/quarto")
public class QuartoController {

    private final QuartoService quartoService;

    @PostMapping("/criar")
    public ResponseEntity<QuartoResponseDTO> criarQuarto(@RequestBody @Valid QuartoRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(quartoService.criarQuarto(dto));
    }

    @PostMapping("/criar-em-lote")
    public ResponseEntity<List<QuartoResponseDTO>> criarQuartosEmLote(@RequestBody @Valid List<@Valid QuartoRequestDTO> dtos){
        return ResponseEntity.status(HttpStatus.CREATED).body(quartoService.criarQuartosEmLote(dtos));
    }

    @GetMapping("/{numeroQuarto}")
    public ResponseEntity<QuartoResponseDTO> buscaQuartoPeloNumeroDoQuarto(@PathVariable Integer numeroQuarto){
        return ResponseEntity.ok(quartoService.buscarQuartoPorNumeroDoQuarto(numeroQuarto));
    }

    @GetMapping("/status")
    public ResponseEntity<List<QuartoResponseDTO>> buscaTodosOsQuartosPeloStatus(@RequestParam(required = false) Boolean ativo){
        return ResponseEntity.ok(quartoService.buscarTodosQuartos(ativo));
    }

    @PatchMapping("/desativar/{numeroQuarto}")
    public ResponseEntity<Void> desativarQuarto(@PathVariable Integer numeroQuarto){
        quartoService.desativarQuarto(numeroQuarto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/atualizar/{numeroQuarto}")
    public ResponseEntity<QuartoResponseDTO> atualizaQuarto(@RequestBody @Valid QuartoUpdateRequestDTO dto, @PathVariable Integer numeroQuarto){
        return ResponseEntity.ok(quartoService.atualizarQuarto(numeroQuarto, dto));
    }
}
