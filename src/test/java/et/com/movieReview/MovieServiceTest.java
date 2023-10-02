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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

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
    public void testAddMovie() throws IOException {
        // Create a sample MovieRequestDto
        MovieRequestDto requestDto = new MovieRequestDto();
        requestDto.setTitle("Sample Movie");
        requestDto.setYear("2020");
        requestDto.setRuntime("120");
        requestDto.setGenre("Action");
        requestDto.setDirector("John Doe");
        requestDto.setWriter("Jane Smith");
        requestDto.setActors("Actor1, Actor2");
        requestDto.setPlot("Sample plot");
        requestDto.setLanguage("English");
        requestDto.setType("Movie");

        // Create a sample MockMultipartFile for the poster
        MockMultipartFile posterFile = new MockMultipartFile("poster", "poster.jpg", "image/jpeg", "poster data".getBytes());

        // Mock the saveUploadedFile method to return a sample photo URL
        when(movieService.saveUploadedFile(any())).thenReturn("https://example.com/poster.jpg");

        // Call the addMovie method
        MovieAddResponse response = movieService.addMovie(requestDto);

        // Verify that the movie is saved in the repository
        verify(movieRepository, times(1)).save(any());

        // Assert the response
        assertEquals("success", response.getStatus());
        assertNotNull(response.getMovieId());
    }

    @Test
    public void testGetMovieDetail_ExistingMovieId() {
        // Create a sample Movie
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Sample Movie");
        movie.setYear("2020");
        movie.setType("Movie");
        movie.setPoster("https://example.com/poster.jpg");

        // Mock the movieRepository.findById method to return the sample Movie
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        // Call the getMovieDetail method
        MovieResponseDto response = movieService.getMovieDetail(1L);

        // Assert the response
        assertEquals("Success", response.getStatus());
        assertEquals(1L, response.getMovieId());
        assertEquals("Sample Movie", response.getTitle());
        assertEquals(2020, response.getYear());
        assertEquals("Movie", response.getType());
        assertEquals("https://example.com/poster.jpg", response.getPoster());
    }

    @Test
    public void testGetMovieDetail_NonExistingMovieId() {
        // Mock the movieRepository.findById method to return an empty Optional
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the getMovieDetail method
        MovieResponseDto response = movieService.getMovieDetail(1L);

        // Assert the response
        assertEquals("failed", response.getStatus());
    }
    @Test
    public void testGetMovies() {
        // Mock searchDto
        SearchDto searchDto = new SearchDto();
        searchDto.setTitle("Titanic");
        searchDto.setYear("1997");
        searchDto.setLimit(5L);

        // Mock movieRepository response
        Movie movie1 = new Movie(1L, "Titanic", "1997", "120 minutes", "Drama", "mr e", "mr g", "mr q,mr d", "nice movie", "English", "public/dsnjdfnsdj.jpg", "Series");
        Movie movie2 = new Movie(2L, "TitanicII", "1997", "120 minutes", "Drama", "mr e", "mr g", "mr q,mr d", "nice movie", "English", "public/dsnjdfnsdj.jpg", "Series");
        List<Movie> mockMovies = Arrays.asList(movie1, movie2);
        when(movieRepository.findAllByTitleAndYear(searchDto.getTitle(), searchDto.getYear(), searchDto.getLimit()))
                .thenReturn(mockMovies);

        // Mock restTemplate response
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        String mockResponseBody = "{\"movieList\": [{\"year\": \"1997\", \"imdbID\": \"tt0120338\", \"poster\": \"poster3.jpg\", \"title\": \"Titanic\", \"type\": \"movie\"}]}";
        ResponseEntity<SearchResponseDto> mockResponse = new ResponseEntity<>(new SearchResponseDto(), HttpStatus.OK);
        mockResponse.getBody().setMovieList(Arrays.asList(new MovieListResponseDto()));
        mockServer.expect(requestTo("https://www.omdbapi.com/?apikey=[yourkey]&t=Titanic"))
                .andExpect(method(HttpMethod.GET))
                .andRespond((ResponseCreator) mockResponse);

        // Create instance of the service
        MovieService movieService = new MovieService(movieRepository, restTemplate);

        // Call the method under test
        List<MovieListResponseDto> result = movieService.getMovies(searchDto);

        // Verify the result
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getTitle()).isEqualTo("Titanic");
        assertThat(result.get(1).getTitle()).isEqualTo("Titanic 2");
        assertThat(result.get(2).getTitle()).isEqualTo("Titanic");
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
