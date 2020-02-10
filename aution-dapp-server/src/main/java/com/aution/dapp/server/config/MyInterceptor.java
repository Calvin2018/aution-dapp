package com.aution.dapp.server.config;

import com.aution.dapp.server.core.ApiConstants;
import com.aution.dapp.server.repository.ConfigRepository;
import com.aution.dapp.server.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hewensheng
 */
public class MyInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyInterceptor.class);

    @Autowired
    private ConfigService configService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        String methodName = method.getName();
        LOGGER.info("====拦截到了方法：{}，在该方法执行之前执行====", methodName);
        Map<String,String> data = configService.getServiceStatus();
        if(null != data){
            String serviceStatus = data.get("service_status");
            String flag = "0";
            if(flag.equals(serviceStatus)){
                String bid = "bid";
                String create = "create";
                if(methodName.endsWith(bid)||methodName.endsWith(create)){
                    returnJson(response);
                    return false;
                }
            }
        }

        // 返回 true 才会继续执行，返回 false 则取消当前请求
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        LOGGER.info("执行完方法之后进执行(Controller方法调用之后)，但是此时还没进行视图渲染");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LOGGER.info("整个请求都处理完咯，DispatcherServlet也渲染了对应的视图咯，此时我可以做一些清理的工作了");
    }

    private void returnJson(HttpServletResponse response){
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            Map<String, Object> result = new HashMap<>();
            result.put("code",ApiConstants.CODE_REQUEST_EROR );
            result.put("msg","服务异常，暂时关闭商品竞拍和发布！");
            result.put("data", null);
            writer.print(result);
        } catch (IOException e){
            LOGGER.error("拦截器IOException");
        } finally {
            if(writer != null){
                writer.close();
            }
        }
    }


}
