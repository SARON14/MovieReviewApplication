package et.com.movieReview;

import et.com.movieReview.constants.ApiMessages;
import et.com.movieReview.dto.RequestDto.MovieRequestDto;
import et.com.movieReview.dto.ResponseDto.ResponseDTO;
import et.com.movieReview.model.Movie;
import et.com.movieReview.repository.MovieRepository;
import et.com.movieReview.service.MovieService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;
    private MovieService movieService;
    private final ApiMessages apiMessages = new ApiMessages();

    @BeforeEach
    public void setUp() {
        movieService = new MovieService(movieRepository,new RestTemplate());
    }

    @Test
    public void testAddMovie_Success() throws IOException {
        MovieRequestDto payload = new MovieRequestDto();
        payload.setTitle("The Chosen");
        payload.setYear(2023);
        payload.setActors("mr x, miss y");
        payload.setDirector("Nathan");
        payload.setGenre("Drama");
        payload.setLanguage("English");
        payload.setPlot("nice movie");
        payload.setRuntime("120 minutes");
        payload.setWriter("Someone");

        // Mock the saveUploadedFile method to return a photo URL
        String photoUrl = "public/poster.jpg";
        Mockito.when(movieService.saveUploadedFile(Mockito.any())).thenReturn(photoUrl);

        // Mock the movieRepository.save method to return the saved movie
        Movie savedMovie = new Movie();
        Mockito.when(movieRepository.save(Mockito.any())).thenReturn(savedMovie);

        // Act
        ResponseDTO<?> response = movieService.addMovie(payload);

        // Assert
        Mockito.verify(movieRepository, Mockito.times(1)).save(Mockito.any());
        Assertions.assertEquals(apiMessages.successMessageWithData(savedMovie), response);
        // You can also assert other properties of the response if needed
    }
}