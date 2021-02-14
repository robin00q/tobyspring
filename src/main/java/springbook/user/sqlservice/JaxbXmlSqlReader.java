package springbook.user.sqlservice;

import lombok.Setter;
import springbook.user.dao.UserDao;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

@Setter
public class JaxbXmlSqlReader implements SqlReader {

    private String sqlmapFile;

    @Override
    public void read(SqlRegistry sqlRegistry) {
        String contextPath = Sqlmap.class.getPackage().getName();

        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream is = UserDao.class.getResourceAsStream(sqlmapFile);
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);

            for(SqlType sqlType : sqlmap.getSql()) {
                sqlRegistry.registerSql(sqlType.getKey(), sqlType.getValue());
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
