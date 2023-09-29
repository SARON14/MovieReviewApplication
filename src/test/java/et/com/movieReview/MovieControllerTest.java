package et.com.movieReview;

import et.com.movieReview.controller.MovieController;
import et.com.movieReview.dto.RequestDto.MovieRequestDto;
import et.com.movieReview.dto.ResponseDto.MovieResponseDto;
import et.com.movieReview.service.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieControllerTest {
    private static class Fixture {

        MovieRequestDto getMovieRequestDto() {
            return MovieRequestDto.builder()
                    .title("xyz")
                    .actors("Mr x, Mr y, Mr z")
                    .director("Mr G")
                    .genre("Horror")
                    .language("English")
                    .plot("very nice Movie")
                    .runtime("120 minutes")
//                   .poster(new MultipartFile)
                    .type("movie")
                    .writer("Miss J")
                    .year(2005)
                    .build();
        }

        MovieResponseDto getMovieResponseDto() {
            MovieResponseDto movieResponseDto = new MovieResponseDto();
            movieResponseDto.setId(1L);
            movieResponseDto.setStatus("success");
            return movieResponseDto;
        }
//        MovieDetailResponseDto getMovieDetail(){
//            MovieDetailResponseDto.builder()
//                    .
//                    .build();
//        }

    }
    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    private final Fixture fixture = new Fixture();
    @Test
    void given_whenCreatePet_thenReturnSuccess() {
        when(movieService.addMovie(fixture.getMovieRequestDto())).thenReturn(fixture.getMovieResponseDto());

        MovieResponseDto response = movieController.addMovie(fixture.getMovieRequestDto());

        assertEquals("success", response.getStatus());
    }
}

