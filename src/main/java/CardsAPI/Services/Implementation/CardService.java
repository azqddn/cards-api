package CardsAPI.Services.Implementation;

import CardsAPI.Dtos.request.CardCreateRequest;
import CardsAPI.Dtos.request.CardUpdateRequest;
import CardsAPI.Dtos.response.CardResponse;
import CardsAPI.Entities.Card;
import CardsAPI.Repositories.CardRepository;
import CardsAPI.Services.Interface.ICardService;
import org.springframework.transaction.annotation.Transactional;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CardService implements ICardService {
    private final CardRepository repository;

    @Override
    @Transactional
    public CardResponse create(CardCreateRequest request) {

        if (repository.existsByCardNumber(request.getCardNumber())) {
            throw new RuntimeException("Card number already exists");
        }

        Card card = Card.builder()
                .cardNumber(request.getCardNumber())
                .cardHolder(request.getCardHolder())
                .cardType(request.getCardType())
                .creditLimit(request.getCreditLimit())
                .status(request.getStatus())
                .createdDate(LocalDateTime.now())
                .build();

        Card savedCard = repository.save(card);

        return map(savedCard);
    }

    @Override
    @Transactional
    public CardResponse update(Long cardId, CardUpdateRequest request) {

        Card card = repository.findById(cardId).orElseThrow(() ->
                        new RuntimeException("Card not found with id: " + cardId));

        card.setCardHolder(request.getCardHolder());
        card.setCardType(request.getCardType());
        card.setCreditLimit(request.getCreditLimit());
        card.setStatus(request.getStatus());
        card.setUpdatedDate(LocalDateTime.now());

        Card updatedCard = repository.save(card);

        return map(updatedCard);
    }

    @Override
    @Transactional(readOnly = true)
    public CardResponse getById(Long cardId) {

        Card card = repository.findById(cardId).orElseThrow(() ->
                        new RuntimeException("Card not found with id: " + cardId));

        return map(card);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardResponse> getAll(int page) {

        Pageable pageable = PageRequest.of(page,10,Sort.by("cardId").descending());

        return repository.findAll(pageable)
                .map(this::map);
    }

    @Override
    @Transactional
    public void delete(Long cardId) {

        Card card = repository.findById(cardId).orElseThrow(() ->
                        new RuntimeException("Card not found with id: " + cardId));

        repository.delete(card);
    }

    /*
    Mapper
     */
    private CardResponse map(Card card) {

        return CardResponse.builder()
                .cardId(card.getCardId())
                .cardNumber(card.getCardNumber())
                .cardHolder(card.getCardHolder())
                .cardType(card.getCardType())
                .creditLimit(card.getCreditLimit())
                .status(card.getStatus())
                .build();
    }
}
