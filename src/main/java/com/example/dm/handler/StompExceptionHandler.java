package com.example.dm.handler;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Component
public class StompExceptionHandler extends StompSubProtocolErrorHandler {

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        Throwable exception = convertThrowException(ex);

        return handlerException(clientMessage, exception);
    }

    private Throwable convertThrowException(Throwable exception) {
        if (exception instanceof MessageDeliveryException) {
            return exception.getCause();
        }

        return exception;
    }

    private Message<byte[]> handlerException(Message<byte[]> clientMessage, Throwable ex) {
        return prepareErrorMessage(clientMessage, ex.getMessage(), HttpStatus.UNAUTHORIZED.name());
    }

    private Message<byte[]> prepareErrorMessage(Message<byte[]> clientMessage, String message, String errorCode) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setMessage(errorCode);
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(
                message.getBytes(StandardCharsets.UTF_8),
                accessor.getMessageHeaders()
        );
    }

    @Override
    protected Message<byte[]> handleInternal(StompHeaderAccessor errorHeaderAccessor, byte[] errorPayload, Throwable cause, StompHeaderAccessor clientHeaderAccessor) {
        return MessageBuilder.createMessage(errorPayload, errorHeaderAccessor.getMessageHeaders());
    }
}
