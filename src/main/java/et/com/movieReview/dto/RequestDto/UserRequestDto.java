package et.com.movieReview.dto.RequestDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class UserRequestDto {
    private String userName;
    @Email(message = "Email should be valid")
    private String email;
}
