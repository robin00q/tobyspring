package springbook.user.dao.proxy.factory;

import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.user.dao.proxy.handler.TransactionHandler;

import java.lang.reflect.Proxy;

@Setter
public class TxProxyFactoryBean implements FactoryBean<Object> {

    Object target; // 실제 기능 수행하는 impl
    PlatformTransactionManager transactionManager;
    String pattern;
    Class<?> serviceInterface; // Bean 등록 타입

    @Override
    public Object getObject() {
        TransactionHandler transactionHandler = new TransactionHandler();
        transactionHandler.setTransactionManager(transactionManager);
        transactionHandler.setPattern(pattern);
        transactionHandler.setTarget(target);

        return Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{serviceInterface},
                transactionHandler
        );
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
