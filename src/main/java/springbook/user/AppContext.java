package springbook.user;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.user.dao.ConnectionMaker;
import springbook.user.dao.DConnectionMaker;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserService;
import springbook.user.sqlservice.SqlMapConfig;

import javax.sql.DataSource;

//@ComponentScan(basePackages = "springbook.user") -> TobyApplication.class 의 @SpringbootApplication 을 통해 설정
@Configuration
@Import({SqlServiceContext.class, ProductionAppContext.class})
//@PropertySource("classpath:database.properties")
public class AppContext implements SqlMapConfig {

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
//        hikariDataSource.setDriverClassName(databaseProperty.getDriverClass());
//        hikariDataSource.setJdbcUrl(env.getProperty("db.url"));
//        hikariDataSource.setUsername(env.getProperty("db.username"));
//        hikariDataSource.setPassword(env.getProperty("db.password"));
        hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/tobyspring?serverTimezone=UTC&characterEncoding=UTF-8");
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("123123");
        return hikariDataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }


    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }

    @Override
    public Resource getSqlMapResource() {
        return new ClassPathResource("/sqlmap.xml", UserDao.class);
    }
}
