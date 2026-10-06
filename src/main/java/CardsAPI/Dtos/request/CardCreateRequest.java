package CardsAPI.Dtos.request;

import lombok.Data;

@Data
public class CardCreateRequest {
    private String cardNumber;
    private String cardHolder;
    private String cardType;
    private Double creditLimit;
    private String status;
}
