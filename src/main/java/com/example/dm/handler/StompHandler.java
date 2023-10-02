package com.example.dm.handler;

import com.example.dm.entity.LoginUser;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.BusinessException;
import com.example.dm.repository.ChatRoomsRepository;
import com.example.dm.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final TokenProvider tokenProvider;

    private final ChatRoomsRepository chatRoomsRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            String accessToken = resolveToken(accessor.getFirstNativeHeader("Authorization"));
            Long roomId = Long.valueOf(accessor.getFirstNativeHeader("Room-id"));

            tokenProvider.validateToken(accessToken);
            tokenProvider.expiredToken(accessToken);

            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            LoginUser principal = (LoginUser) authentication.getPrincipal();


            verifyJoinUser(roomId, principal.getId());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return message;

    }

    private String resolveToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void verifyJoinUser(Long roomId, Long userId) {
        if (!chatRoomsRepository.findUserByRoomId(roomId).stream().anyMatch(chatRoom -> chatRoom.getId().equals(userId)))
            throw new BusinessException(ApiResultStatus.USER_NOT_EXIST_ROOM);
    }

}
