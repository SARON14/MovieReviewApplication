package et.com.movieReview.dto.ResponseDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class MovieResponseDto {
    private Long movieId;
    private String status;
    private String title;
    private String year;
    private String type;
    private String poster;

}
