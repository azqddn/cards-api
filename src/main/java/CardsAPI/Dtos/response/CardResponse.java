package CardsAPI.Dtos.response;

import CardsAPI.Enums.CardStatus;
import CardsAPI.Enums.CardType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class CardResponse {
    private UUID cardId;
    private String cardNumber;
    private String cardHolder;
    private CardType cardType;
    private BigDecimal creditLimit;
    private CardStatus cardStatus;
}