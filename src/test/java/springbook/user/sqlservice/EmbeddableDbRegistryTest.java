package springbook.user.sqlservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import springbook.user.exception.SqlUpdateFailureException;
import springbook.user.sqlservice.sqlregistry.EmbeddedSqlRegistry;
import springbook.user.sqlservice.sqlregistry.UpdatableSqlRegistry;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmbeddableDbRegistryTest extends AbstractUpdatableSqlRegistryTest {
    EmbeddedDatabase db;
    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/schema.sql")
                .build();

        EmbeddedSqlRegistry embeddedSqlRegistry = new EmbeddedSqlRegistry();
        embeddedSqlRegistry.setDataSource(db);

        return embeddedSqlRegistry;
    }

    @AfterEach
    void tearDown() {
        db.shutdown();
    }

    @Test
    void transactionUpdate() {
        checkFindResult("SQL1", "SQL2", "SQL3");

        Map<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "Modified");
        sqlmap.put("Key9999!@#", "Modified9999");

        assertThrows(SqlUpdateFailureException.class, () -> sqlRegistry.updateSql(sqlmap));
        checkFindResult("SQL1", "SQL2", "SQL3");
    }
}
