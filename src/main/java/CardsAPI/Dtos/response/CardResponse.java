package CardsAPI.Dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardResponse {
    private Long cardId;
    private String cardNumber;
    private String cardHolder;
    private String cardType;
    private Double creditLimit;
    private String status;
}