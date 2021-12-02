import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spring.iocContainer.*;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContextTest.class)
public class ApplicationContextTest {

    @Test
    public void appContextTest() {

        //어플리케이션 컨텍스트 생성
        StaticApplicationContext ac = new StaticApplicationContext();

        /** registerSingleton을 이용한 빈 등록 */
        //어플리케이션 컨텍스트에서 관리하는 빈으로 등록
        ac.registerSingleton("hello1", Hello.class);

        //어플리케이션 컨텍스트에서 관리하는 빈을 호출하여 인스턴스 객체 생성
        Hello hello1 = ac.getBean("hello1", Hello.class);
        //인스턴스 객체가 정상적으로 생성되었는지 체크
        assertThat(hello1, is(notNullValue()));

        /** BeanDefinition을 이용한 빈 등록 */
        //빈 메타정보를 담은 오브젝트 생성, 빈 클래스는 Hello, ex)<Bean class="패키지.Hello" />
        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        //빈에 선언 되어있는 name 프로퍼티에 값 지정, <property name="name" value="Spring />
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        //위에서 설정한 빈 메타정보를 이용해서 hello2 이름을 가진 빈으로 등록 <bean id="hello2" .. />
        ac.registerBeanDefinition("hello2", helloDef);

        Hello hello2 = ac.getBean("hello2", Hello.class);

        assertThat(hello1, is(not(hello2)));

        assertThat(ac.getBeanFactory().getBeanDefinitionCount(), is(2));
    }

    @Test
    public void registerBeanWithDependency() {
        StaticApplicationContext ac = new StaticApplicationContext();
        ac.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class));

        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        helloDef.getPropertyValues().addPropertyValue("printer", new RuntimeBeanReference("printer"));

        ac.registerBeanDefinition("hello", helloDef);

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString(), is("Hello Spring"));
    }

    @Test
    public void genericApplicationContext() {
        GenericApplicationContext ac = new GenericApplicationContext();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(ac);
        reader.loadBeanDefinitions("genericApplicationContext.xml");
        ac.refresh();

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString(), is("Hello Spring"));
    }

    @Test
    public void parentChildApplicationContext() {
        ApplicationContext parent = new GenericXmlApplicationContext("parentContext.xml");
        GenericApplicationContext child = new GenericApplicationContext(parent);
        XmlBeanDefinitionReader reader =  new XmlBeanDefinitionReader(child);
        reader.loadBeanDefinitions("childContext.xml");

        Printer printer = child.getBean("printer", Printer.class);
        assertThat(printer, is(notNullValue()));

        Hello hello = child.getBean("hello", Hello.class);
        hello.print();

        assertThat(printer.toString(), is("Hello Spring"));
    }

    //prototype으로 설정하면 매번 객체를 새로 생성
    @Scope("prototype")
    static class SingletonBean {}
    static class SingletonClientBean {
        @Autowired SingletonBean bean1;
        @Autowired SingletonBean bean2;
    }

    @Test
    public void singletonScope() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class, SingletonClientBean.class);
        Set<SingletonBean> beans = new HashSet<>();
        beans.add(ac.getBean(SingletonBean.class));
        beans.add(ac.getBean(SingletonBean.class));
        assertThat(beans.size(), is(2));

        beans.add(ac.getBean(SingletonClientBean.class).bean1);
        beans.add(ac.getBean(SingletonClientBean.class).bean2);
        assertThat(beans.size(), is(4));
    }

    @Test
    public void applicationContextSetTest() {
        GenericXmlApplicationContext xmlContext = new GenericXmlApplicationContext();
        xmlContext.getEnvironment().setPlaceholderPrefix("dev");
    }
}
