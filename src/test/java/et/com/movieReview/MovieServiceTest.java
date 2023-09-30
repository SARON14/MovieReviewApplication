package et.com.movieReview;

import et.com.movieReview.constants.ApiMessages;
import et.com.movieReview.dto.RequestDto.MovieRequestDto;
import et.com.movieReview.dto.RequestDto.SearchDto;
import et.com.movieReview.dto.ResponseDto.*;
import et.com.movieReview.model.Movie;
import et.com.movieReview.repository.MovieRepository;
import et.com.movieReview.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    private final ApiMessages apiMessages = new ApiMessages();
    private final RestTemplate restTemplate;
    @Mock
    private MovieRepository movieRepository;
    private MovieService movieService;

    public MovieServiceTest() {
        restTemplate = null;
    }

    @BeforeEach
    public void setUp() {
        movieService = new MovieService(movieRepository, new RestTemplate());
    }

    @Test
    public void testAddMovie_Success() throws IOException {
        MovieRequestDto payload = new MovieRequestDto();
        payload.setTitle("The Chosen");
        payload.setYear("2023");
        payload.setActors("mr x, miss y");
        payload.setDirector("Nathan");
        payload.setGenre("Drama");
        payload.setLanguage("English");
        payload.setPlot("nice movie");
        payload.setRuntime("120 minutes");
        payload.setWriter("Someone");

        String photoUrl = "public/poster.jpg";
        when(movieService.saveUploadedFile(any())).thenReturn(photoUrl);

        Movie savedMovie = new Movie();
        when(movieRepository.save(any())).thenReturn(savedMovie);

        ResponseDTO<?> response = movieService.addMovie(payload);

        verify(movieRepository, Mockito.times(1)).save(any());
        assertEquals(apiMessages.successMessageWithData(savedMovie), response);
    }

    @Test
    void testGetMovieDetail_WhenMovieExists_ReturnsSuccessResponse() {
        long movieId = 1L;
        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setTitle("The Shawshank Redemption");
        movie.setYear("1994");
        movie.setType("Drama");
        movie.setPoster("https://example.com/poster.jpg");

        given(movieRepository.findById(movieId)).willReturn(Optional.of(movie));

        ResponseDTO<?> response = movieService.getMovieDetail(movieId);

        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertNotNull(response.getData());

        MovieResponseDto movieResponse = (MovieResponseDto) response.getData();
        assertEquals(movieId, movieResponse.getMovieId());
        assertEquals("The Shawshank Redemption", movieResponse.getTitle());
        assertEquals(1994, movieResponse.getYear());
        assertEquals("Drama", movieResponse.getType());
        assertEquals("https://example.com/poster.jpg", movieResponse.getPoster());
    }

    @Test
    void testGetMovieDetail_WhenMovieDoesNotExist_ReturnsErrorResponse() {
        long movieId = 1L;

        given(movieRepository.findById(movieId)).willReturn(Optional.empty());

        ResponseDTO<?> response = movieService.getMovieDetail(movieId);

        assertNotNull(response);
        assertEquals("error", response.getStatus());
        assertEquals("movie not found", response.getMessage());
    }

    @Test
    void getMovies_ShouldReturnMovieListResponseDto() {

        SearchDto searchDto = new SearchDto();
        searchDto.setTitle("Titanic");
        searchDto.setYear("1997");
        searchDto.setLimit(5L);

        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie(1L, "Titanic", "1997", "120 minutes", "Drama", "mr e", "mr g", "mr q,mr d", "nice movie", "English", "public/dsnjdfnsdj.jpg", "Series"));
        movies.add(new Movie(2L, "TitanicII", "1997", "120 minutes", "Drama", "mr e", "mr g", "mr q,mr d", "nice movie", "English", "public/dsnjdfnsdj.jpg", "Series"));
        when(movieRepository.findAllByTitleAndYear(eq("Titanic"), eq("1997"), eq(5L))).thenReturn(movies);


        SearchResponseDto searchResponseDto = new SearchResponseDto();
        List<MovieListResponseDto> movieList = new ArrayList<>();
        movieList.add(new MovieListResponseDto(3L, "Titanic III", "2022", "tt1234567", "Action", "public/djnfjdsnfjk.jpg"));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(SearchResponseDto.class)))
                .thenReturn(new ResponseEntity<>(searchResponseDto, HttpStatus.OK));
        when(searchResponseDto.getMovieList()).thenReturn(movieList);


        List<MovieListResponseDto> result = movieService.getMovies(searchDto);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getMovieId()).isEqualTo(1L);
        assertThat(result.get(0).getType()).isEqualTo("Drama");
        assertThat(result.get(0).getTitle()).isEqualTo("Titanic");
        assertThat(result.get(0).getPoster()).isEqualTo("https://example.com/poster1.jpg");
        assertThat(result.get(0).getYear()).isEqualTo(1997);
        assertThat(result.get(0).getImdbID()).isNull();

        assertThat(result.get(1).getMovieId()).isEqualTo(2L);
        assertThat(result.get(1).getType()).isEqualTo("Drama");
        assertThat(result.get(1).getTitle()).isEqualTo("Titanic II");
        assertThat(result.get(1).getPoster()).isEqualTo("https://example.com/poster2.jpg");
        assertThat(result.get(1).getYear()).isEqualTo(2020);
        assertThat(result.get(1).getImdbID()).isNull();

        assertThat(result.get(2).getMovieId()).isNull();
        assertThat(result.get(2).getType()).isEqualTo("Action");
        assertThat(result.get(2).getTitle()).isEqualTo("Titanic III");
        assertThat(result.get(2).getPoster()).isEqualTo("https://example.com/poster3.jpg");
        assertThat(result.get(2).getYear()).isEqualTo(2022);
        assertThat(result.get(2).getImdbID()).isEqualTo("tt1234567");

        // Verify the interactions
        verify(movieRepository).findAllByTitleAndYear("Titanic", "1997", 5L);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(), eq(SearchResponseDto.class));
        verify(searchResponseDto).getMovieList();
    }
    @Test
    public void testGetMovieDetailByImdbID() {
        MovieDetailResponseDto mockedResponse = new MovieDetailResponseDto();
        mockedResponse.setTitle("The Movie");
        mockedResponse.setYear("2022");
        mockedResponse.setRated("PG-13");
        mockedResponse.setReleased("05 May 2017");
        mockedResponse.setRuntime("136 min");
        mockedResponse.setGenre("Action, Adventure, Comedy");
        mockedResponse.setDirector("James Gunn");
        mockedResponse.setWriter("James Gunn, Dan Abnett, Andy Lanning");
        mockedResponse.setActors("Chris Pratt, Zoe Saldana, Dave Bautista");
        mockedResponse.setPlot("The Guardians struggle to keep together as a team while dealing with their personal family issues, notably Star-Lord's encounter with his father, the ambitious celestial being Ego.");
        mockedResponse.setLanguage("English");
        mockedResponse.setCountry("United States");
        mockedResponse.setAwards("Nominated for 1 Oscar. 15 wins & 60 nominations total");
        mockedResponse.setPoster("public/MV5BNjM0NTc0NzItM2FlYS00YzEwLWE0YmUtNTA2ZWIzODc2OTgxXkEyXkFqcGdeQXVyNTgwNzIyNzg@._V1_SX300.jpg");


        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(MovieDetailResponseDto.class)))
                .thenReturn(ResponseEntity.ok(mockedResponse));

        MovieDetailResponseDto movieDetail = movieService.getMovieDetailByImdbID("tt1234567");

        verify(restTemplate).exchange(eq("https://www.omdbapi.com/?apikey=a55e7284&i=tt1234567"), eq(HttpMethod.GET), any(HttpEntity.class), eq(MovieDetailResponseDto.class));

        assertNotNull(movieDetail);
        assertEquals("The Movie", movieDetail.getTitle());
        assertEquals("2022", movieDetail.getYear());

        assertEquals("Action", movieDetail.getGenre());
        assertTrue(movieDetail.getActors().contains("Tom Cruise"));
    }

}
