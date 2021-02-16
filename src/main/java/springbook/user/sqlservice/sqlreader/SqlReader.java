package springbook.user.sqlservice.sqlreader;

import springbook.user.sqlservice.sqlregistry.SqlRegistry;

public interface SqlReader {
    void read(SqlRegistry sqlRegistry);
}
