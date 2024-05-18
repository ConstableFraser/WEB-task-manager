package hexlet.code.mapper;

import hexlet.code.model.TaskStatus;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapper;

import hexlet.code.dto.StatusCreateDTO;
import hexlet.code.dto.StatusUpdateDTO;
import hexlet.code.dto.StatusDTO;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class StatusMapper {

    public abstract TaskStatus map(StatusCreateDTO statusCreateDTO);
    public abstract StatusDTO map(TaskStatus taskStatus);
    public abstract TaskStatus update(StatusUpdateDTO statusUpdateDTO, @MappingTarget TaskStatus taskStatus);
}
