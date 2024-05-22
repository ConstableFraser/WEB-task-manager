package hexlet.code.dto.status;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class StatusDTO {
    private Long id;
    private String name;
    private String slug;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
}
