package br.com.tecsinapse.jsf;

import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggableExceptionHandlerFactory extends ExceptionHandlerFactory {
    private final ExceptionHandlerFactory parent;

    public LoggableExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        ExceptionHandler result = new LoggableJsfExceptionHandler(parent
                .getExceptionHandler());
        return result;
    }

    public static final class LoggableJsfExceptionHandler extends ExceptionHandlerWrapper {
        private static final Logger LOG = LoggerFactory.getLogger(
                LoggableJsfExceptionHandler.class);
        private final ExceptionHandler wrapped;

        private LoggableJsfExceptionHandler(ExceptionHandler wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public ExceptionHandler getWrapped() {
            return wrapped;
        }

        @Override
        public void handle() throws FacesException {
            for (ExceptionQueuedEvent exceptionQueuedEvent
                    : getUnhandledExceptionQueuedEvents()) {
                ExceptionQueuedEventContext context = exceptionQueuedEvent
                        .getContext();
                Throwable exception = context.getException();
                if (exception instanceof ViewExpiredException) {
                    //ignora ViewExpired
                    continue;
                }

                StringBuffer requestURL = ((HttpServletRequest) context.getContext()
                        .getExternalContext().getRequest()).getRequestURL();
                LOG.error("Error processing request ({})", requestURL, exception);
            }
            getWrapped().handle();
        }
    }
}
