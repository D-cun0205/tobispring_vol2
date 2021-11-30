package spring.iocContainer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelloConfig {

    @Bean
    public Hello hello() {
        Hello hello = new Hello();
        hello.setName("Spring");
        hello.setPrinter(printer());
        return hello;
    }

    @Bean
    public Hello hello2() {
        Hello hello = new Hello();
        hello.setName("Spring2");
        hello.setPrinter(printer());
        return hello;
    }

    //Single tone으로 생성되어 hello, hello2 빈에 같은 주소값의 printer 인스턴스가 주입
    @Bean
    public Printer printer() {
        return new StringPrinter();
    }
}
