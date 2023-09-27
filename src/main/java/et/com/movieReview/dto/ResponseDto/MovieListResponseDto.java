package et.com.movieReview.dto.ResponseDto;

import lombok.Data;

@Data
public class MovieListResponseDto {
    private String title;
    private Integer year;
    private String imdbID;
    private String type;
    private String poster;
}
