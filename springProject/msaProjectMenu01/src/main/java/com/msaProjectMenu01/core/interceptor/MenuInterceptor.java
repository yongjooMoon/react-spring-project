package com.msaProjectMenu01.core.interceptor;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.msaProjectMenu01.core.util.Util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MenuInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(MenuInterceptor.class);
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	// 컨트롤러가 호출되기 전에 실행
//    	if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//            response.setStatus(HttpServletResponse.SC_OK);
//            return false; // OPTIONS 요청에 대해 처리를 중단합니다.
//        }
//
//    	// 세션 체크
//        Map<String, Object> sessionMap = Util.getSessionData(request);
//
//        if (sessionMap == null) {
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            String jsonResponse = "{\"error\": \"세션 정보가 없습니다.\", \"message\": \"세션이 만료되었거나 존재하지 않습니다.\"}";
//            response.getWriter().write(jsonResponse);
//            return false;
//        }
//        
//        Map<String, String[]> map = request.getParameterMap();
//        // 파라미터 맵을 순회하면서 로그 출력
//        for (Map.Entry<String, String[]> entry : map.entrySet()) {
//            String key = entry.getKey();
//            String[] values = entry.getValue();
//            
//            // 값 배열을 문자열로 변환
//            String valuesString = String.join(", ", values);
//
//            // 로그 출력
//            logger.info("Parameter Name: {}, Values: {}", key, valuesString);
//        }
    	
        return true; // true 반환 시 계속 진행, false 반환 시 요청 중단
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 컨트롤러 로직이 실행된 후, 뷰가 렌더링되기 전에 실행
        //System.out.println("Post Handle method is Calling");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 뷰가 렌더링된 후 실행
        //System.out.println("Request and Response is completed");
    }
}
