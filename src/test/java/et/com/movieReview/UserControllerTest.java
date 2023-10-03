package et.com.movieReview;

import et.com.movieReview.controller.UserController;
import et.com.movieReview.dto.RequestDto.UserRequestDto;
import et.com.movieReview.dto.ResponseDto.UserAddResponse;
import et.com.movieReview.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final Fixture fixture = new Fixture();

    @Test
    void given_whenAddUser_thenReturnSuccess() {
        when(userService.addUser(any())).thenReturn(fixture.getUserAddResponse());
        UserAddResponse response = userController.addUser(fixture.getUserRequestDto());
        assertEquals("success", response.getStatus());
    }
    private static class Fixture {
        UserRequestDto getUserRequestDto(){
            return UserRequestDto.builder()
                    .userName("fasil")
                    .email("fasil@gmail.com")
                    .build();
        }
        UserAddResponse getUserAddResponse(){
            return UserAddResponse.builder()
                    .status("success")
                    .userId(1L)
                    .build();
        }
    }
}
