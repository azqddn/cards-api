package CardsAPI.Services.Interface;

import java.util.Map;

public interface IExchangeRateService {
    public Map<String, Object> getRate(String from, String to);
}
