package et.com.movieReview.service;

import et.com.movieReview.constants.ApiMessages;
import et.com.movieReview.dto.RequestDto.MovieRequestDto;
import et.com.movieReview.dto.ResponseDto.MovieDetailResponseDto;
import et.com.movieReview.dto.ResponseDto.ResponseDTO;
import et.com.movieReview.dto.ResponseDto.MovieListResponseDto;
import et.com.movieReview.dto.ResponseDto.SearchResponseDto;
import et.com.movieReview.model.Movie;
import et.com.movieReview.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieService {
    private final ApiMessages apiMessages = new ApiMessages();
    private final MovieRepository movieRepository;

    public ResponseDTO<?> addMovie(MovieRequestDto payload) {
        try {
            List<String> photoUrlList = new ArrayList<>();
            String photoUrl = null;
            if (payload.getPoster() != null) {
//                for (MultipartFile photo : payload.getPhotos()) {
                photoUrl = saveUploadedFile(payload.getPoster());
//                    if (photoUrl != null)
//                        photoUrlList.add(photoUrl);
                }
            return apiMessages.successMessageWithData(movieRepository.save(Movie.builder()
                    .type(payload.getType())
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
        if(movie.isPresent()){
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
    private String saveUploadedFile(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            String fileName = "public/"+ UUID.randomUUID() +"."+type;
            byte[] bytes = file.getBytes();
            Path path = Paths.get(fileName);
            Files.write(path, bytes);
            return fileName;
        }
        return null;
    }

    public ResponseDTO<?> searchMovie(int page, int size, List<Sort.Order> orders, String title, Integer year) {
        List<MovieListResponseDto> movieListResponseDto = new ArrayList<>();
        List<Movie> movies = new ArrayList<>();
        MovieListResponseDto searchDto = new MovieListResponseDto();
        SearchResponseDto searchResponseDto = new SearchResponseDto();
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));
        movies.forEach(movie -> {
            Page<Movie> pageMovie;
            Integer pageMovieCount;
            if (title != null && year == null) {
                pageMovie= movieRepository.findByTitle(title,pagingSort);
                pageMovieCount = movieRepository.countMovieByTitle(title);
                movies.addAll(pageMovie.getContent());
            }
            if (year != null && title == null) {
                pageMovie = movieRepository.findByYear(year,pagingSort);
                pageMovieCount = movieRepository.countMovieByYear(year);
                movies.addAll(pageMovie.getContent());
            } else {
                pageMovie= movieRepository.findByYearAndTitle(year, title,pagingSort);
                pageMovieCount = movieRepository.countMovieByYearAndTitle(year,title);
                movies.addAll(pageMovie.getContent());
            }
            BeanUtils.copyProperties(movies,searchDto);
            searchResponseDto.setTotalResult(pageMovieCount);
        });
        movieListResponseDto.add(searchDto);
        searchResponseDto.setMovieList(movieListResponseDto);
        searchResponseDto.setResponse("True");
        return apiMessages.successMessageWithData(searchResponseDto);
    }
}
