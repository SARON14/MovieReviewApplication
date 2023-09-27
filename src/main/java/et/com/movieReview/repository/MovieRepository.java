package et.com.movieReview.repository;

import et.com.movieReview.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie,Long> {
    Page<Movie> findByTitle(String title, Pageable pageable);
    Integer countMovieByTitle(String title);
    Page<Movie>findByYear(Integer year, Pageable pageable);
    Integer countMovieByYear(Integer year);
    Page<Movie>findByYearAndTitle(Integer year,String title, Pageable pageable);
    Integer countMovieByYearAndTitle(Integer year,String title);
}
