package hexlet.code.mapper;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.StatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "taskStatus", source = "status")
    @Mapping(target = "labels", source = "taskLabelIds")
    public abstract Task map(TaskCreateDTO taskCreateDTO);

    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "status", source = "taskStatus.slug") //.slug
    @Mapping(target = "taskLabelIds", source = "labels")
    public abstract TaskDTO map(Task task);

    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "assigneeId", target = "assignee")
    @Mapping(source = "status", target = "taskStatus")
    @Mapping(target = "labels", source = "taskLabelIds")
    public abstract void update(TaskUpdateDTO taskUpdateDTO, @MappingTarget Task task);

    public TaskStatus mapNameToStatus(String slug) {
        return statusRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Status hot found"));
    }

    public List<Label> mapIdToLabel(List<Long> labels) {
        if (labels == null) {
            return List.of();
        }

        return labels.stream()
                .map((name) -> labelRepository.findById(name)
                        .orElseThrow(() -> new ResourceNotFoundException("Label not found")))
                .toList();
    }

    public List<Long> mapLabelToId(List<Label> labels) {
        if (labels == null) {
            return List.of();
        }

        return labels.stream()
                .map(Label::getId)
                .toList();
    }
}
