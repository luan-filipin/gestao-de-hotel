package com.api.gestaodehotel.controller;

import com.api.gestaodehotel.dto.request.QuartoRequestDTO;
import com.api.gestaodehotel.dto.request.QuartoUpdateRequestDTO;
import com.api.gestaodehotel.dto.response.QuartoResponseDTO;
import com.api.gestaodehotel.service.QuartoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

    @GetMapping("/buscarPeloNumeroDoQuarto")
    public ResponseEntity<QuartoResponseDTO> buscaQuartoPeloNumeroDoQuarto(
            @Valid
            @NotNull(message = "O numero do quarto é obrigatorio.") Integer numeroQuarto){
        QuartoResponseDTO quarto = quartoService.buscarQuartoPorNumeroDoQuarto(numeroQuarto);
        return ResponseEntity.ok(quarto);
    }

    @GetMapping("/buscarTodosPeloStatus")
    public ResponseEntity<List<QuartoResponseDTO>> buscaTodosOsQuartosPeloStatus(Boolean ativo){
        List<QuartoResponseDTO> quartos = quartoService.buscarTodosQuartos(ativo);
        return ResponseEntity.ok(quartos);
    }

    @PatchMapping("/desativar")
    public ResponseEntity<Void> desativarQuarto(
            @Valid
            @NotNull(message = "O numero do quarto é obrigatorio") Integer numeroQuarto){
        quartoService.desativarQuarto(numeroQuarto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/atualizar")
    public ResponseEntity<QuartoResponseDTO> atualizaQuarto(
            @RequestBody
            @Valid QuartoUpdateRequestDTO dto,
            @Valid Integer numeroQuarto){
        QuartoResponseDTO quartoAtualizado = quartoService.atualizarQuarto(numeroQuarto, dto);
        return ResponseEntity.ok(quartoAtualizado);
    }
}
