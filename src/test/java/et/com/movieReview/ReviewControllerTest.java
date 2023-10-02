package et.com.movieReview;

import et.com.movieReview.controller.ReviewController;
import et.com.movieReview.dto.RequestDto.ReviewRequestDto;
import et.com.movieReview.dto.ResponseDto.ReviewAddResponse;
import et.com.movieReview.dto.ResponseDto.ReviewResponse;
import et.com.movieReview.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {

    private final Fixture fixture = new Fixture();
    @Mock
    private ReviewService reviewService;
    @InjectMocks
    private ReviewController reviewController;

    @Test
    void given_whenAddReview_thenReturnSuccess() {
        when(reviewService.addReview(fixture.getReviewRequestDto())).thenReturn(fixture.getReviewAddResponse());

        ReviewAddResponse response = reviewController.addReview(fixture.getReviewRequestDto());

        assertEquals("success", response.getStatus());
    }
    @Test
    void get_WhenGetReviewByUserId_thenReturnGetReviewByUserId(){
        when(reviewService.getReviewByUserId(1L)).thenReturn(fixture.getReviewResponse());
        ReviewResponse response = reviewController.getReviewByUserId(1L);
        assertEquals("success",response.getStatus());
    }

    private static class Fixture {
        ReviewRequestDto getReviewRequestDto() {
            return ReviewRequestDto.builder()
                    .comment("Great movie!")
                    .movieId(1L)
                    .rating(8)
                    .userId(1L)
                    .build();
        }
        ReviewAddResponse getReviewAddResponse() {
            return ReviewAddResponse.builder()
                    .status("success")
                    .reviewId(1L)
                    .build();
        }
        ReviewResponse getReviewResponse(){
            return ReviewResponse.builder()
                    .totalResult(1)
                    .status("success")
                    .reviewList(new ArrayList<>(0))
                    .build();
        }
    }
}
