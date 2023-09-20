package com.example.dm.security;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "url.permit")
public class PermitUrlProperties {
  @Value("${api.path.default}")
  private String API_URL_PREFIX;

  private List<String> get;
  private List<String> prefixGet;
  private List<String> prefixPost;

  List<String> list = new ArrayList<>();

  public List<String> getGet(){
    list = new ArrayList<>();
    for(String s:prefixGet){
      list.add(API_URL_PREFIX+s);
    }
    list.addAll(get);
    return list;
  }

  public List<String> getPost(){
    list = new ArrayList<>();
    for(String s:prefixPost){
      list.add(API_URL_PREFIX+s);
    }
    return list;
  }
}
