package springbook.user.sqlservice;

import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
public class SimpleSqlService implements SqlService {
    private Map<String, String> sqlMap = new HashMap<>();

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        String sql = sqlMap.get(key);
        if(StringUtils.isEmpty(sql)) {
            throw new SqlRetrievalFailureException(key + "에 대한 SQL을 찾을 수 없습니다.");
        }
        return sql;
    }
}
