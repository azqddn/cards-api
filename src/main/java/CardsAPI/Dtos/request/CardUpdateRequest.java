package CardsAPI.Dtos.request;

import CardsAPI.Enums.CardStatus;
import CardsAPI.Enums.CardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardUpdateRequest {

    @NotBlank(message = "Card holder is required")
    @Size(max = 100)
    private String cardHolder;

    @NotNull(message = "Card type is required")
    private CardType cardType;

    @NotNull(message = "Credit limit is required")
    @Positive(message = "Credit limit must be greater than 0")
    private BigDecimal creditLimit;

    @NotNull(message = "Status is required")
    private CardStatus cardStatus;
}
