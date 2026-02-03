package com.example.app.logging.interceptor;

import com.example.app.logging.filter.MdcFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

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
        long times = System.currentTimeMillis() - start;

        Map<String, Object> params = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> params.put(k, v.length == 1 ? v[0] : v));

        access.info(
                "method={}, path={}, status={}, times={}, traceId={}, params={}",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                times,
                MDC.get(MdcFilter.TRACE_ID),
                params
        );
    }
}
