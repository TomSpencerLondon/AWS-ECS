package com.example.ecspractice;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @GetMapping("/hello")
  public String hello() {
    String message = "Hello AWS!";
    try {
      InetAddress ip = InetAddress.getLocalHost();
      message += " From host: " + ip;
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    return message;
  }

}
