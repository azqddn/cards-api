package CardsAPI.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final int MAX_BODY_LENGTH = 5000;

    private static final Pattern PAN_JSON =
            Pattern.compile("(\"cardNumber\"\\s*:\\s*\")(\\d{6})\\d+(\\d{4})(\")");

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {

        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);
        response.setHeader("X-Correlation-Id", correlationId);

        ContentCachingRequestWrapper req = new ContentCachingRequestWrapper(request, MAX_BODY_LENGTH);
        ContentCachingResponseWrapper res = new ContentCachingResponseWrapper(response);
        long start = System.currentTimeMillis();

        try {
            chain.doFilter(req, res);
        } finally {
            long duration = System.currentTimeMillis() - start;
            String query = req.getQueryString() == null ? "" : "?" + req.getQueryString();

            log.info("REQUEST  | {} {}{} | body={}",
                    req.getMethod(), req.getRequestURI(), query,
                    toLogBody(req.getContentAsByteArray()));

            log.info("RESPONSE | {} {}{} | status={} | {}ms | body={}",
                    req.getMethod(), req.getRequestURI(), query,
                    res.getStatus(), duration,
                    toLogBody(res.getContentAsByteArray()));

            res.copyBodyToResponse();
            MDC.clear();
        }
    }

    private String toLogBody(byte[] content) {
        if (content.length == 0) return "";
        String body = new String(content, StandardCharsets.UTF_8).replaceAll("\\s+", " ");
        body = PAN_JSON.matcher(body).replaceAll("$1$2******$3$4");
        return body.length() > MAX_BODY_LENGTH
                ? body.substring(0, MAX_BODY_LENGTH) + "...(truncated)"
                : body;
    }
}
