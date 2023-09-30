package et.com.movieReview;

import et.com.movieReview.constants.ApiMessages;
import et.com.movieReview.dto.RequestDto.ReviewRequestDto;
import et.com.movieReview.dto.ResponseDto.ResponseDTO;
import et.com.movieReview.dto.ResponseDto.ReviewResponse;
import et.com.movieReview.dto.ResponseDto.ReviewResponseDto;
import et.com.movieReview.model.Movie;
import et.com.movieReview.model.Review;
import et.com.movieReview.model.User;
import et.com.movieReview.repository.MovieRepository;
import et.com.movieReview.repository.ReviewRepository;
import et.com.movieReview.repository.UserRepository;
import et.com.movieReview.service.ReviewService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    private ReviewService reviewService;

    @Mock
    private UserRepository userRepository;
    private final ApiMessages apiMessages = new ApiMessages();

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @BeforeEach
    public void setUp() {
        reviewService = new ReviewService(reviewRepository, userRepository, movieRepository);
    }

    @Test
    public void testAddReview_RecordExists() {

        ReviewRequestDto payload = new ReviewRequestDto();
        payload.setUserId(1L);
        payload.setMovieId(1L);
        payload.setRating(8);
        payload.setComment("Great movie!");

        User user = new User();
        Mockito.when(userRepository.findById(payload.getUserId())).thenReturn(Optional.of(user));

        Movie movie = new Movie();
        Mockito.when(movieRepository.findById(payload.getMovieId())).thenReturn(Optional.of(movie));

        Review savedReview = new Review();
        Mockito.when(reviewRepository.save(Mockito.any())).thenReturn(savedReview);

        ResponseDTO<?> response = reviewService.addReview(payload);

        Mockito.verify(userRepository, Mockito.times(1)).findById(payload.getUserId());
        Mockito.verify(movieRepository, Mockito.times(1)).findById(payload.getMovieId());
        Mockito.verify(reviewRepository, Mockito.times(1)).save(Mockito.any());
        Assertions.assertEquals(apiMessages.successMessageWithData(savedReview), response);
    }

    @Test
    public void testAddReview_RecordDoesNotExist() {
        ReviewRequestDto payload = new ReviewRequestDto();
        payload.setUserId(1L);
        payload.setMovieId(1L);
        payload.setRating(8);
        payload.setComment("Great movie!");

        Mockito.when(userRepository.findById(payload.getUserId())).thenReturn(Optional.empty());

        Mockito.when(movieRepository.findById(payload.getMovieId())).thenReturn(Optional.empty());

        ResponseDTO<?> response = reviewService.addReview(payload);

        Mockito.verify(userRepository, Mockito.times(1)).findById(payload.getUserId());
        Mockito.verify(movieRepository, Mockito.times(1)).findById(payload.getMovieId());
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
        Assertions.assertEquals(apiMessages.errorMessage("record doesn't exist"), response);
    }

    @Test
    public void testAddReview_InvalidRating() {
        // Arrange
        ReviewRequestDto payload = new ReviewRequestDto();
        payload.setUserId(1L);
        payload.setMovieId(1L);
        payload.setRating(15);
        payload.setComment("Great movie!");

        User user = new User();
        Mockito.when(userRepository.findById(payload.getUserId())).thenReturn(Optional.of(user));

        Movie movie = new Movie();
        Mockito.when(movieRepository.findById(payload.getMovieId())).thenReturn(Optional.of(movie));

        ResponseDTO<?> response = reviewService.addReview(payload);

        Mockito.verify(userRepository, Mockito.times(1)).findById(payload.getUserId());
        Mockito.verify(movieRepository, Mockito.times(1)).findById(payload.getMovieId());
        Mockito.verify(reviewRepository, Mockito.never()).save(Mockito.any());
        Assertions.assertEquals(apiMessages.errorMessage("rating must be between 1 and 10"), response);
    }
    @Test
    public void testGetReviewByUserId_UserExists() {
        Long userId = 1L;

        User user = new User();
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        List<Review> reviewList = new ArrayList<>();
        Review review1 = new Review();
        review1.setUserId(userId);
        review1.setMovieId(1L);
        review1.setRating(8);
        review1.setComment("Great movie!");
        reviewList.add(review1);

        Review review2 = new Review();
        review2.setUserId(userId);
        review2.setMovieId(2L);
        review2.setRating(6);
        review2.setComment("Average movie");
        reviewList.add(review2);

        Mockito.when(reviewRepository.findAllByUserId(userId)).thenReturn(reviewList);

        ResponseDTO<?> response = reviewService.getReviewByUserId(userId);

        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Mockito.verify(reviewRepository, Mockito.times(1)).findAllByUserId(userId);

        ReviewResponse expectedReviewResponse = ReviewResponse.builder()
                .reviewList(List.of(
                        ReviewResponseDto.builder()
                                .userId(userId)
                                .movieId(1L)
                                .rating(8)
                                .comment("Great movie!")
                                .build(),
                        ReviewResponseDto.builder()
                                .userId(userId)
                                .movieId(2L)
                                .rating(6)
                                .comment("Average movie")
                                .build()
                ))
                .totalResult(null)
                .status("success")
                .build();

        Assertions.assertEquals(apiMessages.successMessageWithData(expectedReviewResponse), response);
    }

    @Test
    public void testGetReviewByUserId_UserNotFound() {
        Long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseDTO<?> response = reviewService.getReviewByUserId(userId);

        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Mockito.verify(reviewRepository, Mockito.never()).findAllByUserId(Mockito.anyLong());

        Assertions.assertEquals(apiMessages.errorMessage("user not found"), response);
    }
}