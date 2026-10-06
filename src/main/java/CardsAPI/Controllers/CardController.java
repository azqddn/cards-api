package CardsAPI.Controllers;

import CardsAPI.Dtos.request.CardCreateRequest;
import CardsAPI.Dtos.request.CardUpdateRequest;
import CardsAPI.Dtos.response.CardResponse;
import CardsAPI.Services.Interface.ICardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final ICardService cardService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardResponse create(@RequestBody CardCreateRequest request) {
        return cardService.create(request);
    }

    @PutMapping("/{cardId}")
    public CardResponse update(@PathVariable Long cardId, @RequestBody CardUpdateRequest request) {

        return cardService.update(cardId, request);
    }

    @GetMapping("/{cardId}")
    public CardResponse getById(@PathVariable Long cardId) {

        return cardService.getById(cardId);
    }

    @GetMapping
    public Page<CardResponse> getAll(@RequestParam(defaultValue = "0") int page) {

        return cardService.getAll(page);
    }

    @DeleteMapping("/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long cardId) {

        cardService.delete(cardId);
    }
}
