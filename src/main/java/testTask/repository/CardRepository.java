package testTask.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import testTask.domain.Card;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CardRepository extends CrudRepository<Card, Long> {

    Optional<Card> findByNumber(String number);

    Page<Card> findAllByOwnerId(long ownerId, Pageable pageable);

    @Modifying
    @Query("UPDATE Card e SET e.status = 'EXPIRED' WHERE e.expiresAt <= :now AND e.status <> 'EXPIRED'")
    void expireEntities(@Param("now") LocalDateTime now);
}
