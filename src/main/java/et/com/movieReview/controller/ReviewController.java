package et.com.movieReview.controller;

import et.com.movieReview.constants.Endpoints;
import et.com.movieReview.dto.RequestDto.ReviewRequestDto;
import et.com.movieReview.dto.ResponseDto.ReviewAddResponse;
import et.com.movieReview.dto.ResponseDto.ReviewResponse;
import et.com.movieReview.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ReviewController {
    private final ReviewService reviewService;
    private static final String JSON = MediaType.APPLICATION_JSON_VALUE;

    @PostMapping(value = Endpoints.ADD_REVIEW,produces = JSON,consumes = JSON)
    public ReviewAddResponse addReview(@Valid @RequestBody ReviewRequestDto payload){
        return reviewService.addReview(payload);
    }
    @GetMapping(value = Endpoints.GET_REVIEW_BY_USERID,produces = JSON)
    public ReviewResponse getReviewByUserId(@PathVariable Long userId){
        return reviewService.getReviewByUserId(userId);
    }
}
