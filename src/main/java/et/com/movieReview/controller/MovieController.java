package et.com.movieReview.controller;

import et.com.movieReview.constants.Endpoints;
import et.com.movieReview.dto.RequestDto.MovieRequestDto;
import et.com.movieReview.dto.RequestDto.SearchDto;
import et.com.movieReview.dto.ResponseDto.MovieListResponseDto;
import et.com.movieReview.dto.ResponseDto.MovieResponseDto;
import et.com.movieReview.dto.ResponseDto.ResponseDTO;
import et.com.movieReview.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class MovieController {
    private final MovieService movieService;
    private static final String JSON = MediaType.APPLICATION_JSON_VALUE;

    @PostMapping(value = Endpoints.ADD_MOVIE, produces = JSON)
    public MovieResponseDto addMovie(@ModelAttribute MovieRequestDto payload) {
        return movieService.addMovie(payload);
    }

    @GetMapping(value = Endpoints.GET_MOVIE_DETAIL, produces = JSON)
    public ResponseDTO<?> getMovieDetail(@PathVariable long movieId) {
        return movieService.getMovieDetail(movieId);
    }

//    @GetMapping(value = Endpoints.SEARCH_MOVIE, produces = JSON)
//    public ResponseDTO<?> searchMovie(@RequestParam(required = false) String title,
//                                         @RequestParam(required = false) Integer year,
//                                         @RequestParam(required = false) Long limit) {
//        return movieService.searchMovie(title, year,limit);
//    }
    @GetMapping(value = Endpoints.SEARCH_MOVIE, produces = JSON)
public List<MovieListResponseDto> getMovies(@ModelAttribute SearchDto searchDto) {
    return movieService.getMovies(searchDto);
}
}
