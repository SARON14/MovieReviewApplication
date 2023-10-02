package et.com.movieReview;

import et.com.movieReview.constants.ApiMessages;
import et.com.movieReview.dto.RequestDto.ReviewRequestDto;
import et.com.movieReview.dto.ResponseDto.ReviewAddResponse;
import et.com.movieReview.dto.ResponseDto.ReviewResponse;
import et.com.movieReview.dto.ResponseDto.ReviewResponseDto;
import et.com.movieReview.exception.NotFoundException;
import et.com.movieReview.model.Movie;
import et.com.movieReview.model.Review;
import et.com.movieReview.model.User;
import et.com.movieReview.repository.MovieRepository;
import et.com.movieReview.repository.ReviewRepository;
import et.com.movieReview.repository.UserRepository;
import et.com.movieReview.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
    @Captor
    private ArgumentCaptor<Review> reviewCaptor;

    @BeforeEach
    public void setUp() {
        reviewService = new ReviewService(reviewRepository, userRepository, movieRepository);
    }



    @Test
    public void testAddReview_ValidPayload() {
        // Create a sample ReviewRequestDto
        ReviewRequestDto requestDto = new ReviewRequestDto();
        requestDto.setUserId(1L);
        requestDto.setMovieId(1L);
        requestDto.setRating(8);
        requestDto.setComment("Great movie!");

        // Create a sample User
        User user = new User();
        user.setId(1L);

        // Create a sample Movie
        Movie movie = new Movie();
        movie.setId(1L);

        // Mock the userRepository.findById method to return the sample User
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Mock the movieRepository.findById method to return the sample Movie
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        // Call the addReview method
        ReviewAddResponse response = reviewService.addReview(requestDto);

        // Assert the response
        assertEquals("success", response.getStatus());
        assertNotNull(response.getReviewId());
    }

    @Test
    public void testAddReview_UserNotFound() {
        // Create a sample ReviewRequestDto
        ReviewRequestDto requestDto = new ReviewRequestDto();
        requestDto.setUserId(1L);
        requestDto.setMovieId(1L);
        requestDto.setRating(8);
        requestDto.setComment("Great movie!");

        // Mock the userRepository.findById method to return an empty Optional
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the addReview method and assert that it throws an exception
        assertThrows(NotFoundException.class, () -> reviewService.addReview(requestDto));
    }

    @Test
    public void testAddReview_MovieNotFound() {
        // Create a sample ReviewRequestDto
        ReviewRequestDto requestDto = new ReviewRequestDto();
        requestDto.setUserId(1L);
        requestDto.setMovieId(1L);
        requestDto.setRating(8);
        requestDto.setComment("Great movie!");

        // Create a sample User
        User user = new User();
        user.setId(1L);

        // Mock the userRepository.findById method to return the sample User
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Mock the movieRepository.findById method to return an empty Optional
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the addReview method and assert that it throws an exception
        assertThrows(NotFoundException.class, () -> reviewService.addReview(requestDto));
    }

    @Test
    public void testAddReview_InvalidRating() {
        // Create a sample ReviewRequestDto with an invalid rating
        ReviewRequestDto requestDto = new ReviewRequestDto();
        requestDto.setUserId(1L);
        requestDto.setMovieId(1L);
        requestDto.setRating(15);
        requestDto.setComment("Invalid rating!");

        // Call the addReview method
        ReviewAddResponse response = reviewService.addReview(requestDto);

        // Assert the response
        assertEquals("failed rating must be between 1 - 10", response.getStatus());
        assertNull(response.getReviewId());
    }
    @Test
    public void testGetReviewByUserId_UserFound() {
        // Create a sample User
        User user = new User();
        user.setId(1L);

        // Create a sample Review
        Review review = new Review();
        review.setUserId(1L);
        review.setMovieId(1L);
        review.setRating(8);
        review.setComment("Sample comment");

        // Mock the userRepository.findById method to return the sample User
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Mock the reviewRepository.findAllByUserId method to return a list containing the sample Review
        when(reviewRepository.findAllByUserId(1L)).thenReturn(List.of(review));

        // Call the getReviewByUserId method
        ReviewResponse response = reviewService.getReviewByUserId(1L);

        // Assert the response
        assertEquals("success", response.getStatus());
        assertEquals(1, response.getTotalResult());
        assertEquals(1, response.getReviewList().size());

        ReviewResponseDto reviewResponseDto = response.getReviewList().get(0);
        assertEquals(1L, reviewResponseDto.getUserId());
        assertEquals(1L, reviewResponseDto.getMovieId());
        assertEquals(8, reviewResponseDto.getRating());
        assertEquals("Sample comment", reviewResponseDto.getComment());
    }

    @Test
    public void testGetReviewByUserId_UserNotFound() {
        // Mock the userRepository.findById method to return an empty Optional
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the getReviewByUserId method and assert that it throws an exception
        assertThrows(NotFoundException.class, () -> reviewService.getReviewByUserId(1L));
    }

    @Test
    public void testGetReviewByUserId_NoReviews() {
        // Create a sample User
        User user = new User();
        user.setId(1L);

        // Mock the userRepository.findById method to return the sample User
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Mock the reviewRepository.findAllByUserId method to return an empty list
        when(reviewRepository.findAllByUserId(1L)).thenReturn(new ArrayList<>());

        // Call the getReviewByUserId method
        ReviewResponse response = reviewService.getReviewByUserId(1L);

        // Assert the response
        assertEquals("success", response.getStatus());
        assertEquals(0, response.getTotalResult());
        assertEquals(0, response.getReviewList().size());
    }
}