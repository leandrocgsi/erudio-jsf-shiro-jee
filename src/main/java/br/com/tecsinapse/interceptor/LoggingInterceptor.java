package br.com.tecsinapse.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Interceptor
@Logging
public class LoggingInterceptor implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @AroundInvoke
    public Object log(InvocationContext context) throws Exception {
        logger.info(string(context));
        try {
            return context.proceed();
        } catch (Exception e) {
            logger.error("Erro", e);
            throw e;
        }
    }

    private String string(InvocationContext context) {
        final String clazz = context.getMethod().getDeclaringClass().getSimpleName();
        final String method = context.getMethod().getName();
        final Object[] parameters = context.getParameters();
        StringBuilder sb = new StringBuilder();
        sb.append(clazz);
        sb.append(".");
        sb.append(method);
        boolean first = true;
        if (parameters != null && parameters.length > 0) {
            sb.append(": params=[");
            for (Object o : parameters) {
                if (o != null) {
                    if (!first) {
                        sb.append("), ");
                    }
                    first = false;
                    sb.append("(");
                    sb.append(o.toString());
                }
            }
            if (!first) {
                sb.append(")");
            }
            sb.append("]");
        }
        return sb.toString();
    }
}