package com.me.mall.exception;

import com.me.mall.common.ApiRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：处理统一异常的handler
 * <p>
 * 我们需要记住，我们经常使用@ControllerAdvice配合@ExceptionHandler进行统一异常处理
 * <p>
 * 如果不加@ControllerAdvice
 * 那么直接返回前端的json如下
 * {
 * "timestamp": "2021-02-18T12:10:05.405+0000",
 * "status": 500,
 * "error": "Internal Server Error",
 * "message": "不允许重名，注册失败",
 * "path": "/register"
 * }
 * 前端知道错误的路径、信息、错误码，这也不安全，只想让前端知道自定义错误码和错误消息即可，
 * 那么我们需要统一的json格式，用于返回这些异常信息
 * 加上@ControllerAdvice之后，前端得到的json如下
 * {
 * "status": 10004,
 * "msg": "不允许重名，注册失败",
 * "data": null
 * }
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 这里如果异常是Exception异常，一般是系统异常
     *
     * @param e 异常类型
     * @return 通用对象
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception e) {
        log.error("Default Exception: ", e);
        // 返回一个通用对象，里面是status、msg、data
        return ApiRestResponse.error(MyMallExceptionEnum.SYSTEM_ERROR);
    }

    /**
     * 如果异常是MyMallException类型的异常，那么就是用户的异常，比如
     * 没输入用户名、密码，或者密码长度不对、用户名已存在、数据库插入失败等
     *
     * @param e 异常类型
     * @return 通用对象
     */
    @ExceptionHandler(MyMallException.class)
    @ResponseBody
    public Object handleMyMallException(MyMallException e) {
        log.error("MyMallException: ", e);
        // 返回一个通用对象，里面是status、msg、data
        return ApiRestResponse.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiRestResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: ", e);
        return handleBindingResult(e.getBindingResult());

    }

    private ApiRestResponse handleBindingResult(BindingResult result) {
        // 把异常处理为对外暴露的提示
        List<String> list = new ArrayList<>();
        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError objectError : allErrors) {
                /**
                 * 前端请求体如下
                 * {
                 *     "name":"鸭货",
                 *     "type":4,
                 *     "orderNum":10
                 * }
                 * 根据调试，allErrors的size = 2，
                 * 第一个：ObjectError{objectName："addCategoryReq",field:"type",defaultMessage:"type最大只能为3"}
                 * 第二个：ObjectError{objectName："addCategoryReq",field:"parentId",defaultMessage:"parentId不能为null"}
                 */
                String message = objectError.getDefaultMessage();
                list.add(message);
            }
        }
        if (list.size() == 0) {
            return ApiRestResponse.error(MyMallExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return ApiRestResponse.error(MyMallExceptionEnum.REQUEST_PARAM_ERROR.getCode(), list.toString());
    }
}
