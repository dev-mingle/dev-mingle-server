package com.example.dm.security.jwt;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.dm.dto.users.SignupDto;
import com.example.dm.entity.LoginUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("토큰 필터 테스트")
public class TokenFilterTest {
  @Resource
  MockMvc mockMvc;
  @Resource
  ObjectMapper objectMapper;

  @Autowired
  TokenFilter tokenFilter;

  private TokenProvider tokenProvider;
  private String token;

  @BeforeEach
  public void setUp() {
    tokenProvider = mock(TokenProvider.class);
    token = tokenProvider.generateAccessToken(mock(LoginUser.class));
  }

  @Test
  @DisplayName("토큰 검증이 필요하지 않은 엔드포인트")
  public void doFilterTestWithToken() throws Exception {
    mockMvc.perform(get("/actuator/health"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("토큰 검증이 필요한 엔드포인트")
  public void doFilterTestWithoutToken() throws Exception {
    SignupDto dto = makeSignDto();

    ResultActions resultActions = mockMvc.perform(
        post("/api/v1/users")
            .header("Authorization", "Bearer " + token)
            .header("Authorization-refresh", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
    );

    resultActions
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(200))
        .andExpect(jsonPath("$.data.createdAt").isNotEmpty())
        .andDo(print());
  }

  // 유효성 필요로 테스트용 객체 생성
  private SignupDto makeSignDto() {
    return new SignupDto("devmingle11@gmail.com","Password11#",
                          "nickname",
                          "City", "State", "Street",
                          52.65165, 65.8451321,
                          "introduce",
                          "localhost:8080","localhost",
                          "email","");
  }
}
