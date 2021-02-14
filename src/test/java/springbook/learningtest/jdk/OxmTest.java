package springbook.learningtest.jdk;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.oxm.Unmarshaller;
import springbook.user.DaoFactory;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = DaoFactory.class)
class OxmTest {

    @Autowired
    Unmarshaller unmarshaller;

    @Test
    void unmarshallSqlMap() throws IOException {
        Source xmlSource = new StreamSource(getClass().getResourceAsStream("/sqlmap-test.xml"));

        Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(xmlSource);

        List<SqlType> sqlList = sqlmap.getSql();

        assertEquals(3, sqlList.size());
        assertEquals("add", sqlList.get(0).getKey());
        assertEquals("insert", sqlList.get(0).getValue());
        assertEquals("get", sqlList.get(1).getKey());
        assertEquals("select", sqlList.get(1).getValue());
        assertEquals("delete", sqlList.get(2).getKey());
        assertEquals("delete", sqlList.get(2).getValue());
    }
}
