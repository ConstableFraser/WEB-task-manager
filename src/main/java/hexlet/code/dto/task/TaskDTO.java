package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private int index;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private String title;

    private String content;
    private String status;

    private List<Long> taskLabelIds;
}
