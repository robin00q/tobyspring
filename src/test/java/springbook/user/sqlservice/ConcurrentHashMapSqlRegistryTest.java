package springbook.user.sqlservice;

import springbook.user.sqlservice.sqlregistry.ConcurrentHashMapSqlRegistry;
import springbook.user.sqlservice.sqlregistry.UpdatableSqlRegistry;

class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {
    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        return new ConcurrentHashMapSqlRegistry();
    }
}