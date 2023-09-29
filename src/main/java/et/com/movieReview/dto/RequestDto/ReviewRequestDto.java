package et.com.movieReview.dto.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDto {
    private Long userId;
    private Long movieId;
    private Integer rating;
    private String comment;
}
