package et.com.movieReview.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "review")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name ="user_id")
    private Long userId;
    @Column(name="movie_id")
    private Long movieId;
    @Column(name = "rating")
    private int rating;
    @Column(name = "comment")
    private String comment;
}
