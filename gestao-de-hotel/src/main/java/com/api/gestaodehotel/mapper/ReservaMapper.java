package com.api.gestaodehotel.mapper;

import com.api.gestaodehotel.domain.Reserva;
import com.api.gestaodehotel.dto.response.ReservaResponseDTO;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ReservaMapper {
    ReservaResponseDTO toDTO(Reserva entity);

    default Page<ReservaResponseDTO> toDTOReservas(Page<Reserva> entity){
        return entity.map(this::toDTO);
    }
}
