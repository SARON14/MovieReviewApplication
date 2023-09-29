package et.com.movieReview.dto.RequestDto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDto {
    private Long userId;
    private Long movieId;
    private Float rating;
    private String comment;
}
