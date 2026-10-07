package CardsAPI.Controllers;

import CardsAPI.Services.Interface.IExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/exchange-rate")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final IExchangeRateService service;

    @GetMapping
    public Map<String, Object> getRate(@RequestParam(defaultValue = "USD") String from, @RequestParam(defaultValue = "MYR") String to) {
        return service.getRate(from, to);
    }
}
