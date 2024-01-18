package org.zerock.b01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // application의 listener가 있는 것이 있으면 여기서 실행됨
public class B01Application {

    public static void main(String[] args) {
        SpringApplication.run(B01Application.class, args);




    }
// main 페이지 -> 시작하면 main 페이지가 실행됨. run -> B01Application.class
}
