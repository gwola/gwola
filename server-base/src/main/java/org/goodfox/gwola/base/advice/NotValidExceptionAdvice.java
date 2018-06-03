package org.goodfox.gwola.base.advice;

import org.goodfox.gwola.util.http.ResponseMessage;
import org.goodfox.gwola.util.http.ResponseMessageCodeEnum;
import org.goodfox.gwola.util.http.Result;
import org.goodfox.gwola.util.http.ValidError;
import org.goodfox.gwola.util.utils.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 参考：http://javaninja.net/2013/12/getting-spring-mvc-validation-messages-from-a-json-service/
 */
@ControllerAdvice
public class NotValidExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(NotValidExceptionAdvice.class);

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseMessage<ValidError> handlerConstraintViolationException(ConstraintViolationException exception) {
        ConstraintViolation violation = Exceptions.getFirstError(exception);
        if (violation != null) {
            logger.warn("Valid Exception：{}", violation.getMessage());
        } else {
            logger.warn(exception.getMessage(), exception);
        }
        ValidError validError = new ValidError(violation.getPropertyPath().toString(), violation.getMessage());
        return Result.error(ResponseMessageCodeEnum.ERROR.getCode(), validError.getMessage(), validError);
    }

}



