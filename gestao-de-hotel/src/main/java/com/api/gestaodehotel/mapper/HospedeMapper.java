package com.api.gestaodehotel.mapper;

import com.api.gestaodehotel.domain.Hospede;
import com.api.gestaodehotel.dto.request.HospedeRequestDTO;
import com.api.gestaodehotel.dto.request.HospedeUpdateRequestDTO;
import com.api.gestaodehotel.dto.response.HospedeResponseDTO;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface HospedeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", constant = "true")
    @Mapping(target = "dataCadastro", ignore = true)
    Hospede toEntity(HospedeRequestDTO dto);
    HospedeResponseDTO toDTO(Hospede entity);

    default Page<HospedeResponseDTO> toPageResponse(Page<Hospede> entity){
        return entity.map(this::toDTO);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", constant = "true")
    @Mapping(target = "dataCadastro", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(HospedeUpdateRequestDTO dto, @MappingTarget Hospede entity);
}
