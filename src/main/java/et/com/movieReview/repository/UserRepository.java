package et.com.movieReview.repository;

import et.com.movieReview.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUserName(String userName);
    User findByEmail(String email);
}
