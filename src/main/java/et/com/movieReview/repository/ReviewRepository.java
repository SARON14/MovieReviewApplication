package et.com.movieReview.repository;

import et.com.movieReview.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findAllByUserId(Long userId);

}
