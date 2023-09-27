package et.com.movieReview.dto.RequestDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ReviewRequestDto {
    private Long userId;
    private Long movieId;
    @NotNull
    @Min(value = 1,message = "rating can't be less than 1")
    @Max(value = 10,message = "rating can't be grater than 10")
    private int rating;
    private String comment;
}
