package com.example.dm.aspect;

import com.example.dm.util.TxidGenerator;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Slf4j
@WebFilter(filterName = "LoggingFilter", urlPatterns = "/api/v1/*")
public class LoggingFilter implements Filter {

    private final TxidGenerator txidGenerator;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // txid 및 IP 셋팅
        initTxid((HttpServletRequest) request);
        initPublicIp((HttpServletRequest) request);

        CustomRequestWrapper requestWrapper = new CustomRequestWrapper((HttpServletRequest) request);
        CustomResponseWrapper responseWrapper = new CustomResponseWrapper((HttpServletResponse) response);

        filterChain.doFilter(requestWrapper, responseWrapper);

        String responseBody = new String(responseWrapper.getDataStream(), StandardCharsets.UTF_8);
        writeResponseLogs((HttpServletRequest) request, (HttpServletResponse) response, responseBody, stopWatch);
        response.getOutputStream().write(responseBody.getBytes());
    }

    /**
     * 메소드 실행 후 Response 로그 작성
     */
    private void writeResponseLogs(HttpServletRequest request, HttpServletResponse response, String responseBody, StopWatch stopWatch) {
        String txid = (String) request.getAttribute("txid");
        String requestMethod = request.getMethod();
        String servletPath = request.getServletPath();
        String queryString = !Strings.isEmpty(request.getQueryString()) ? "?" + request.getQueryString() : "";
        int statusCode = response.getStatus();

        if (statusCode >= 400) { // 오류
            log.error("-------------------------------------");
            log.error("[Response] => {}", txid);
            log.error("{} [Exception] {} {} {}", txid, statusCode, requestMethod, servletPath + queryString);
            log.error("{} response body=[{}]", txid, responseBody);

        } else { // 성공
            log.info("-------------------------------------");
            log.info("[Response] => {}", txid);
            log.info("{} [Success] {} {} {}", txid, statusCode, requestMethod, servletPath + queryString);
            log.info("{} response body=[{}]", txid, responseBody);
        }

        stopWatch.stop();
        long durationMs = stopWatch.getTotalTimeMillis();

        log.info("{} duration time:{}ms", txid, durationMs);
        log.info("-------------------------------------");
    }

    private void initTxid(HttpServletRequest request) {
        request.setAttribute("txid", txidGenerator.getTxid());
    }

    private void initPublicIp(HttpServletRequest request) {
        String publicIp = request.getRemoteAddr();
        request.setAttribute("publicIp", publicIp);
    }

}
