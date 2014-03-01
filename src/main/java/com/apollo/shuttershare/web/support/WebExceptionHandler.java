package com.apollo.shuttershare.web.support;

import com.apollo.shuttershare.common.WebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Date: 2/15/14
 */
@Slf4j
@ControllerAdvice
public class WebExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ModelAndView handleAjaxException(RuntimeException e, HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Handling exception. {}", e);

        String stacktrace = getStackTrace(e);

        int httpStatus;
        if (e instanceof WebException) {
            httpStatus = ((WebException) e).getStatusCode();
        } else {
            httpStatus = 500;
        }
        response.setStatus(httpStatus);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", httpStatus);
        map.put("code", httpStatus);
        map.put("message", e.getMessage());
        map.put("developerMessage", stacktrace);

        map.put("moreInfo", "http://www.shuttershareapp.com");

        logger.warn("Runtime Exception is captured. {}", e);

        if (isAjax(request)) {
            return new ModelAndView(new MappingJackson2JsonView(), map);
        } else {
            map.put("exception", e);
            map.put("_contextRoot", request.getContextPath());
            return new ModelAndView("error", map);
        }
    }

    private boolean isAjax(HttpServletRequest request) {
        return true;
        //TODO Return true for now, for convenience
//        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    public static String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        try {
            e.printStackTrace(printWriter);
            return stringWriter.toString();
        } finally {
            printWriter.close();
            try {
                stringWriter.close();
            } catch (IOException e2) {
                log.error("Exception while closing StringWriter {}", e2);
            }
        }
    }
}
