package springbook.user.dao;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {

    @Bean
    public JdbcContext jdbcContext() {
        JdbcContext jdbcContext = new JdbcContext();
        jdbcContext.setDataSource(dataSource());
        return jdbcContext;
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName("org.h2.Driver");
        hikariDataSource.setJdbcUrl("jdbc:h2:tcp://localhost/~/tobyspring");
        hikariDataSource.setUsername("sa");
        hikariDataSource.setPassword("");
        return hikariDataSource;
    }

    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao(dataSource());
        userDao.setJdbcContext(jdbcContext());
        return userDao;
    }
}
