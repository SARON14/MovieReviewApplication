package et.com.movieReview.controller;

import et.com.movieReview.config.DateEntity;
import et.com.movieReview.constants.Endpoints;
import et.com.movieReview.dto.RequestDto.UserRequestDto;
import et.com.movieReview.dto.ResponseDto.UserAddResponse;
import et.com.movieReview.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController extends DateEntity {
    private final UserService customerService;
    private static final String JSON = MediaType.APPLICATION_JSON_VALUE;

    @PostMapping(value = Endpoints.ADD_USER,produces = JSON,consumes = JSON)
    public UserAddResponse addUser(@Valid @RequestBody UserRequestDto payload){
        return customerService.addUser(payload);
    }
}
