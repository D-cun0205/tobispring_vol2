package spring.iocContainer;

import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.web.context.WebApplicationContext;

public class SpringConfig {

    /**
     * IOC 컨테이너로 동작하기 위한 POJO Class, Meta 설정 정보 필요, 테스트에 사용
     * 코드에 의해 설정 메타 정보를 등록하는 기능을 제공하는 어플리케이션 컨텍스트
     * POJO Class : 각자 기능에 충실하게 독립적으로 설계된 클래스
     * Meta 설정 정보 : 스프링 컨테이너가 관리하는 빈을 어떻게 만들고 동작하게 할 것 인지 에 대한 정보
     */
    StaticApplicationContext staticApplicationContext = new StaticApplicationContext();

    /**
     * 가장 일반적인 어플리케이션 컨텍스트 구현 클래스, 어플리케이션 컨텍스트의 모든 기능을 갖추고 있음
     * 컨테이너의 주요 기능을 DI를 통해 확장 가능능
     * XML 파일과 같은 외부의 리소스에 있는 빈 설정 메타정보를 리더를 통해 읽어서 메타정보 전환 및 사용
     * XMLBeanDefinitionReader로 XML 메타 설정 파일을 읽어서 Context가 빈을 관리할수 있게 돕는다
    */
    GenericApplicationContext genericApplicationContext = new GenericApplicationContext();

    /**
     * XmlBeanDefinitionReader 내장하고 있는 GenericApplicationContext
     * 생성자 매개변수에 인자로 XML File path를 등록하면 파일을 읽고 초기화까지 진행
     */
    GenericXmlApplicationContext genericXmlApplicationContext = new GenericXmlApplicationContext();

    /**
     * 웹 환경에서 사용할 때 필요한 기능이 추가된 어플리케이션 컨텍스트
     * XML을 설정파일을 사용할 때 XmlWebApplicationContext
     * 어노테이션을 사용할 때 AnnotationConfigWebApplicationCntext
     */
    WebApplicationContext webApplicationContext = null;
}
