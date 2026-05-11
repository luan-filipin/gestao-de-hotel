package com.api.gestaodehotel.controller;

import com.api.gestaodehotel.dto.request.QuartoRequestDTO;
import com.api.gestaodehotel.dto.request.QuartoUpdateRequestDTO;
import com.api.gestaodehotel.dto.response.QuartoResponseDTO;
import com.api.gestaodehotel.service.QuartoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/quarto")
public class QuartoController {

    private final QuartoService quartoService;

    @PostMapping("/criar")
    public ResponseEntity<QuartoResponseDTO> criarQuarto(@RequestBody @Valid QuartoRequestDTO dto){
        QuartoResponseDTO quartoCriado = quartoService.criarQuarto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(quartoCriado);
    }

    @GetMapping("/{numeroQuarto}")
    public ResponseEntity<QuartoResponseDTO> buscaQuartoPeloNumeroDoQuarto(
            @PathVariable
            Integer numeroQuarto){
        QuartoResponseDTO quarto = quartoService.buscarQuartoPorNumeroDoQuarto(numeroQuarto);
        return ResponseEntity.ok(quarto);
    }

    @GetMapping()
    public ResponseEntity<List<QuartoResponseDTO>> buscaTodosOsQuartosPeloStatus(
            @RequestParam(required = false)
            Boolean ativo){
        List<QuartoResponseDTO> quartos = quartoService.buscarTodosQuartos(ativo);
        return ResponseEntity.ok(quartos);
    }

    @PatchMapping("/desativar/{numeroQuarto}")
    public ResponseEntity<Void> desativarQuarto(
            @PathVariable
             Integer numeroQuarto){
        quartoService.desativarQuarto(numeroQuarto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/atualizar/{numeroQuarto}")
    public ResponseEntity<QuartoResponseDTO> atualizaQuarto(
            @RequestBody
            @Valid
            QuartoUpdateRequestDTO dto,
            @PathVariable
            Integer numeroQuarto){
        QuartoResponseDTO quartoAtualizado = quartoService.atualizarQuarto(numeroQuarto, dto);
        return ResponseEntity.ok(quartoAtualizado);
    }
}
