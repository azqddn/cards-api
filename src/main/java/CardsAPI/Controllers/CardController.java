package CardsAPI.Controllers;

import CardsAPI.Dtos.request.CardCreateRequest;
import CardsAPI.Dtos.request.CardUpdateRequest;
import CardsAPI.Dtos.response.CardResponse;
import CardsAPI.Dtos.response.PagedResponse;
import CardsAPI.Services.Interface.ICardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final ICardService service;
    @PostMapping
    public ResponseEntity<CardResponse> create(@Valid @RequestBody CardCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public CardResponse update(@PathVariable UUID id, @Valid @RequestBody CardUpdateRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/{id}")
    public CardResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping
    public PagedResponse<CardResponse> getAll(@RequestParam(defaultValue = "0") @Min(0) int page) {
        return service.getAll(page);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
