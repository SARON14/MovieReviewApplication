package et.com.movieReview.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movie")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name ="year")
    private String year;

    @Column(name="runTime")
    private String runTime;

    @Column(name="genre")
    private String genre;

    @Column(name ="director")
    private String director;

    @Column(name ="writer")
    private String writer;

    @Column(name ="actors")
    private String actors;

    @Column(name ="plot")
    private String plot;

    @Column(name ="language")
    private String language;

    @Column(name ="poster")
    private String poster;

    @Column(name ="type")
    private String type;
}
