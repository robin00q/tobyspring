package springbook.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import springbook.user.sqlservice.OxmSqlService;
import springbook.user.sqlservice.SqlService;
import springbook.user.sqlservice.sqlregistry.EmbeddedSqlRegistry;
import springbook.user.sqlservice.sqlregistry.SqlRegistry;

@Configuration
public class SqlServiceContext {

    @Bean
    public SqlService sqlService() {
        OxmSqlService oxmSqlService = new OxmSqlService();
        oxmSqlService.setUnmarshaller(unmarshaller());
        oxmSqlService.setSqlmap(new ClassPathResource("/sqlmap.xml"));
        oxmSqlService.setSqlRegistry(sqlRegistry());
        return oxmSqlService;
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/schema.sql")
                .addScript("classpath:/data.sql")
                .build();
        EmbeddedSqlRegistry embeddedSqlRegistry = new EmbeddedSqlRegistry();
        embeddedSqlRegistry.setDataSource(db);
        return embeddedSqlRegistry;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("springbook.user.sqlservice.jaxb");
        return jaxb2Marshaller;
    }
}
