package et.com.movieReview.controller;

import et.com.movieReview.constants.Endpoints;
import et.com.movieReview.dto.ResponseDto.ResponseDTO;
import et.com.movieReview.dto.RequestDto.ReviewRequestDto;
import et.com.movieReview.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ReviewController {
    private final ReviewService reviewService;
    private static final String JSON = MediaType.APPLICATION_JSON_VALUE;

    @PostMapping(value = Endpoints.ADD_REVIEW,produces = JSON,consumes = JSON)
    public ResponseDTO<?> addReview(@Valid @RequestBody ReviewRequestDto payload){
        return reviewService.addReview(payload);
    }
//    @GetMapping(value = Endpoints.GET_ADOPTION,produces = JSON)
//    public ResponseDTO<?> getAdoption(){
//        return adoptService.getAdoption();
//    }
}
