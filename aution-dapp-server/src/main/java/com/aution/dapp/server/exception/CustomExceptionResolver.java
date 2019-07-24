package com.aution.dapp.server.exception;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aution.dapp.server.core.ApiConstants;
import com.aution.dapp.server.core.ApiException;
import com.aution.dapp.server.utils.ResultUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局异常处理器.
 *
 * @author CatalpaFlat
 */

@Configuration
public class CustomExceptionResolver implements HandlerExceptionResolver {
  private static Logger LOGGER = LoggerFactory.getLogger(CustomExceptionResolver.class);

  //系统抛出的异常
  @Override
  public ModelAndView resolveException(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      Exception ex) {
    // log the error.
    LOGGER.error(ex.getMessage(),ex);

    //handler就是处理器适配器要执行的Handler对象(只有method)
    //解析出异常类型。
    /* 使用response返回 */
    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); //设置状态码
    response.setContentType(ApiConstants.APPLICATION_JSON); //设置ContentType
    response.setCharacterEncoding(ApiConstants.UTF_8); // 避免乱码
    response.setHeader("Cache-Control", "no-cache, must-revalidate");
    //如果该 异常类型是系统 自定义的异常，直接取出异常信息。
    PrintWriter writer = null;
    try {
       writer = response.getWriter();
      if (ex instanceof CustomException) {
    	CustomException customException = (CustomException) ex;
        //错误信息
        writer.write(ResultUtils.error(customException.getCode(), ex.getMessage()).toString());
      } else if(ex instanceof ApiException ) {
    	  ApiException apiException = (ApiException)ex;
    	  //错误信息
          writer.write(ResultUtils.error(apiException.getStatusCode(), ex.getMessage()).toString());
      }else {
        writer.write(ResultUtils.error(-1, ex.getMessage()).toString());
      }
    } catch (IOException e) {
      LOGGER.error(e.getMessage(), e);
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
    ModelAndView modelAndView = new ModelAndView();
    return modelAndView;
  }
}
