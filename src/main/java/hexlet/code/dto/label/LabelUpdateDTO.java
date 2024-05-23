package hexlet.code.dto.label;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabelUpdateDTO {

    @Size(min = 3, max = 1000)
    @NotNull
    @NotBlank
    private JsonNullable<String> name;
}
