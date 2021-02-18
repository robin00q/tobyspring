package springbook.user;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.user.dao.ConnectionMaker;
import springbook.user.dao.DConnectionMaker;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserService;
import springbook.user.mail.DummyMailSender;

import javax.sql.DataSource;

//@ComponentScan(basePackages = "springbook.user") -> TobyApplication.class 의 @SpringbootApplication 을 통해 설정
@Configuration
@Import(SqlServiceContext.class)
public class AppContext {

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

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

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }


    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }
}
