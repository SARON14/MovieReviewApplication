package et.com.movieReview.service;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ApiMessages apiMessages = new ApiMessages();

    public ReviewAddResponse addReview(ReviewRequestDto payload) {
        User user = userRepository.findById(payload.getUserId())
                .orElseThrow(()->new NotFoundException("User not found"));
        Movie movie = movieRepository.findById(payload.getMovieId())
                .orElseThrow(()->new NotFoundException("Movie not found"));
        ReviewAddResponse reviewAddResponse = new ReviewAddResponse();
        if (!validateRating(payload.getRating())) {
            reviewAddResponse.setStatus("failed rating must be between 1 - 10");
            reviewAddResponse.setReviewId(null);
            return reviewAddResponse;
        }
        Review review = Review.builder()
                .userId(payload.getUserId())
                .movieId(payload.getMovieId())
                .rating(payload.getRating())
                .comment(payload.getComment())
                .build();
        reviewRepository.save(review);
        reviewAddResponse.setStatus("success");
        reviewAddResponse.setReviewId(review.getId());
        return reviewAddResponse;
    }
    public boolean validateRating(int rating) {
        return rating >= 1 && rating <= 10;
    }

    public ReviewResponse getReviewByUserId(Long userId,Integer page,Integer pageSize) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundException("user not found"));
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Review> reviewPage = reviewRepository.findAllByUserId(userId,pageable);
        List<ReviewResponseDto> reviewArrayList = new ArrayList<>();
        reviewPage.forEach(review -> {
            ReviewResponseDto reviewResponseDto = ReviewResponseDto.builder()
                    .userId(review.getUserId())
                    .movieId(review.getMovieId())
                    .rating(review.getRating())
                    .comment(review.getComment())
                    .build();
            reviewArrayList.add(reviewResponseDto);
        });
        return ReviewResponse.builder()
                .reviewList(reviewArrayList)
                .totalResult(reviewPage.getTotalElements())
                .status("success")
                .build();
    }
}
