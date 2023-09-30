package et.com.movieReview.service;

import et.com.movieReview.constants.ApiMessages;
import et.com.movieReview.dto.ResponseDto.ResponseDTO;
import et.com.movieReview.dto.RequestDto.UserRequestDto;
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

    public ResponseDTO<?> addUser(UserRequestDto payload) {
        User userByName = userRepository.findByUserName(payload.getUserName());
        User userByEmail = userRepository.findByEmail(payload.getEmail());
        if(userByName != null && userByEmail != null){
            return apiMessages.errorMessage("user existed with a provided data");
        }
        //check if the username is valid
        if(!isValidUsername(payload.getUserName())){
            return apiMessages.errorMessage(" invalid username");
        }
        return apiMessages.successMessageWithData(userRepository.save(User.builder()
                        .userName(payload.getUserName())
                        .email(payload.getEmail())
                        .build()));
    }
    public static boolean isValidUsername(String name){
        String regex = "^[A-Za-z]\\w{5,29}$";
        Pattern p = Pattern.compile(regex);
        if (name == null) {return false;}
        Matcher m = p.matcher(name);
        return m.matches();
    }
}
