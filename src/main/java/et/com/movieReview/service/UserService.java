package et.com.movieReview.service;

import et.com.movieReview.constants.ApiMessages;
import et.com.movieReview.dto.RequestDto.UserRequestDto;
import et.com.movieReview.dto.ResponseDto.UserAddResponse;
import et.com.movieReview.model.User;
import et.com.movieReview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ApiMessages apiMessages = new ApiMessages();

    public UserAddResponse addUser(UserRequestDto payload) {
        User userByName = userRepository.findByUserName(payload.getUserName());
        User userByEmail = userRepository.findByEmail(payload.getEmail());
        UserAddResponse userAddResponse = new UserAddResponse();
        if (userByName != null && userByEmail != null) {
            userAddResponse.setStatus("failed user existed with a provided data");
            userAddResponse.setUserId(null);
            return userAddResponse;
        }
        //check if the username is valid
        if (!isValidUsername(payload.getUserName())) {
            userAddResponse.setStatus("failed invalid username");
            userAddResponse.setUserId(null);
            return userAddResponse;
        }
        User user = User.builder()
                .userName(payload.getUserName())
                .email(payload.getEmail())
                .build();
        userRepository.save(user);
        userAddResponse.setStatus("success");
        userAddResponse.setUserId(user.getId());
        return userAddResponse;
    }
    public static boolean isValidUsername(String name){
        String regex = "^[A-Za-z]\\w{5,29}$";
        Pattern p = Pattern.compile(regex);
        if (name == null) {return false;}
        Matcher m = p.matcher(name);
        return m.matches();
    }
}
