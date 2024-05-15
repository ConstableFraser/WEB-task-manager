package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusCreateDTO {

    @Size(min = 1)
    @NotNull
    @NotBlank
    private String name;

    @Size(min = 1)
    @NotNull
    @NotBlank
    private String slug;
}
