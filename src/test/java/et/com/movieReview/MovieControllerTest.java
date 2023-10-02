package et.com.movieReview;

import et.com.movieReview.controller.MovieController;
import et.com.movieReview.dto.RequestDto.MovieRequestDto;
import et.com.movieReview.dto.RequestDto.SearchDto;
import et.com.movieReview.dto.ResponseDto.MovieAddResponse;
import et.com.movieReview.dto.ResponseDto.MovieDetailResponseDto;
import et.com.movieReview.dto.ResponseDto.MovieListResponseDto;
import et.com.movieReview.dto.ResponseDto.MovieResponseDto;
import et.com.movieReview.service.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieControllerTest {
    private final Fixture fixture = new Fixture();
    @Mock
    private MovieService movieService;
    @InjectMocks
    private MovieController movieController;

    @Test
    void given_whenAddMovie_thenReturnSuccess() {
        when(movieService.addMovie(fixture.getMovieRequest())).thenReturn(fixture.getMovieAddResponse());

        MovieAddResponse response = movieController.addMovie(fixture.getMovieRequest());

        assertEquals("success", response.getStatus());
    }
    @Test
    void given_whenGetMovies_thenReturnMovieList() {
        when(movieService.getMovies(fixture.getSearchDto())).thenReturn(fixture.getMovieAddResponseList());

        List<MovieListResponseDto> responseList = movieController.getMovies(fixture.getSearchDto());

        assertEquals(1, responseList.size());
    }
    @Test
    void get_WhenGetMovieDetail_thenReturnGetMovieDetail(){
        when(movieService.getMovieDetail(1L)).thenReturn(fixture.getMovieResponseDto());
        MovieResponseDto response = movieController.getMovieDetail(1L);
        assertEquals("success",response.getStatus());
    }
    @Test
    void get_WhenGetMovieDetailByImdbID_thenReturnGetMovieDetail(){
        when(movieService.getMovieDetailByImdbID("tt2224026")).thenReturn(fixture.getMovieDetailResponseDto());
        MovieDetailResponseDto response = movieController.getMovieDetailByImdbID("tt2224026");
        assertEquals("True",response.getResponse());
    }

    private static class Fixture {

        MovieRequestDto getMovieRequest() {
            return MovieRequestDto.builder()
                    .title("Sample Movie")
                    .year("2020")
                    .runtime("120")
                    .genre("Action")
                    .director("John Doe")
                    .writer("Jane Smith")
                    .actors("Actor1, Actor2")
                    .plot("Sample plot")
                    .language("English")
                    .type("Movie")
                    .build();
        }

        MovieAddResponse getMovieAddResponse() {
            return MovieAddResponse.builder()
                    .status("success")
                    .movieId(1L)
                    .build();
        }

        SearchDto getSearchDto() {
            return SearchDto.builder()
                    .title("Home")
                    .year("2015")
                    .limit(10L)
                    .build();
        }

        List<MovieListResponseDto> getMovieAddResponseList() {
            List<MovieListResponseDto> movieListResponseDtos = new ArrayList<>();
            movieListResponseDtos.add(MovieListResponseDto.builder()
                    .movieId(1L)
                    .title("Home")
                    .year("2015")
                    .imdbID("tt2224026")
                    .type("movie")
                    .poster("public/picture.jpg")
                    .build());
            return movieListResponseDtos;
        }
        MovieResponseDto getMovieResponseDto(){
            return MovieResponseDto.builder()
                    .movieId(1L)
                    .status("success")
                    .title("Home")
                    .year("2015")
                    .type("movie")
                    .poster("public/picture.jpg")
                    .build();
        }
        MovieDetailResponseDto getMovieDetailResponseDto(){
            return MovieDetailResponseDto.builder()
                    .title("The Movie")
                    .year("2022")
                    . rated("PG-13")
                    .released("05 May 2017")
                    . runtime("136 min")
                    .genre("Action, Adventure, Comedy")
                    . director("James Gunn")
                    . writer("James Gunn, Dan Abnett, Andy Lanning")
                    . actors("Chris Pratt, Zoe Saldana, Dave Bautista")
                    .plot("The Guardians struggle to keep together as a team while dealing with their personal family issues, notably Star-Lord's encounter with his father, the ambitious celestial being Ego.")
                    . language("English")
                    .country("United States")
                    .awards("Nominated for 1 Oscar. 15 wins & 60 nominations total")
                    .poster("public/MV5BNjM0NTc0NzItM2FlYS00YzEwLWE0YmUtNTA2ZWIzODc2OTgxXkEyXkFqcGdeQXVyNTgwNzIyNzg@._V1_SX300.jpg")
                    .response("True")
                    .build();
        }
    }
}
