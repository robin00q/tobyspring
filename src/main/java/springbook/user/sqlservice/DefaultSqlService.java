package springbook.user.sqlservice;

import springbook.user.sqlservice.sqlreader.JaxbXmlSqlReader;
import springbook.user.sqlservice.sqlregistry.HashMapSqlRegistry;

public class DefaultSqlService extends BaseSqlService {
    public DefaultSqlService() {
        setSqlReader(new JaxbXmlSqlReader());
        setSqlRegistry(new HashMapSqlRegistry());
    }
}
