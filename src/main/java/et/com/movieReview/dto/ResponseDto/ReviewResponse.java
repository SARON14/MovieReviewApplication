package et.com.movieReview.dto.ResponseDto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class ReviewResponse {
    private List<ReviewResponseDto> reviewList;
    private Long totalResult;
    private String status;
}
