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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", constant = "true")
    List<Quarto> toEntityList(List<QuartoRequestDTO> dtos);
    List<QuartoResponseDTO> toResponseList(List<Quarto> entity);

    QuartoResponseDTO toResponse(Quarto entity);

    List<QuartoResponseDTO> toListResponse(List<Quarto> entity);

    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "ativo" , constant = "true")
    @Mapping(target = "numeroQuarto", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(QuartoUpdateRequestDTO dto, @MappingTarget Quarto entity);
}
