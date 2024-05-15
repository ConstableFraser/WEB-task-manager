package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskUpdateDTO {
    private int index;
    private Long assigneeId;

    @NotBlank
    @Size(min = 1)
    private String title;
    private String content;

    @NotNull
    private Long statusId;
}
