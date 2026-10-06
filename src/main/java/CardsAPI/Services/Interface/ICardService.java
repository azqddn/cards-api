package CardsAPI.Services.Interface;

import CardsAPI.Dtos.request.CardCreateRequest;
import CardsAPI.Dtos.request.CardUpdateRequest;
import CardsAPI.Dtos.response.CardResponse;
import org.springframework.data.domain.Page;

public interface ICardService {
    CardResponse create(CardCreateRequest request);

    CardResponse update(Long cardId, CardUpdateRequest request);

    CardResponse getById(Long cardId);

    Page<CardResponse> getAll(int page);

    void delete(Long cardId);
}
