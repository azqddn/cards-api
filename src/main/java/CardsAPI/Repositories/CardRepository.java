package CardsAPI.Repositories;

import CardsAPI.Entities.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {
    boolean existsByCardNumberAndDeletedDateIsNull(String cardNumber);
    Optional<Card> findByCardIdAndDeletedDateIsNull(UUID cardId);
    Page<Card> findAllByDeletedDateIsNull(Pageable pageable);
}
