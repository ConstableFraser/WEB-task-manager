package hexlet.code.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserUpdateDTO {
    private String firstName;
    private String lastName;
}
