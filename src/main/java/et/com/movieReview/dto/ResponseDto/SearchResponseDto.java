package et.com.movieReview.dto.ResponseDto;

import lombok.Data;

import java.util.List;

@Data
public class SearchResponseDto {
    private List<MovieListResponseDto> movieList;
    private Integer totalResult;
    private String response;

}
