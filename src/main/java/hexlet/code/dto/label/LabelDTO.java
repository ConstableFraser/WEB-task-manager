package hexlet.code.dto.label;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LabelDTO {
    private Long id;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
}
