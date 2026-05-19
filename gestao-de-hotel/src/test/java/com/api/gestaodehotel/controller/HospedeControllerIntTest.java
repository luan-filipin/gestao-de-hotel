package com.api.gestaodehotel.controller;

import com.api.gestaodehotel.domain.enums.Nacionalidade;
import com.api.gestaodehotel.dto.request.HospedeRequestDTO;
import com.api.gestaodehotel.dto.request.HospedeUpdateRequestDTO;
import com.api.gestaodehotel.fixture.HospedeFixture;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@DataSet(value = "datasets/hospede.xml")
class HospedeControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarHospede() throws Exception{

        HospedeRequestDTO hospedeRequestDTO = HospedeFixture.criarHospedeDTO("52998224725");
        mockMvc.perform(post("/api/hospede/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hospedeRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cpf").value("52998224725"));
    }

    @Test
    void deveLancarErroSeHospedeJaExistir() throws Exception{
        HospedeRequestDTO hospedeRequestDTO = HospedeFixture.criarHospedeDTO("12345778912");
        mockMvc.perform(post("/api/hospede/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hospedeRequestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem").value("Ja existe um hospede com esse cpf: 12345778912"));
    }

    @Test
    void deveLancarErroSeRequisicaoNaoForValida() throws Exception{
        mockMvc.perform(post("/api/hospede/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveBuscarHospedePeloCpf() throws Exception{

        mockMvc.perform(get("/api/hospede/cpf/12345778912"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value("12345778912"));
    }

    @Test
    void deveLancarErroSeHospedeNaoExistir() throws Exception{
        mockMvc.perform(get("/api/hospede/cpf/12345778992"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Não existe um hospede com esse cpf: 12345778992"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/hospede/cpf/12345778992"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveLancarErroSeCpfNaoForNumerico() throws Exception{
        mockMvc.perform(get("/api/hospede/cpf/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("CPF deve conter apenas 11 dígitos numéricos"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/hospede/cpf/abc"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveBuscarTodosOsHospedes() throws Exception{
        mockMvc.perform(get("/api/hospede/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void deveBuscarTodosOsHospedeComStatusTrue() throws Exception{
        mockMvc.perform(get("/api/hospede/status").param("ativo", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].cpf").value("12345778912"))
                .andExpect(jsonPath("$[0].ativo").value(true))
                .andExpect(jsonPath("$[1].cpf").value("81020839008"))
                .andExpect(jsonPath("$[1].ativo").value(true));
    }

    @Test
    void deveBuscarTodosOsHospedeComStatusFalse() throws Exception{
        mockMvc.perform(get("/api/hospede/status").param("ativo", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].cpf").value("12345673912"))
                .andExpect(jsonPath("$[0].ativo").value(false));
    }

    @Test
    void deveLancarErroSeStatusNaoForBooleano() throws Exception{
        mockMvc.perform(get("/api/hospede/status").param("ativo", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("O valor do campo ativo deve ser do tipo Boolean"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/hospede/status"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveDesativarHospede() throws Exception{
        mockMvc.perform(patch("/api/hospede/desativar/cpf/12345778912"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveLancarErroSeHospedeNaoExistirParaDesativar() throws Exception{
        mockMvc.perform(patch("/api/hospede/desativar/cpf/12345778917"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Não existe um hospede com esse cpf: 12345778917"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/hospede/desativar/cpf/12345778917"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveLancarErroSeHospedeJaEstiverDesativado() throws Exception{
        mockMvc.perform(patch("/api/hospede/desativar/cpf/12345673912"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("O hospede com cpf: 12345673912 ja esta inativo"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/hospede/desativar/cpf/12345673912"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void develancarErroSeCpfNaoForNumericoParaDesativar() throws Exception{
        mockMvc.perform(patch("/api/hospede/desativar/cpf/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("CPF deve conter apenas 11 dígitos numéricos"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/hospede/desativar/cpf/abc"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveAtualizarHospede() throws Exception{
        HospedeUpdateRequestDTO hospedeUpdateRequestDTO = HospedeFixture.criarUpdateRequestDTO(
                "Luan",
                "45879658241",
                "",
                "luan@gmail.com",
                LocalDate.of(1992, 5, 2),
                Nacionalidade.ESTRANGEIRO,
                ""
        );
        mockMvc.perform(patch("/api/hospede/atualizar/12345778912")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hospedeUpdateRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Luan"))
                .andExpect(jsonPath("$.email").value("luan@gmail.com"))
                .andExpect(jsonPath("$.dataNascimento").value("1992-05-02"))
                .andExpect(jsonPath("$.nacionalidade").value("ESTRANGEIRO"));
    }

    @Test
    void deveLancarErroSeCpfNaoForNumericoParaAtualiar() throws Exception{
        HospedeUpdateRequestDTO hospedeUpdateRequestDTO = HospedeFixture.criarUpdateRequestDTO(
                "Luan",
                "45879658241",
                "",
                "luan@gmail.com",
                LocalDate.of(1992, 5, 2),
                Nacionalidade.ESTRANGEIRO,
                ""
        );

        mockMvc.perform(patch("/api/hospede/atualizar/abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hospedeUpdateRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("CPF deve conter apenas 11 dígitos numéricos"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/hospede/atualizar/abc"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
