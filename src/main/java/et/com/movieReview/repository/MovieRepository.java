package et.com.movieReview.repository;

import et.com.movieReview.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie,Long> {
    List<Movie> findByTitle(String title);
    Integer countMovieByTitle(String title);
    List<Movie>findByYear(Integer year);
    Integer countMovieByYear(Integer year);
    List<Movie>findByYearAndTitle(Integer year,String title);
    Integer countMovieByYearAndTitle(Integer year,String title);
    @Query(value = "select * from movie where " +
            "(?1 is null or title = ?1) AND " +
            "(?2 is null or year = ?2) limit ?3 ", nativeQuery = true)
    List<Movie> findAllByTitleAndYear(
            @Param("title") String title,
            @Param("year") Integer year,
            @Param("limit") Long limit);
    long count();
}
