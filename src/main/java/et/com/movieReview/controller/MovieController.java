package et.com.movieReview.controller;

import et.com.movieReview.constants.Endpoints;
import et.com.movieReview.dto.RequestDto.MovieRequestDto;
import et.com.movieReview.dto.RequestDto.SearchDto;
import et.com.movieReview.dto.ResponseDto.MovieDetailResponseDto;
import et.com.movieReview.dto.ResponseDto.MovieListResponseDto;
import et.com.movieReview.dto.ResponseDto.ResponseDTO;
import et.com.movieReview.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class MovieController {
    private static final String JSON = MediaType.APPLICATION_JSON_VALUE;
    private final MovieService movieService;

    @PostMapping(value = Endpoints.ADD_MOVIE, produces = JSON)
    public ResponseDTO<?> addMovie(@ModelAttribute MovieRequestDto payload) {
        return movieService.addMovie(payload);
    }

    @GetMapping(value = Endpoints.GET_MOVIE_DETAIL, produces = JSON)
    public ResponseDTO<?> getMovieDetail(@PathVariable long movieId) {
        return movieService.getMovieDetail(movieId);
    }
    @GetMapping(value = Endpoints.GET_MOVIE_DETAIL_BY_ImdbID, produces = JSON)
    public MovieDetailResponseDto getMovieDetailByImdbID(@PathVariable String imdbID) {
        return movieService.getMovieDetailByImdbID(imdbID);
    }

    @GetMapping(value = Endpoints.SEARCH_MOVIE, produces = JSON)
    public List<MovieListResponseDto> getMovies(@ModelAttribute SearchDto searchDto) {
        return movieService.getMovies(searchDto);
    }
}
