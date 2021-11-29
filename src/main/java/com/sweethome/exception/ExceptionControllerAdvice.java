package com.sweethome.exception;

import com.sweethome.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "com.sweethome.controller")
public class ExceptionControllerAdvice {

    /**
     * 字段校验异常处理
     * @param e 方法参数
     * @return R
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        Map<String, String> error = new HashMap<>();
        result.getFieldErrors().forEach( fieldError -> {
            error.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        log.error(e.getMessage());
        return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(), BizCodeEnum.VALID_EXCEPTION.getMsg()).put("data", error);
    }

    /**
     * 普通异常处理
     * @param exception exception
     * @return R
     */
    @ExceptionHandler(value = RuntimeException.class)
    public R handleException(RuntimeException exception) {
        exception.printStackTrace();
        return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), BizCodeEnum.UNKNOWN_EXCEPTION.getMsg());
    }
}
