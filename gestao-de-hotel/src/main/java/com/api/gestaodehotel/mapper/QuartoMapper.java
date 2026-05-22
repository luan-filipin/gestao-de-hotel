package com.api.gestaodehotel.mapper;

import com.api.gestaodehotel.domain.Quarto;
import com.api.gestaodehotel.dto.request.QuartoRequestDTO;
import com.api.gestaodehotel.dto.request.QuartoUpdateRequestDTO;
import com.api.gestaodehotel.dto.response.QuartoResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuartoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", constant = "true")
    Quarto toEntity(QuartoRequestDTO dto);
    QuartoResponseDTO toDTO(Quarto entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", constant = "true")
    List<Quarto> toEntityList(List<QuartoRequestDTO> dtos);
    List<QuartoResponseDTO> toListResponse(List<Quarto> entity);

    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "ativo" , constant = "true")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(QuartoUpdateRequestDTO dto, @MappingTarget Quarto entity);
}
