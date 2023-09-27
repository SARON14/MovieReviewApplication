package et.com.movieReview.dto.ResponseDto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MovieDetailResponseDto {
    private String status;
    private Long movieId;
    private String title;
    private Integer year;
    private String rated;
    private String released;
    private String runtime;
    private String genre;
    private String director;
    private String actors;
    private String plot;
    private String language;
    private String country;
    private String awards;
    private String poster;
    private List<String> ratings;
    private String metaScore;
    private Float imdbRating;
    private Integer imdbVotes;
    private String imdbID;
    private String type;
    private String dvd;
    private String boxOffice;
    private String production;
    private String website;
    private String response;
}
