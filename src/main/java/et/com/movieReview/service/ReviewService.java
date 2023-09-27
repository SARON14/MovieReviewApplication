package et.com.movieReview.service;

import et.com.movieReview.constants.ApiMessages;
import et.com.movieReview.dto.ResponseDto.ResponseDTO;
import et.com.movieReview.dto.RequestDto.ReviewRequestDto;
import et.com.movieReview.model.Movie;
import et.com.movieReview.model.Review;
import et.com.movieReview.model.User;
import et.com.movieReview.repository.MovieRepository;
import et.com.movieReview.repository.ReviewRepository;
import et.com.movieReview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository adoptRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ApiMessages apiMessages = new ApiMessages();

    public ResponseDTO<?> addReview(ReviewRequestDto payload) {
        Optional<User> user = userRepository.findById(payload.getUserId());
        Optional<Movie> movie = movieRepository.findById(payload.getMovieId());
        if (!user.isPresent() && !movie.isPresent() ){
            return apiMessages.errorMessage("record doesn't exist");
        }
        return apiMessages.successMessageWithData(Review.builder()
                        .userId(payload.getUserId())
                        .movieId(payload.getMovieId())
                        .rating(payload.getRating())
                        .comment(payload.getComment())
                        .build());
    }
}
