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
    private String status;
    private Long id;
}
