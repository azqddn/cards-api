package CardsAPI.Dtos.request;

import lombok.Data;

@Data
public class CardUpdateRequest {
    private String cardHolder;
    private String cardType;
    private Double creditLimit;
    private String status;
}
