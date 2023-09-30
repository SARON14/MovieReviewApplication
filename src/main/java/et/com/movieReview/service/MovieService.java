package et.com.movieReview.service;

import et.com.movieReview.constants.ApiMessages;
import et.com.movieReview.dto.RequestDto.MovieRequestDto;
import et.com.movieReview.dto.RequestDto.SearchDto;
import et.com.movieReview.dto.ResponseDto.*;
import et.com.movieReview.model.Movie;
import et.com.movieReview.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieService {
    private final ApiMessages apiMessages = new ApiMessages();
    private final MovieRepository movieRepository;
    private final RestTemplate restTemplate;

    public ResponseDTO<?> addMovie(MovieRequestDto payload) {
        try {
            String photoUrl = null;
            if (payload.getPoster() != null) {
                photoUrl = saveUploadedFile(payload.getPoster());
            }
            return apiMessages.successMessageWithData(movieRepository.save(Movie.builder()
                    .title(payload.getTitle())
                    .year(payload.getYear())
                    .runTime(payload.getRuntime())
                    .genre(payload.getGenre())
                    .director(payload.getDirector())
                    .writer(payload.getWriter())
                    .actors(payload.getActors())
                    .plot(payload.getPlot())
                    .language(payload.getLanguage())
                    .type(payload.getType())
                    .poster(photoUrl)
                    .build()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ResponseDTO<?> getMovieDetail(long movieId) {
        Optional<Movie> movie = movieRepository.findById(movieId);
        if (movie.isPresent()) {
            return apiMessages.successMessageWithData(MovieResponseDto.builder()
                    .status("Success")
                    .movieId(movie.get().getId())
                    .title(movie.get().getTitle())
                    .year(movie.get().getYear())
                    .type(movie.get().getType())
                    .poster(movie.get().getPoster())
                    .build());
        }
        return apiMessages.errorMessage("movie not found");
    }

    public String saveUploadedFile(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            String fileName = "public/" + UUID.randomUUID() + "." + type;
            byte[] bytes = file.getBytes();
            Path path = Paths.get(fileName);
            Files.write(path, bytes);
            return fileName;
        }
        return null;
    }

    public List<MovieListResponseDto> getMovies(SearchDto searchDto) {
        List<MovieListResponseDto> movieListResponseDtos = new ArrayList<>();
        List<Movie> movies = movieRepository.findAllByTitleAndYear(searchDto.getTitle(), searchDto.getYear(), searchDto.getLimit());
        log.info("movies" + movies);
        movies.forEach(movie -> {
            MovieListResponseDto movieListResponseDto = MovieListResponseDto.builder()
                    .movieId(movie.getId())
                    .type(movie.getType())
                    .title(movie.getTitle())
                    .poster(movie.getPoster())
                    .year(movie.getYear())
                    .imdbID(null)
                    .build();
            movieListResponseDtos.add(movieListResponseDto);
        });
        Long remainingLimit = searchDto.getLimit() - movies.size();
        if (remainingLimit > 0) {
            HttpHeaders headers = new HttpHeaders();
            String url = "https://www.omdbapi.com/";
            HttpEntity<String> entity = new HttpEntity<>(headers);
            String uriWithParam = buildParam(searchDto, url);
            ResponseEntity<SearchResponseDto> exchange =
                    restTemplate.exchange(uriWithParam, HttpMethod.GET, entity, SearchResponseDto.class);

            List<MovieListResponseDto> movieList = exchange.getBody().getMovieList();

            Integer responseSize = movieList.size();
            Integer index = 0;

            while (remainingLimit > 0 && responseSize > 0) {
                MovieListResponseDto movieListResponseDto = movieList.get(index);
                MovieListResponseDto movieListResponse = MovieListResponseDto.builder()
                        .year(movieListResponseDto.getYear())
                        .imdbID(movieListResponseDto.getImdbID())
                        .poster(movieListResponseDto.getPoster())
                        .title(movieListResponseDto.getTitle())
                        .type(movieListResponseDto.getType())
                        .movieId(null)
                        .build();

                movieListResponseDtos.add(movieListResponse);
                remainingLimit--;
                responseSize--;
                index++;
            }
        }
        return movieListResponseDtos;
    }

    public String buildParam(SearchDto searchDto, String url) {

        Map<String, String> params = new HashMap<>();
        params.put("s", searchDto.getTitle());
        if (searchDto.getYear() != null)
            params.put("y", searchDto.getYear());
        params.put("apikey", "a55e7284");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        return builder.toUriString();
    }

    public MovieDetailResponseDto getMovieDetailByImdbID(String imdbID) {
        HttpHeaders headers = new HttpHeaders();
        String url = "https://www.omdbapi.com/";
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String uriWithParam = buildParamForMovieDetail(imdbID, url);
        ResponseEntity<MovieDetailResponseDto> exchange =
                restTemplate.exchange(uriWithParam, HttpMethod.GET, entity, MovieDetailResponseDto.class);

        MovieDetailResponseDto movieDetail = exchange.getBody();
        MovieDetailResponseDto movieDetailResponseDto = null;
        if (movieDetail != null) {
            movieDetailResponseDto = MovieDetailResponseDto.builder()
                    .imdbID(movieDetail.getImdbID())
                    .actors(movieDetail.getActors())
                    .awards(movieDetail.getAwards())
                    .boxOffice(movieDetail.getBoxOffice())
                    .country(movieDetail.getCountry())
                    .dvd(movieDetail.getDvd())
                    .imdbRating(movieDetail.getImdbRating())
                    .imdbVotes(movieDetail.getImdbVotes())
                    .director(movieDetail.getDirector())
                    .genre(movieDetail.getGenre())
                    .language(movieDetail.getLanguage())
                    .metaScore(movieDetail.getMetaScore())
                    .response(movieDetail.getResponse())
                    .plot(movieDetail.getPlot())
                    .production(movieDetail.getProduction())
                    .rated(movieDetail.getRated())
                    .ratings(movieDetail.getRatings())
                    .released(movieDetail.getReleased())
                    .runtime(movieDetail.getRuntime())
                    .type(movieDetail.getType())
                    .poster(movieDetail.getPoster())
                    .website(movieDetail.getWebsite())
                    .build();
        }
        return movieDetailResponseDto;
    }
    public String buildParamForMovieDetail(String imdbID , String url) {

        Map<String, String> params = new HashMap<>();
        params.put("i", imdbID);
        params.put("apikey", "a55e7284");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        return builder.toUriString();
    }
}