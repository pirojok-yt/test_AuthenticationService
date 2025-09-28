package testTask.repository;

import org.springframework.data.repository.CrudRepository;
import testTask.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String email);
}
