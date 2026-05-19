package com.api.gestaodehotel.mapper;

import com.api.gestaodehotel.domain.Hospede;
import com.api.gestaodehotel.dto.request.HospedeRequestDTO;
import com.api.gestaodehotel.dto.request.HospedeUpdateRequestDTO;
import com.api.gestaodehotel.dto.response.HospedeResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HospedeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", constant = "true")
    @Mapping(target = "dataCadastro", ignore = true)
    Hospede toEntity(HospedeRequestDTO dto);
    HospedeResponseDTO toDTO(Hospede entity);

    List<HospedeResponseDTO> toResponseList(List<Hospede> entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cpf", ignore = true)
    @Mapping(target = "ativo", constant = "true")
    @Mapping(target = "dataCadastro", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(HospedeUpdateRequestDTO dto, @MappingTarget Hospede entity);
}
