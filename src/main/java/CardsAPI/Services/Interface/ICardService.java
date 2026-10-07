package CardsAPI.Services.Interface;

import CardsAPI.Dtos.request.CardCreateRequest;
import CardsAPI.Dtos.request.CardUpdateRequest;
import CardsAPI.Dtos.response.CardResponse;
import CardsAPI.Dtos.response.PagedResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ICardService {
    CardResponse create(CardCreateRequest request);
    CardResponse update(UUID cardId, CardUpdateRequest request);
    CardResponse getById(UUID cardId);
    PagedResponse<CardResponse> getAll(int page);
    void delete(UUID cardId);
}
