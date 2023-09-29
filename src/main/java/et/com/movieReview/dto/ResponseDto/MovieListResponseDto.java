package et.com.movieReview.dto.ResponseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieListResponseDto {
    private Long movieId;
    private String title;
    private Integer year;
    private String imdbID;
    private String type;
    private String poster;
}
