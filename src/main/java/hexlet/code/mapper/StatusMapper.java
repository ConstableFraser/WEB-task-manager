package hexlet.code.mapper;

import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapper;

import hexlet.code.dto.StatusCreateDTO;
import hexlet.code.dto.StatusUpdateDTO;
import hexlet.code.dto.StatusDTO;

import hexlet.code.model.Status;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class StatusMapper {

    public abstract Status map(StatusCreateDTO statusCreateDTO);
    public abstract StatusDTO map(Status status);
    public abstract Status update(StatusUpdateDTO statusUpdateDTO, @MappingTarget Status status);
}
