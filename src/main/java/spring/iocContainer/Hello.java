package spring.iocContainer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Hello {

    //디폴트 값. 초기화를 해뒀기 때문에 name 프로퍼티를 설정하지 않아도 사용할 수 있으며
    //디폴트 값을 변경하고 싶을 땐 언제든지 프로퍼티의 값을 선언해서 변경 가능
    String name = "EveryOne";

    //자바 코드 외부의 리소스나 환경 정보를 담은 프로퍼티 값 호출
    //@Value("#{systemProperties['os.name']}")
    String osName;

    //환경 정보를 담은 프로퍼티 파일의 값 호출
    //@Value("${database.username}")
    String username;

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

    //<property value="EveryOne"> 와 동일
    @Value("EveryOne")
    public void setName(String name) {
        this.name = name;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }
}
