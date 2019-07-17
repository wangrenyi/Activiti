package com.activiti.mybatis;

import java.util.Map;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * @refer https://www.cnblogs.com/claindoc/p/9835416.html
 */

@Intercepts({@Signature(type = Executor.class, method = "query",
    args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PagingInterceptor extends AbstractInterceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement ms = (MappedStatement)invocation.getArgs()[0];
        if (ms.getSqlCommandType() != SqlCommandType.SELECT) {
            return invocation.proceed();
        }

        Object parameter = invocation.getArgs()[1];
        if (parameter == null || !(parameter instanceof Map)) {
            return invocation.proceed();
        }

        Map<?, ?> parMap = (Map<?, ?>)parameter;
        Integer pageIndex = safeGet(parMap, "pageIndex");
        Integer pageSize = safeGet(parMap, "pageSize");
        Object example = safeGet(parMap, "example");

        if (pageIndex != null && pageSize != null) {
            BoundSql boundSql = ms.getBoundSql(example == null ? parameter : example);
            String originalSql = boundSql.getSql().trim();

            StringBuffer sb = new StringBuffer();
            int offset = pageIndex * pageSize;

            String newSql
                = sb.append(originalSql).append(" limit ").append(offset).append(",").append(pageSize).toString();

            BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, newSql);
            MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
            invocation.getArgs()[0] = newMs;
        }

        if (example != null) {
            invocation.getArgs()[1] = example;
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

}
