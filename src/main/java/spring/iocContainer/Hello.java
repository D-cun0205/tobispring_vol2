package spring.iocContainer;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Hello {

    String name;

    //필드에 선언하면 수정자 메서드가 없어도 스프링이 필드에 DI 주입 (필드 주입)
    //Resource(name = "") name 속성을 지정하지 않고 타입으로 찾는 방법도 있는데 권장하지 않음
    //ApplicationContext를 직접 호출하는 경우에 사용
    @Resource(name = "printer") //참조할 빈의 이름을 지정, 생략 가능
    Printer printer;

    public String sayHello() {
        return "Hello " + name;
    }

    public void print() {
        this.printer.print(sayHello());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }
}
