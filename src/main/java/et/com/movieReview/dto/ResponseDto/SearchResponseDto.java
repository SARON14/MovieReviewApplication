package et.com.movieReview.dto.ResponseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchResponseDto {
    @JsonProperty("Search")
    private List<MovieListResponseDto> movieList;
    @JsonProperty("totalResults")
    private Integer totalResult;
    @JsonProperty("response")
    private String response;

}
