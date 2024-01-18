package org.zerock.b01.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api(){// 설정값을 가지고 만들어지는 클래스
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)//404에러 났을때 번호 바로 볼수 있는데 그거 말고 내가 주는 형태로 문서결과값이 나올수있게
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.zerock.b01.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Boot B01 project Swagger")
                .build();
    }
}
