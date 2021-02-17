package springbook.user;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.user.dao.ConnectionMaker;
import springbook.user.dao.DConnectionMaker;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserServiceImpl;
import springbook.user.mail.DummyMailSender;
import springbook.user.sqlservice.OxmSqlService;
import springbook.user.sqlservice.SqlService;
import springbook.user.sqlservice.sqlregistry.EmbeddedSqlRegistry;
import springbook.user.sqlservice.sqlregistry.SqlRegistry;

import javax.sql.DataSource;

@Configuration
//@ComponentScan(basePackages = "springbook.user") -> TobyApplication.class 의 @SpringbootApplication 을 통해 설정
public class DaoFactory {

    @Autowired
    UserDao userDao;

    @Bean
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
//        hikariDataSource.setDriverClassName("org.h2.Driver");
//        hikariDataSource.setJdbcUrl("jdbc:h2:tcp://localhost/~/tobyspring");
//        hikariDataSource.setUsername("sa");
//        hikariDataSource.setPassword("");
        hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/tobyspring?serverTimezone=UTC&characterEncoding=UTF-8");
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("123123");
        return hikariDataSource;
    }

//    @Bean
//    public UserDao userDao() {
//        UserDaoJdbc userDao = new UserDaoJdbc();
////        userDao.setDataSource(dataSource());
////        userDao.setSqlService(sqlService());
//        return userDao;
//    }

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public UserServiceImpl userService() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        userServiceImpl.setUserDao(userDao);
        userServiceImpl.setMailSender(mailSender());
        return userServiceImpl;
    }

    @Bean
    public SqlService sqlService() {
        OxmSqlService oxmSqlService = new OxmSqlService();
        oxmSqlService.setUnmarshaller(unmarshaller());
        oxmSqlService.setSqlmap(new ClassPathResource("/sqlmap.xml"));
        oxmSqlService.setSqlRegistry(sqlRegistry());
        return oxmSqlService;
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/schema.sql")
                .addScript("classpath:/data.sql")
                .build();
        EmbeddedSqlRegistry embeddedSqlRegistry = new EmbeddedSqlRegistry();
        embeddedSqlRegistry.setDataSource(db);
        return embeddedSqlRegistry;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("springbook.user.sqlservice.jaxb");
        return jaxb2Marshaller;
    }


    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }
}
