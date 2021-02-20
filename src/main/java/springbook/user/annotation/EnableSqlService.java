package springbook.user.annotation;

import org.springframework.context.annotation.Import;
import springbook.user.SqlServiceContext;

@Import(value = SqlServiceContext.class)
public @interface EnableSqlService {
}
