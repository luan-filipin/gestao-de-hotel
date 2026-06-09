package com.api.gestaodehotel.controller;

import com.api.gestaodehotel.dto.request.ReservaRequestDTO;
import com.api.gestaodehotel.dto.request.ReservaUpdateRequestDTO;
import com.api.gestaodehotel.dto.response.ReservaResponseDTO;
import com.api.gestaodehotel.service.ReservaService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/reserva")
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping("/criar")
    public ResponseEntity<ReservaResponseDTO> criarReserva(@RequestBody @Valid ReservaRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.criarReserva(dto));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ReservaResponseDTO> buscaReservaPorId(@PathVariable Long id){
        return ResponseEntity.ok(reservaService.buscarReservaPorId(id));
    }

    @GetMapping("/status")
    public ResponseEntity<Page<ReservaResponseDTO>> buscaReservaPeloStatus(@RequestParam(required = false) Boolean ativo, Pageable pageable){
        return ResponseEntity.ok(reservaService.buscarTodasReservas(ativo, pageable));
    }

    @PatchMapping("/desativar/{id}")
    public ResponseEntity<Void> desativaReserva(@PathVariable Long id){
        reservaService.cancelarReserva(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("atualizar/{id}")
    public ResponseEntity<ReservaResponseDTO> atualizaReserva(@RequestBody @Valid ReservaUpdateRequestDTO dto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(reservaService.atualizarReserva(id, dto));
    }
}
