package spring.iocContainer;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/** 커스텀 스테레오타입 어노테이션 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface BusinessRule {
    
    String value() default ""; //빈의 아이디를 직접 지정할 수 있도록 디폴트 엘리먼트 선언
}
