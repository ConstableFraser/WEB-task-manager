package hexlet.code.dto.task;

import org.openapitools.jackson.nullable.JsonNullable;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskUpdateDTO {
    private JsonNullable<Integer> index;

    @JsonAlias("assignee_id")
    private JsonNullable<Long> assigneeId;

    private JsonNullable<List<Long>> taskLabelIds;

    @NotBlank
    @Size(min = 1)
    private JsonNullable<String> title;

    private JsonNullable<String> content;

    @NotNull
    private JsonNullable<String> status;
}
