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
            return apiMessages.successMessageWithData(MovieDetailResponseDto.builder()
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


    //    public ResponseDTO<?> searchMovie(String title, Integer year,Long limit) {
//        List<MovieListResponseDto> movieListResponseDto = new ArrayList<>();
//        List<Movie> movies = new ArrayList<>();
//        MovieListResponseDto searchDto = new MovieListResponseDto();
//        SearchResponseDto searchResponseDto = new SearchResponseDto();
//        movies.forEach(movie -> {
//            List<Movie> movieList;
//            Integer movieCount;
//            if (title != null && year == null ) {
//                movieList= movieRepository.findByTitle(title);
//                movieCount = movieRepository.countMovieByTitle(title);
//                movies.addAll(movieList);
//            }
//            if (year != null && title == null) {
//                movieList = movieRepository.findByYear(year);
//                movieCount = movieRepository.countMovieByYear(year);
//                movies.addAll(movieList);
//            }
//            if(year != null && title != null) {
//                movieList= movieRepository.findByYearAndTitle(year, title);
//                movieCount = movieRepository.countMovieByYearAndTitle(year,title);
//                movies.addAll(movieList);
//            }else{
//                movieList = movieRepository.findAll();
//                movieCount = Math.toIntExact(movieRepository.count());
//                movies.addAll(movieList);
//            }
//            BeanUtils.copyProperties(movies,searchDto);
//            searchResponseDto.setTotalResult(movieCount);
//        });
//        movieListResponseDto.add(searchDto);
//        searchResponseDto.setMovieList(movieListResponseDto);
//        searchResponseDto.setResponse("True");
//        return apiMessages.successMessageWithData(searchResponseDto);
//    }
//    public ResponseDTO<?> searchMovie(String title, Integer year,Long limit) {
//        List<MovieListResponseDto> movieListResponseDto = new ArrayList<>();
//        List<Movie> movies = new ArrayList<>();
//        MovieListResponseDto searchDto = new MovieListResponseDto();
//        SearchResponseDto searchResponseDto = new SearchResponseDto();
//        List<Movie> movieList;
////        if(title != null && year == null){
////          movieList = movieRepository.findByTitle(title);
////        }else {
//        log.info("year"+ year);
//        log.info("title"+ title);
//            movieList = movieRepository.findByTitle(title);
//            log.info("mmmmmmmmmmooooooooooovielist" + movieList);
////        }
//        movieList.forEach(movie -> {
////            searchDto.setTitle(movie.getTitle());
////            searchDto.setYear(movie.getYear());
////            searchDto.setPoster(movie.getPoster());
////            searchDto.setType(movie.getType());
////            searchDto.setImdbID(String.valueOf(movie.getId()));
////            movies.addAll(movieList);
//            BeanUtils.copyProperties(movie,searchDto);
////            searchResponseDto.setTotalResult(movieCount);
//        });
//        movieListResponseDto.add(searchDto);
//        searchResponseDto.setMovieList(movieListResponseDto);
//        searchResponseDto.setResponse("True");
//        return apiMessages.successMessageWithData(searchResponseDto);
//    }
    public List<MovieListResponseDto> getMovies(SearchDto searchDto) {
        List<MovieListResponseDto> movieListResponseDtos = new ArrayList<>();
        List<Movie> movies = movieRepository.findAllByTitleAndYear(searchDto.getTitle(), searchDto.getYear(), searchDto.getLimit());
        log.info("movies" +movies);
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
        if (remainingLimit>0) {
            HttpHeaders headers = new HttpHeaders();
            String url = "https://www.omdbapi.com/?s=Batman&page=2&apikey=a55e7284&";
            HttpEntity<String> entity = new HttpEntity<>(headers);
            String uriWithParam = buildParam(searchDto, url, remainingLimit+1);
            ResponseEntity<SearchResponseDto> exchange =
                    restTemplate.exchange(uriWithParam, HttpMethod.GET, entity, SearchResponseDto.class);
            exchange.getBody().getMovieList().forEach(movieListResponseDto -> {
                MovieListResponseDto movieListResponse =MovieListResponseDto.builder()
                        .year(movieListResponseDto.getYear())
                        .imdbID(movieListResponseDto.getImdbID())
                        .poster(movieListResponseDto.getPoster())
                        .title(movieListResponseDto.getTitle())
                        .type(movieListResponseDto.getType())
                        .movieId(null)
                        .build();
                movieListResponseDtos.add(movieListResponse);
        });
    }
        return movieListResponseDtos;
}
    public String buildParam(SearchDto searchDto, String url, Long remainingLimit){

        Map<String, String> params = new HashMap<>();
        if(searchDto.getTitle() !=null) params.put("title", searchDto.getTitle());
        if(remainingLimit !=null)
            params.put("limit", remainingLimit.toString());
        else
            params.put("limit","10");
        if(searchDto.getYear() !=null) params.put("year", searchDto.getYear().toString());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }

        return builder.toUriString();
    }
}