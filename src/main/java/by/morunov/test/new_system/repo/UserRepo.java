package by.morunov.test.new_system.repo;

import by.morunov.test.new_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alex Morunov
 */
@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByLogin(String login);

}
