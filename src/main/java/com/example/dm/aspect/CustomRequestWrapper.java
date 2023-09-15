package com.example.dm.aspect;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class CustomRequestWrapper extends HttpServletRequestWrapper {
    private final Charset encoding;
    private final byte[] rawData;

    public CustomRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        String characterEncoding = request.getCharacterEncoding();
        if (Strings.isBlank(characterEncoding)) {
            characterEncoding = StandardCharsets.UTF_8.name();
        }
        this.encoding = Charset.forName(characterEncoding);

        try {
            InputStream inputStream = request.getInputStream();
            this.rawData = toByteArray(inputStream);
            String requestBody = new String(rawData, StandardCharsets.UTF_8);

            writeRequestLogs(request, requestBody);

        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 공통 Request 로그 작성
     */
    private void writeRequestLogs(HttpServletRequest request, String requestBody) {
        String txid = (String) request.getAttribute("txid");
        String publicIp = (String) request.getAttribute("publicIp");
        String userAgent = request.getHeader("user-agent");
        String requestMethod = request.getMethod();
        String servletPath = request.getServletPath();
        String queryString = !Strings.isEmpty(request.getQueryString()) ? "?" + request.getQueryString() : "";

        log.info("-------------------------------------");
        log.info("[Request] => {}", txid);
        log.info("{} {} {}", txid, requestMethod, servletPath + queryString);
        log.info("{} public ip:{}", txid, publicIp);
        log.info("{} user-agent:{}", txid, userAgent);
        log.info("{} request body=[{}]", txid, requestBody);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rawData);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), this.encoding));
    }

    @Override
    public ServletRequest getRequest() {
        return super.getRequest();
    }

    /**
     * InputStream -> byte[] 변환
     */
    private byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];

        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();

        return buffer.toByteArray();
    }
}

