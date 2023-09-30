package et.com.movieReview;

import et.com.movieReview.dto.RequestDto.UserRequestDto;
import et.com.movieReview.dto.ResponseDto.ResponseDTO;
import et.com.movieReview.model.User;
import et.com.movieReview.repository.UserRepository;
import et.com.movieReview.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    public void setUp() {
    }

    @Test
    void testAddUser_WhenUserDoesNotExist_ReturnsSuccessResponse() {
        UserRequestDto payload = new UserRequestDto();
        payload.setUserName("john_doe");
        payload.setEmail("john.doe@example.com");

        when(userRepository.findByUserName(payload.getUserName())).thenReturn(null);
        when(userRepository.findByEmail(payload.getEmail())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(new User());

        ResponseDTO<?> response = userService.addUser(payload);

        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getData());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testAddUser_WhenUserExistsWithProvidedData_ReturnsErrorResponse() {

        UserRequestDto payload = new UserRequestDto();
        payload.setUserName("john_doe");
        payload.setEmail("john.doe@example.com");

        when(userRepository.findByUserName(payload.getUserName())).thenReturn(new User());
        when(userRepository.findByEmail(payload.getEmail())).thenReturn(new User());

        ResponseDTO<?> response = userService.addUser(payload);

        assertNotNull(response);
        assertEquals("error", response.getStatus());
        assertEquals("user existed with a provided data", response.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAddUser_WhenInvalidUsername_ReturnsErrorResponse() {

        UserRequestDto payload = new UserRequestDto();
        payload.setUserName("john doe"); // Invalid username
        payload.setEmail("john.doe@example.com");

        ResponseDTO<?> response = userService.addUser(payload);

        assertNotNull(response);
        assertEquals("error", response.getStatus());
        assertEquals("invalid username", response.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
