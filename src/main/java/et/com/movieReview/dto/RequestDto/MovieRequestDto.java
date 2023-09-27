package et.com.movieReview.dto.RequestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieRequestDto {
    private String title;
    private Integer year;
    private String runtime;
    private String genre;
    private String director;
    private String writer;
    private String actors;
    private String plot;
    private String language;
    private String type;
    @JsonProperty("poster")
    private  MultipartFile poster;
}
