package hexlet.code.dto.status;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class StatusUpdateDTO {

    @NotBlank
    @Size(min = 1)
    private JsonNullable<String> name;

    @NotBlank
    @Size(min = 1)
    private JsonNullable<String> slug;
}
