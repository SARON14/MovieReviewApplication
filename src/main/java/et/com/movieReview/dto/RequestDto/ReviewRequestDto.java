package et.com.movieReview.dto.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequestDto {
    private Long userId;
    private Long movieId;
    private Integer rating;
    private String comment;
}
