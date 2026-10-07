package CardsAPI.Services.Implementation;

import CardsAPI.Dtos.request.CardCreateRequest;
import CardsAPI.Dtos.request.CardUpdateRequest;
import CardsAPI.Dtos.response.CardResponse;
import CardsAPI.Dtos.response.PagedResponse;
import CardsAPI.Entities.Card;
import CardsAPI.Enums.CardStatus;
import CardsAPI.Repositories.CardRepository;
import CardsAPI.Services.Interface.ICardService;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService implements ICardService {

    private static final int PAGE_SIZE = 10;
    private final CardRepository repository;

    @Override
    @Transactional
    public CardResponse create(CardCreateRequest request) {
        if (repository.existsByCardNumberAndDeletedDateIsNull(request.getCardNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Card number already exists");
        }

        Card card = Card.builder()
                .cardNumber(request.getCardNumber())
                .cardHolder(request.getCardHolder())
                .cardType(request.getCardType())
                .creditLimit(request.getCreditLimit())
                .cardStatus(request.getCardStatus())
                .createdDate(LocalDateTime.now())
                .build();

        return map(repository.save(card));
    }

    @Override
    @Transactional
    public CardResponse update(UUID cardId, CardUpdateRequest request) {
        Card card = findActiveCard(cardId);

        card.setCardHolder(request.getCardHolder());
        card.setCardType(request.getCardType());
        card.setCreditLimit(request.getCreditLimit());
        card.setCardStatus(request.getCardStatus());
        card.setUpdatedDate(LocalDateTime.now());

        return map(card);
    }

    @Override
    @Transactional(readOnly = true)
    public CardResponse getById(UUID cardId) {
        return map(findActiveCard(cardId));
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<CardResponse> getAll(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Order.desc("createdDate"), Sort.Order.desc("cardId")));
        Page<Card> result = repository.findAllByDeletedDateIsNull(pageable);

        return PagedResponse.<CardResponse>builder()
                .content(result.getContent().stream().map(this::map).toList())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    public void delete(UUID cardId) {
        Card card = findActiveCard(cardId);
        card.setDeletedDate(LocalDateTime.now());
        card.setCardStatus(CardStatus.CLOSED);
        card.setUpdatedDate(LocalDateTime.now());
    }


    /* ---------- helpers ---------- */
    private Card findActiveCard(UUID cardId) {
        return repository.findByCardIdAndDeletedDateIsNull(cardId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Card not found with id: " + cardId));
    }

    private CardResponse map(Card card) {
        return CardResponse.builder()
                .cardId(card.getCardId())
                .cardNumber(maskCardNumber(card.getCardNumber()))
                .cardHolder(card.getCardHolder())
                .cardType(card.getCardType())
                .creditLimit(card.getCreditLimit())
                .cardStatus(card.getCardStatus())
                .build();
    }

    private String maskCardNumber(String number) {
        if (number == null || number.length() < 10) return number;
        return number.substring(0, 6)
                + "*".repeat(number.length() - 10)
                + number.substring(number.length() - 4);
    }
}
