package CardsAPI.Services.Implementation;

import CardsAPI.Services.Interface.IExchangeRateService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Map;

@Service
@Slf4j
public class ExchangeRateService implements IExchangeRateService {

    private final RestClient restClient = RestClient.create("https://open.er-api.com");

    public Map<String, Object> getRate(String from, String to) {
        log.info("Calling 3rd party API: base={}", from);

        ExchangeRateApiResponse response = restClient.get()
                .uri("/v6/latest/{base}", from)
                .retrieve()
                .body(ExchangeRateApiResponse.class);

        if (response == null || response.rates() == null || !response.rates().containsKey(to)) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Exchange rate not available for " + to);
        }

        return Map.of("from", from, "to", to, "rate", response.rates().get(to));
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ExchangeRateApiResponse(String result, Map<String, BigDecimal> rates) {}
}
