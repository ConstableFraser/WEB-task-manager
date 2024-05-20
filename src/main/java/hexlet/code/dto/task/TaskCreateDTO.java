package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskCreateDTO {
    private int index;

    @JsonAlias("assignee_id")
    private Long assigneeId;

    private List<Long> taskLabelIds;

    @NotBlank
    @Size(min = 1)
    private String title;

    private String content;

    @NotNull
    private String status;
}
