package testTask.repository;

import org.springframework.data.repository.CrudRepository;
import testTask.domain.LoginAttempt;

import java.util.List;

public interface LoginAttemptRepository extends CrudRepository<LoginAttempt, Long> {

    List<LoginAttempt> findAllByUsername(String username);
}
