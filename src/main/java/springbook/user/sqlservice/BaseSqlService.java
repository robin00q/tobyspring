package springbook.user.sqlservice;

import lombok.Setter;
import springbook.user.exception.SqlNotFoundException;
import springbook.user.exception.SqlRetrievalFailureException;

import javax.annotation.PostConstruct;

@Setter
public class BaseSqlService implements SqlService {

    protected SqlReader sqlReader;
    protected SqlRegistry sqlRegistry;

    @PostConstruct
    public void loadSql() {
        sqlReader.read(sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }
}
