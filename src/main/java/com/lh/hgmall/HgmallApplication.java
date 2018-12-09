package com.lh.hgmall;

import com.lh.hgmall.util.PortUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

@SpringBootApplication
@ServletComponentScan
@EnableCaching
public class HgmallApplication extends SpringBootServletInitializer {

    static {
        PortUtil.checkPort(6379,"Redis",true);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HgmallApplication.class);
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return (container -> {
            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/exception/401.html");
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/exception/401.html");
            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/exception/401.html");

            container.addErrorPages(error401Page, error404Page, error500Page);
        });
    }
    public static void main(String[] args) {
        SpringApplication.run(HgmallApplication.class, args);
    }
}
