package com.api.gestaodehotel.controller;

import com.api.gestaodehotel.domain.enums.TipoQuarto;
import com.api.gestaodehotel.dto.request.QuartoRequestDTO;
import com.api.gestaodehotel.dto.request.QuartoUpdateRequestDTO;
import com.api.gestaodehotel.fixture.QuartoFixture;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@DataSet("datasets/quarto.xml")
class QuartoControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarQuarto() throws Exception{
        QuartoRequestDTO quartoRequestDTO = QuartoFixture.criarRequestDTO(4);
        mockMvc.perform(post("/api/quarto/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(quartoRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroQuarto").value(4));
    }

    @Test
    void deveLancarExceptionQuandoQuartoJaExistir() throws Exception{
        QuartoRequestDTO quartoRequestDTO = QuartoFixture.criarRequestDTO(1);
        mockMvc.perform(post("/api/quarto/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(quartoRequestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem").value("Ja existe um quarto com esse numero: 1"))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.path").value("/api/quarto/criar"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveLancarErroSeRequisicaoNaoForValida() throws Exception{
        mockMvc.perform(post("/api/quarto/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveLancarErroSeNumeroQuartoEstiverVazio() throws Exception{
        QuartoRequestDTO quartoRequestDTO = QuartoFixture.criarRequestDTO(null);
        mockMvc.perform(post("/api/quarto/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(quartoRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Campos Inválidos"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/quarto/criar"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.erros[0].campo").value("numeroQuarto"))
                .andExpect(jsonPath("$.erros[0].mensagem").value("O numero do quarto é obrigatorio."));
    }

    @Test
    void deveBuscarQuartoPeloNumero() throws Exception{
        mockMvc.perform(get("/api/quarto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroQuarto").value(1));
    }

    @Test
    void deveLancarErroSeNumeroQuartoNaoExistir() throws Exception{
        mockMvc.perform(get("/api/quarto/1000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("O quarto 1000 não foi encontrado"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/quarto/1000"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveLancarErroSeNumeroQuartoNaoForNumerico() throws Exception{
        mockMvc.perform(get("/api/quarto/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("O valor do campo numeroQuarto deve ser do tipo Integer"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/quarto/abc"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveLancarErroSeNumeroQuartoEstiverAusente() throws Exception{
        mockMvc.perform(get("/api/quarto/"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Endpoint não encontrado"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/quarto/"))
                .andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    void deveBuscarTodosOsQuartosAtivos() throws Exception{
        mockMvc.perform(get("/api/quarto").param("ativo", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].numeroQuarto").value(1))
                .andExpect(jsonPath("$[0].ativo").value(true))
                .andExpect(jsonPath("$[1].numeroQuarto").value(2))
                .andExpect(jsonPath("$[1].ativo").value(true));
    }

    @Test
    void deveBuscarTodosOsQuartosInativos() throws Exception{
        mockMvc.perform(get("/api/quarto").param("ativo", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].numeroQuarto").value(3))
                .andExpect(jsonPath("$[0].ativo").value(false));
    }

    @Test
    void deveLancarErroSeStatusNaoForBooleano() throws Exception{
        mockMvc.perform(get("/api/quarto").param("ativo", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("O valor do campo ativo deve ser do tipo Boolean"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/quarto"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveBuscarTodosOsQuartosAtivosEInativos() throws Exception{
        mockMvc.perform(get("/api/quarto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[*].ativo", hasItems(true, false)));
    }

    @Test
    void deveDesativarQuarto() throws Exception{
        mockMvc.perform(patch("/api/quarto/desativar/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveLancarErroSeNumeroQuartoEstiverAusenteParaDesativacao() throws Exception {
        mockMvc.perform(patch("/api/quarto/desativar/"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Endpoint não encontrado"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/quarto/desativar/"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveLancarErroQuandoNumeroQuartoNaoForInteger() throws Exception{
        mockMvc.perform(patch("/api/quarto/desativar/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("O valor do campo numeroQuarto deve ser do tipo Integer"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/quarto/desativar/abc"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveAtualizarQuarto() throws Exception{

        QuartoUpdateRequestDTO quartoUpdateRequestDTO = QuartoFixture.criarUpdateRequestDTO(
                TipoQuarto.CASAL,
                2,
                new BigDecimal("380.00"),
                "Teste descricao QuartoUpdateRequestDTO"
        );

        mockMvc.perform(patch("/api/quarto/atualizar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quartoUpdateRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroQuarto").value(1))
                .andExpect(jsonPath("$.tipoQuarto").value("CASAL"))
                .andExpect(jsonPath("$.capacidade").value(2))
                .andExpect(jsonPath("$.precoPorNoite").value("380.0"))
                .andExpect(jsonPath("$.descricao").value("Teste descricao QuartoUpdateRequestDTO"));

    }

    @Test
    void deveLancarErroSeNumeroQuartoNaoExistirParaAtualizar() throws Exception{

        QuartoUpdateRequestDTO quartoUpdateRequestDTO = QuartoFixture.criarUpdateRequestDTO(
                TipoQuarto.CASAL,
                2,
                new BigDecimal("380.00"),
                "Teste descricao QuartoUpdateRequestDTO"
        );

        mockMvc.perform(patch("/api/quarto/atualizar/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(quartoUpdateRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("O quarto 4 não foi encontrado"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/quarto/atualizar/4"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveAtualizarQuartoParcialmente() throws Exception{

        QuartoUpdateRequestDTO quartoUpdateRequestDTO = QuartoFixture.criarUpdateRequestDTO(
                TipoQuarto.CASAL,
                null,
                null,
                "Teste descricao QuartoUpdateRequestDTO"
        );

        mockMvc.perform(patch("/api/quarto/atualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(quartoUpdateRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoQuarto").value("CASAL"))
                .andExpect(jsonPath("$.capacidade").value(1))
                .andExpect(jsonPath("$.precoPorNoite").value("175.0"))
                .andExpect(jsonPath("$.descricao").value("Teste descricao QuartoUpdateRequestDTO"));
    }

    @Test
    void deveLancarErroSeNumeroQuartoNaoForNumericoParaAtualizar() throws Exception{

        QuartoUpdateRequestDTO quartoUpdateRequestDTO = QuartoFixture.criarUpdateRequestDTO(
                TipoQuarto.SOLTEIRO,
                1,
                new BigDecimal("135.00"),
                "Teste descricao QuartoUpdateRequestDTO"
        );

        mockMvc.perform(patch("/api/quarto/atualizar/abc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(quartoUpdateRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("O valor do campo numeroQuarto deve ser do tipo Integer"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/quarto/atualizar/abc"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
