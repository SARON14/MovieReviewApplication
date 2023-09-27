package et.com.movieReview.controller;

import et.com.movieReview.constants.Endpoints;
import et.com.movieReview.dto.RequestDto.MovieRequestDto;
import et.com.movieReview.dto.ResponseDto.ResponseDTO;
import et.com.movieReview.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class MovieController {
    private final MovieService movieService;
    private static final String JSON = MediaType.APPLICATION_JSON_VALUE;

    @PostMapping(value = Endpoints.ADD_MOVIE, produces = JSON, consumes = JSON)
    public ResponseDTO<?> addMovie(@ModelAttribute MovieRequestDto payload) {
        return movieService.addMovie(payload);
    }

    @GetMapping(value = Endpoints.GET_MOVIE_DETAIL, produces = JSON)
    public ResponseDTO<?> getMovieDetail(@PathVariable long movieId) {
        return movieService.getMovieDetail(movieId);
    }

    @GetMapping(value = Endpoints.SEARCH_MOVIE, produces = JSON)
    public ResponseDTO<?> searchMovie(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size,
                                      @RequestParam(defaultValue = "id,desc") String[] sort,
                                      @RequestParam(required = false) String title,
                                      @RequestParam(required = false) Integer year) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sort[0].contains(",")) {
            // will sort more than 2 fields
            // sortOrder="field, direction"
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            // sort=[field, direction]
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
        }
        return movieService.searchMovie(page, size, orders, title, year);
    }
    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }
}
