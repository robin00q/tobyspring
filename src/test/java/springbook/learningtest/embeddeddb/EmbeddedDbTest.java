package springbook.learningtest.embeddeddb;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmbeddedDbTest {

    EmbeddedDatabase db;
    JdbcTemplate template;

    @BeforeEach
    void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/schema.sql")
                .addScript("classpath:/data.sql")
                .build();

        template = new JdbcTemplate(db);
    }

    @AfterEach
    void tearDown() {
        db.shutdown();
    }

    @Test
    void initData() {
        assertEquals(2, template.queryForObject("select count(*) from sqlmap", Integer.class));

        List<Map<String, Object>> list = template.queryForList("select * from sqlmap order by key_");
        assertEquals("KEY1", list.get(0).get("key_"));
        assertEquals("SQL1", list.get(0).get("sql_"));
        assertEquals("KEY2", list.get(1).get("key_"));
        assertEquals("SQL2", list.get(1).get("sql_"));
    }

    @Test
    void insert() {
        template.update("insert into sqlmap(key_, sql_) values (?,?)", "KEY3", "SQL3");
        assertEquals(3, template.queryForObject("select count(*) from sqlmap", Integer.class));
    }
}
