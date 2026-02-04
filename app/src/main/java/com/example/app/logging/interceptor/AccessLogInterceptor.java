package com.example.app.logging.interceptor;

import com.example.app.logging.event.AccessLogEvent;
import com.example.app.logging.event.AppLogEvent;
import com.example.app.logging.logger.LogEventLogger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AccessLogInterceptor implements HandlerInterceptor {
    private static final Logger access = LoggerFactory.getLogger("ACCESS_LOG");
    private static final String START = "startMs";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        long start = (long) request.getAttribute(START);
        long elapsed = System.currentTimeMillis() - start;
        AppLogEvent log = new AccessLogEvent(
                "ACCESS LOG",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                elapsed
        );
        LogEventLogger.log(log);
    }
}
