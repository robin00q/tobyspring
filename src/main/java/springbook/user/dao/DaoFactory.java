package springbook.user.dao;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.user.mail.DummyMailSender;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {

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
    public UserDao userDao() {
        UserDaoJdbc userDao = new UserDaoJdbc();
        userDao.setDataSource(dataSource());
        return userDao;
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
    public UserServiceImpl userService() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        userServiceImpl.setUserDao(userDao());
        userServiceImpl.setMailSender(mailSender());
        return userServiceImpl;
    }

//    @Bean
//    public TransactionInterceptor transactionAdvice() {
//        DefaultTransactionAttribute get = new DefaultTransactionAttribute();
//        get.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        get.setReadOnly(true);
//
//        DefaultTransactionAttribute all = new DefaultTransactionAttribute();
//        all.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//
//        Map<String, TransactionAttribute> txMap = new HashMap<>(20);
//        txMap.put("get*", get);
//        txMap.put("*", all);
//        NameMatchTransactionAttributeSource nmt = new NameMatchTransactionAttributeSource();
//        nmt.setNameMap(txMap);
//
//        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
//        transactionInterceptor.setTransactionManager(transactionManager());
//        transactionInterceptor.setTransactionAttributeSource(nmt);
//        return transactionInterceptor;
//    }
//
//    @Bean
//    public AspectJExpressionPointcut transactionPointCut() {
//        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
//        pointcut.setExpression("bean(*Service)");
//        return pointcut;
//    }
//
//    @Bean
//    public DefaultPointcutAdvisor transactionAdvisor() {
//        return new DefaultPointcutAdvisor(transactionPointCut(), transactionAdvice());
//    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }
}
