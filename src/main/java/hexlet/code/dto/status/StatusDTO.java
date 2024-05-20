package hexlet.code.dto.status;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class StatusDTO {
    private Long id;
    private String name;
    private String slug;
    private Date createdAt;
}
