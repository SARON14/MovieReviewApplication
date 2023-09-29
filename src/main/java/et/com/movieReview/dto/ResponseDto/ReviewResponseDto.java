package et.com.movieReview.dto.ResponseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponseDto {
    private Long movieId;
    private Long userId;
    private Integer rating;
    private String comment;
}
