package com.api.gestaodehotel.controller;

import com.api.gestaodehotel.dto.request.ReservaRequestDTO;
import com.api.gestaodehotel.dto.request.ReservaUpdateRequestDTO;
import com.api.gestaodehotel.fixture.ReservaFixture;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@DBUnit(alwaysCleanBefore = true)
@DataSet(
        value = "datasets/reserva.xml",
        executeScriptsAfter = "datasets/reset-sequences.sql"
)
class ReservaControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarReserva() throws Exception{
        ReservaRequestDTO reservaRequestDTO = ReservaFixture.criarReservaDTO(6L, 1L);
        mockMvc.perform(post("/api/reserva/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaRequestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void deveLancarErroSeRequisicaoNaoForValida() throws Exception{
        mockMvc.perform(post("/api/reserva/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Campos Inválidos"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/reserva/criar"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveLancarErroSeQuartoJaEstiverOcupado() throws Exception{
        ReservaRequestDTO reservaRequestDTO = ReservaFixture.criarReservaDTO(1L, 1L);
        mockMvc.perform(post("/api/reserva/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaRequestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem").value("Ja existe uma reserva para esse quarto: 1"))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.path").value("/api/reserva/criar"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveLancarErroSeQuartoNaoExistir() throws Exception{
        ReservaRequestDTO reservaRequestDTO = ReservaFixture.criarReservaDTO(99L, 1L);
        mockMvc.perform(post("/api/reserva/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("O quarto com o ID: 99 não foi encontrado"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/reserva/criar"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveLancarErroSeQuartoEstiverDesativado() throws Exception{
        ReservaRequestDTO reservaRequestDTO = ReservaFixture.criarReservaDTO(4L, 1L);
        mockMvc.perform(post("/api/reserva/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("O quarto: 4 esta inativo"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/reserva/criar"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveLancarErroSeHospedeNaoExistir() throws Exception{
        ReservaRequestDTO reservaRequestDTO = ReservaFixture.criarReservaDTO(5L, 99L);
        mockMvc.perform(post("/api/reserva/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("O hospede com o id: 99 não foi encontrado"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/reserva/criar"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveLancarErroSeHospedeEstiverDesativado() throws Exception{
        ReservaRequestDTO reservaRequestDTO = ReservaFixture.criarReservaDTO(5L, 4L);
        mockMvc.perform(post("/api/reserva/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("O Hospede com o id: 4 esta inativo"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/reserva/criar"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveBuscarReservaPorId() throws Exception{
        mockMvc.perform(get("/api/reserva/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveLancarErroSeReservaNaoExistir() throws Exception{
        mockMvc.perform(get("/api/reserva/id/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("A Reserva com id: 99 não foi encontrada"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/reserva/id/99"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveBuscarTodasAsReservasTrue() throws Exception{
        mockMvc.perform(get("/api/reserva/status")
                        .param("ativo", "true")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].ativo").value(true));
    }

    @Test
    void deveBuscarTodasAsReservasFalse() throws Exception{
        mockMvc.perform(get("/api/reserva/status")
                .param("ativo", "false")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].ativo").value(false));
    }

    @Test
    void deveBuscarTodasAsReservasTrueEFalse() throws Exception{
        mockMvc.perform(get("/api/reserva/status")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    void deveLancarErroSeStatusNaoForBooleano() throws Exception{
        mockMvc.perform(get("/api/reserva/status")
                        .param("ativo", "abc")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("O valor do campo ativo deve ser do tipo Boolean"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/reserva/status"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void develCancelarReserva() throws Exception{
        mockMvc.perform(patch("/api/reserva/desativar/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveLancarErroSeReservaNaoExistirParaDesativar() throws Exception{
        mockMvc.perform(patch("/api/reserva/desativar/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("A Reserva com id: 99 não foi encontrada"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/reserva/desativar/99"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveAlterarReserva() throws Exception{

        ReservaUpdateRequestDTO reservaUpdateRequestDTO = ReservaFixture.criarUpdateRequestDto(1L);

        mockMvc.perform(patch("/api/reserva/atualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaUpdateRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.quantidadeDias").value(3))
                .andExpect(jsonPath("$.observacao").value("Teste observacao update"));
    }

    @Test
    void deveLancarErroSeIdUrlDiferenteDeIdCorpo() throws Exception{
        ReservaUpdateRequestDTO reservaUpdateRequestDTO = ReservaFixture.criarUpdateRequestDto(1L);

        mockMvc.perform(patch("/api/reserva/atualizar/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaUpdateRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("O id da url: 2 é diferente do id do corpo: 1"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/reserva/atualizar/2"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveLancarErroSeReservaNaoExistirParaAtualizar() throws Exception{
        ReservaUpdateRequestDTO reservaUpdateRequestDTO = ReservaFixture.criarUpdateRequestDto(99L);

        mockMvc.perform(patch("/api/reserva/atualizar/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaUpdateRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("A Reserva com id: 99 não foi encontrada"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/reserva/atualizar/99"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}