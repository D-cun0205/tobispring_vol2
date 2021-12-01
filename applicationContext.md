###Application Context

    ListableBeanFactory(나열 가능한 빈 팩토리), HierachicalBeanFactory(계층적 빈 팩토리) 두 개의 인터페이스를 상속
    위 두 개의 인터페이스는 BeanFactory 인터페이스의 서브 인터페이스 이므로 BeanFactory를 상속하고 있는 샘
    
###BeanDefinition Interface
스프링 IoC 컨테이너는 각 빈에 대한 정보를 담은 설정 메타 정보를 읽어들여  
빈 오브젝트를 생성하고 프로퍼티나 생성자를 통해 의존 오브젝트를 주입해주는 DI작업 수행

    IoC Container가 사용하는 빈 메타 정보
    빈 아이디, 이름, 별칭 : 빈 오브젝트를 구분할 수 있는 식별자
    클래스 또는 클래스 이름 : 빈으로 만들 POJO 클래스 또는 서비스 클래스 정보
    스코프 : 싱글톤, 프로토타입과 같은 빈의 생성 방식과 존재 범위
    프로퍼티 값 또는 참조 : 의존성 주입(DI)에 사용할 프로퍼티 이름과 값 또는 참조하는 빈의 이름
    생성자 파라미터 값 또는 참조 : 의존성 주입에 사용할 생성자 파라미터 이름과 값 또는 참조할 빈의 이름
    지연된 로딩 여부, 우선 빈 여부, 자동와이어링 여부, 부모 빈 정보, 빈 팩토리 이름

###WebApplicationContext

    웹 어플리케이션에서 만들어지는 스프링 IoC 컨테이너는 WebApplicationContext 인터페이스 구현체
    웹 모듈에 대한 정보에 접근 가능

###계층적 Application Context

    부모/자식 의 관계로 Application Context를 설정할 수 있으며 자식은 부모에 접근할 수 있지만 부모는 자식에게 접근할 수 없다
    자식 어플리케이션 컨텍스트가 우선시 되어 자신과 부모에 동일한 이름의 빈이 선언되어있으면 부모의 빈은 무시되고
    자신의 빈을 사용하고 없는 경우 부모의 빈을 사용

###웹 어플리케이션의 IoC 컨테이너 구성

    프론트 컨트롤러 패턴 : 몇개 의 서블릿이 중앙집중식으로 모든 요청을 다 받아서 처리하는 방식

웹 어플리케이션의 컨텍스트 구성 방법

    서블릿 컨텍스트와 루트 어플리케이션 컨텍스트 계층구조
    

일반 클래스안에 @Bean 설정시 주의 사항

    @Configuration 안에 @Bean을 사용해서 생성된 빈들은 ex)1번 빈 안에서 호출로 2번이나 다른 빈들을 호출해서 싱글톤으로 같은 객체를 반환
    일반 클래스 안에 @Bean을 위와 동일하게 호출하면 싱글톤을 보장할 수 없어서 문제가 될 수 있다.
    아래 예제로 디테일한 확인

@Configuration이 선언 되어 있을 때
```java
        @Configuration
        public class HelloService {

            @Bean
            public Hello hello() {
                Hello hello = new Hello();
                /**
                 @Configuration -> @Bean은 싱글톤을 보장하여 문제가 되지 않음
                 @Configuration 선언 되지 않은 일반클래스에서는 싱글톤을 보장하지 않는다
                 */
                hello.setPrinter(printer());
            }

            @Bean
            public Printer printer() {
                return new StringPrinter();
    }
}
```

@Configuration이 선언 되어 있지 않을 때
```java
public class HelloService {
    private Printer printer;
    
    public void setPrinter(Printer printer) {
        this.printer = printer;
    }
    
    @Bean
    private Hello hell() {
        Hello hello = new Hello();
        //내부에서 사용할 때도 빈에 직접 접근하지 않고 DI를 통한 참조하도록 주의
        hello.setPrinter(this.printer);
    }
    
    //외부에서 빈에 직접 접근하지 못하도록 접근제한자 private 설정
    @Bean
    private Printer printer() {
        return new StringPrinter();
    }
}
```

###빈 의존관계 설정 방법

    DI할 빈의 아이디를 직접 지정하는 방법
    타입 비교를 통해서 호환되는 타입의 빈을 DI후보로 삼는 방법(오토와이어링)
    메타정보 작성 방법 분류 : XML <bean> 태그, XML 스키마를 가진 전용 태그, 어노테이션, 자바 코드를 통한 DI 주입

<bean>을 이용한 DI 지정 2가지 방법 (<property>, <constructor-arg>)

    <property> : 수정자 메소드 (주로 사용)
        name = 사용 명, value = String, int, ref = 참조 객체 
    <constructor-arg> : 빈 클래스의 생성자
        index = 순서, name = 사용 명, type = 중복되는 타입이 없는 경우 사용, value = 값, ref = 참조 객체

자동와이어링 (자동으로 관계가 맺어져야 할 빈을 찾아서 연결해준다는 의미)

    자동와이어링은 타입 또는 이름 하나만 설정할 수 있으므로 프로젝트 특성에 맞는 자동와이어링 설정과 프로퍼티를 같이 사용하여 문제 해결
    미리 정해둔 규칙을 이용해 자동으로 DI 설정을 컨테이너가 추가
    XML autowired="byName" : 빈 이름 자동와이어링
        ex)빈 안에있는 필드에 setMethod 가 있으면서 Method 라는 이름으로 등록 된 빈이 있으면 자동 DI 주입
    <beans> 태그 안에 속성값으로 default-autowire="byName"을 지정하면 모든 빈에 자동와이어링 실행

    XML autowired="byType" : 타입에 의한 자동와이어링
        프로퍼티의 타입과 빈의 타입을 비교해서 자동 연결
        <bean id="hello" class"...Hello" autowire="byType"></bean>
        <bean id="myPrinter" class="...StringPrinter"></bean>
        주의 : 타입에 의한 오토와이어링은 타입이 같은 빈이 두 개 이상 존재하면 적용되지 않음

    property가 아닌 생성자에 의한 autowire 사용 방법은 autowire="constructor" 지정
    빈 스캐닝을 통해 등록되는 빈은 XML로 <property> 지정이나 자동와이어링이 불가 반대로는 사용 가능

@Resource : XML 대신 어노테이션을 이용해 빈의 의존관계를 정의할 수 있는 방법 두 가지

    <property> 와 같은 주입할 빈을 아이디로 지정하는 방법, 수정자 메서드 또는 필드에 설정

    어노테이션 같은 의존관계 정보를 이용해서 DI가 이뤄지게 하기 위한 설정
    1.<context:annotation-config /> : 어노테이션 의존 관계 정보를 읽어서 메타정보를 추가해주는 빈 후처리기 등록
        빈 후처리기 : 빈 설정을 후처리하여 빈 생명 주기, 빈 팩토리 생명 주기 관여
                    빈의 초기화 전, 후 기회 제공
                    빈 프로퍼티의 유효성 검사
                    선행, 후행으로 afterPropertiesSet()과 init-method()가 호출

    2.<context:component-scan /> : 빈 스캐닝을 통한 빈 등록 방법을 지정
        내부적으로 첫 번째 태그로 만들어지는 빈을 함께 등록
    3.AnnotationConfigApplicationContext or AnnotationConfigWebApplicationContext : 1,2번을 내장한 어플리케이션 컨텍스트 사용
        
    필드에 선언하면 수정자 메서드가 없어도 스프링이 필드에 DI 주입 (필드 주입)
    Resource(name = "") name 속성을 지정하지 않고 타입으로 찾는 방법도 있는데 권장하지 않음
    ApplicationContext를 직접 호출하는 경우에 타입으로 찾는 방법 사용
    참조할 빈의 이름을 지정, 생략 가능

@Autowired(spring 2.5버전 적용된 전용 어노테이션)

    타입에 의한 자동와이어링 방식
    @Autowired : XML 타입에 의한 자동와이어링 방식 생성자, 필드, 수정자 메소드, 일반 메소드
        @Resource와 차이점으로 필드나 프로퍼티 타입을 이용해 빈을 찾음
    
생성자 주입 @Autowired
```java
public class A {
    protected B b;
    protected C c;
    
    //필드나 수정자메서드를 사용하면 2개의 @Autowired를 사용해야 하지만 생성자로 할 경우 @Autowired 1개로 해결 가능
    //생성자에 @Autowired 주입 시 하나의 생성자에만 적용 가능
    @Autowired
    public A(B b, C c) {
        this.b = b;
        this.c = c;
    }
}
```

    특정 인터페이스를 구현한 빈이 2개인 경우 @Autowired로 받을 때 Collection, Set, List로 타입을 선언해서 DI 받을 수 있다
    DI 할 빈의 타입이 컬렉션인 경우 @Autowired로 설정이 불가능하고 @Resource를 이용해야 한다

@Qualifier : 타입 외의 정보를 추가해서 자동와이어링을 세밀하게 제어
    
    DataSource 인터페이스를 구현한 2개의 ex)oracle, mysql DataSource가 있는 경우
    @Autowired를 통해서 주입 받으려고 하면 어느 DataSource를 주입해야 할지 몰라서 에러를 발생시킨다
    @Resource로 id값을 명시하면 문제가 되지 않음
    이 경우 XML = <bean>으로 등록 된 2개의 DataSource에 <qualifier value=""> 로 구분값을 주고
    에노테이션 @Component, @Qualifier("") 빈으로 등록할 때 사용하는 @Qualifier는 구분 값으로 빈에 넣어준다
    @Autowired 받는 필드에 @Qualifier("")에 동일한 값을 주어 스프링이 구분할 수 있도록 한다
    @Qualifier를 사용하여 구분값으로 DI하려고 할 때 없으면 구분값과 동일한 빈 ID가 있는지 까지 확인한다 !권장하지 않음

    이름을 이용한 빈 지정 : @Resource, 타입과 구분값을 통한 빈 지정 : @Autowired + @Qualifier
    @Qualifier를 생성자와 일반메소드에 적용할 때는 각 파라미터 마다 부여
    @Autowired를 사용해서 DI주입이 되지 않아도 괜찮다면 속성 값으로 required=false 적용

@Inject(Java EE 6의 표준 스펙), @Qualifier(Java EE 6의 표준 스펙)

    @Inject : required 속성을 사용할 수 없는 @Autowired와 동일한 기능
    @Qualifier : 다른 한정자 어노테이션을 정의하는 용도로 거의 사용하지 않음
    @PostConstruct : WAS실행시 메서드에 초기화를 진행하기 위해 붙여주는 어노테이션
    @PreDestroy : 제거 메소드로 컨테이너가 종료될 때 호출되어 빈이 사용한 리소스를 반환하거나 종료 전 처리해주는 작업
    <context:annotation-config /> : @Autowired, @PostConstruct를 위해 사용
    <context:component-scan base-package="..." /> : @Component 같은 스테레오타입 빈의 등록을 위해 사용, annotation-cofig의 역할도 수행
