package et.com.movieReview.service;

import et.com.movieReview.constants.ApiMessages;
import et.com.movieReview.dto.RequestDto.ReviewRequestDto;
import et.com.movieReview.dto.ResponseDto.ResponseDTO;
import et.com.movieReview.dto.ResponseDto.ReviewResponse;
import et.com.movieReview.dto.ResponseDto.ReviewResponseDto;
import et.com.movieReview.exception.NotFoundException;
import et.com.movieReview.model.Movie;
import et.com.movieReview.model.Review;
import et.com.movieReview.model.User;
import et.com.movieReview.repository.MovieRepository;
import et.com.movieReview.repository.ReviewRepository;
import et.com.movieReview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ApiMessages apiMessages = new ApiMessages();

    public ResponseDTO<?> addReview(ReviewRequestDto payload) {
        Optional<User> user = userRepository.findById(payload.getUserId());
        Optional<Movie> movie = movieRepository.findById(payload.getMovieId());
        if (user.isEmpty() && movie.isEmpty()){
            return apiMessages.errorMessage("record doesn't exist");
        }
        if(!validateRating(payload.getRating())){
            return apiMessages.errorMessage("rating must between 1 and 10");
        }
        return apiMessages.successMessageWithData(reviewRepository.save(Review.builder()
                        .userId(payload.getUserId())
                        .movieId(payload.getMovieId())
                        .rating(payload.getRating())
                        .comment(payload.getComment())
                        .build()));
    }
    public boolean validateRating(int rating) {
        return rating >= 1 && rating <= 10;
    }

    public ResponseDTO<?> getReviewByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundException("user not found"));
        List<Review> reviewList = reviewRepository.findAllByUserId(userId);
        List<ReviewResponseDto> reviewArrayList = new ArrayList<>();
        reviewList.forEach(review -> {
            ReviewResponseDto reviewResponseDto = ReviewResponseDto.builder()
                    .userId(review.getUserId())
                    .movieId(review.getMovieId())
                    .rating(review.getRating())
                    .comment(review.getComment())
                    .build();
            reviewArrayList.add(reviewResponseDto);
        });
        ReviewResponse reviewResponse = ReviewResponse.builder()
                .reviewList(reviewArrayList)
                .totalResult(null)
                .status("success")
                .build();
        return apiMessages.successMessageWithData(reviewResponse);
    }
}
