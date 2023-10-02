package et.com.movieReview;

import et.com.movieReview.dto.RequestDto.UserRequestDto;
import et.com.movieReview.dto.ResponseDto.UserAddResponse;
import et.com.movieReview.model.User;
import et.com.movieReview.repository.UserRepository;
import et.com.movieReview.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    public void setUp() {
    }

    @Test
    public void testAddUser_ValidPayload() {
        // Create a sample UserRequestDto
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setUserName("john_doe");
        requestDto.setEmail("john@example.com");

        // Mock the userRepository.findByUserName method to return null
        when(userRepository.findByUserName("john_doe")).thenReturn(null);

        // Mock the userRepository.findByEmail method to return null
        when(userRepository.findByEmail("john@example.com")).thenReturn(null);

        // Call the addUser method
        UserAddResponse response = userService.addUser(requestDto);

        // Assert the response
        assertEquals("success", response.getStatus());
        assertNotNull(response.getUserId());
    }

    @Test
    public void testAddUser_UserExistsWithSameUsernameAndEmail() {
        // Create a sample UserRequestDto
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setUserName("john_doe");
        requestDto.setEmail("john@example.com");

        // Create a sample User with the same username and email
        User existingUser = new User();
        existingUser.setUserName("john_doe");
        existingUser.setEmail("john@example.com");

        // Mock the userRepository.findByUserName method to return the sample User
        when(userRepository.findByUserName("john_doe")).thenReturn(existingUser);

        // Mock the userRepository.findByEmail method to return the sample User
        when(userRepository.findByEmail("john@example.com")).thenReturn(existingUser);

        // Call the addUser method
        UserAddResponse response = userService.addUser(requestDto);

        // Assert the response
        assertEquals("failed user existed with a provided data", response.getStatus());
        assertNull(response.getUserId());
    }

    @Test
    public void testAddUser_InvalidUsername() {
        // Create a sample UserRequestDto with an invalid username
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setUserName("john doe");
        requestDto.setEmail("john@example.com");

        // Call the addUser method
        UserAddResponse response = userService.addUser(requestDto);

        // Assert the response
        assertEquals("failed invalid username", response.getStatus());
        assertNull(response.getUserId());
    }

}
