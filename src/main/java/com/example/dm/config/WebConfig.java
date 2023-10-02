package com.example.dm.config;

import com.example.dm.resolver.ApiResponseResolver;
import com.example.dm.util.TxidGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public TxidGenerator txidGenerator(){
        return new TxidGenerator();
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        handlers.add(new ApiResponseResolver(txidGenerator()));
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor = null;
        ApiResponseResolver apiResponseResolver = null;

        for (HandlerExceptionResolver resolver : resolvers) {
            if (resolver instanceof ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver) {
                HandlerMethodReturnValueHandlerComposite handlerMethodReturnValueHandlerComposite = exceptionHandlerExceptionResolver.getReturnValueHandlers();
                if (handlerMethodReturnValueHandlerComposite == null) {
                    continue;
                }

                for (HandlerMethodReturnValueHandler handler : handlerMethodReturnValueHandlerComposite.getHandlers()) {
                    if (handler instanceof RequestResponseBodyMethodProcessor processor) {
                        requestResponseBodyMethodProcessor = processor;
                    }
                    if (handler instanceof ApiResponseResolver responseResolver) {
                        apiResponseResolver = responseResolver;
                    }
                }

                if (apiResponseResolver != null) {
                    apiResponseResolver.setRequestResponseBodyMethodProcessor(requestResponseBodyMethodProcessor);
                }
            }
        }
    }
}
