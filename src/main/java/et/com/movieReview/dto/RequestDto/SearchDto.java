package et.com.movieReview.dto.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchDto {
    private String title;
    private Integer year;
    private Long limit;
}
