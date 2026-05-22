package com.api.gestaodehotel.controller;

import com.api.gestaodehotel.dto.request.HospedeRequestDTO;
import com.api.gestaodehotel.dto.request.HospedeUpdateRequestDTO;
import com.api.gestaodehotel.dto.response.HospedeResponseDTO;
import com.api.gestaodehotel.service.HospedeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/hospede")
public class HospedeController {

    private final HospedeService hospedeService;

    @PostMapping("/criar")
    public ResponseEntity<HospedeResponseDTO> criarHospede(@RequestBody @Valid HospedeRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(hospedeService.criarHospede(dto));
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<HospedeResponseDTO> buscaHospedePeloCpf(@PathVariable @Pattern(regexp = "\\d{11}", message = "CPF deve conter apenas 11 dígitos numéricos") String cpf){
        return ResponseEntity.ok(hospedeService.buscarHospedePorCpf(cpf));
    }

    @GetMapping("/status")
    public ResponseEntity<Page<HospedeResponseDTO>> buscaTodosOsHospedes(@RequestParam(required = false) Boolean ativo, Pageable pageable){
        return ResponseEntity.ok(hospedeService.buscaTodosOsHospedes(ativo, pageable));
    }

    @PatchMapping("/desativar/cpf/{cpf}")
    public ResponseEntity<Void> desativaHospedePeloCpf(@PathVariable @Pattern(regexp = "\\d{11}", message = "CPF deve conter apenas 11 dígitos numéricos") String cpf){
        hospedeService.desativarHospedePorCpf(cpf);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/atualizar/{cpf}")
    public ResponseEntity<HospedeResponseDTO> atualizaHospede(@PathVariable @Pattern(regexp = "\\d{11}", message = "CPF deve conter apenas 11 dígitos numéricos") String cpf, @RequestBody @Valid HospedeUpdateRequestDTO dtoUpdate){
        return ResponseEntity.ok(hospedeService.atualizarHospede(cpf, dtoUpdate));
    }
}
