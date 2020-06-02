package com.yzl.study.db2rest.annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.yzl.study.db2rest.SimpleRestConfig;
import com.yzl.study.db2rest.component.DbMetaInfo;
import com.yzl.study.db2rest.model.PageResponse;
import com.yzl.study.db2rest.model.ResponseMessage;


/**
 * 正常的执行顺序是：@Around ->@Before->主方法体->@Around中pjp.proceed()->@After->@AfterReturning
 *
 */
@Aspect //AOP 切面
@Component
public class SimpleRestAspect {

    @Autowired
	SimpleRestConfig  simpleRestConfig;
	
    //切入点
    @Pointcut(value = "@annotation(com.yzl.study.db2rest.annotation.SimpleRest)")
    private void simpleRestPointCut() {

    }

    @SuppressWarnings("unused")
    @Around(value = "simpleRestPointCut() && @annotation(simpleRest)")
    public Object  doBefore(ProceedingJoinPoint  point, SimpleRest simpleRest) throws Throwable {
    	 
    	 String tableObjectName = null;
    	 String targetMethod = point.getSignature().getName();
    	 ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    	 HttpServletRequest request = attributes.getRequest();
    	 HttpServletResponse response = attributes.getResponse();
    	 
    	 tableObjectName=getTableObjectName(request);
    	 
    	 if(tableObjectName!=null && DbMetaInfo.checkTableExist(tableObjectName)) {
    		 
    		 if(simpleRestConfig.getPermit()!=null) {
    			 //配置了 -all=true 可以访问 
    			 Boolean permitAll = simpleRestConfig.getPermit().get(tableObjectName+"-all");
    			 if((permitAll==null)?false:permitAll) {
    				 return point.proceed();
    			 }else {
    				 //否则   明确配置了true 才能访问
    				 Boolean permit = simpleRestConfig.getPermit().get(tableObjectName+"-"+targetMethod);
        			 if((permit==null)?false:permit ) {
        				 return point.proceed();
        			 }
    			 }
    			
    		 }
    		 System.out.println("======="+ JSON.toJSONString(simpleRestConfig));
    		 return point.proceed();
    	 }
		 System.out.println("table not exist: "+tableObjectName);
		 response.setStatus(404); // .getWriter().write("table not exist");
		 return null;
    	 

    }
    
 
    @SuppressWarnings("unchecked")
	@AfterReturning(value = "simpleRestPointCut() && @annotation(simpleRest)", returning = "result")
    public Object afterReturning(JoinPoint joinPoint, SimpleRest simpleRest, Object result) {

    	 String tableObjectName = null;
    	 String targetMethod = joinPoint.getSignature().getName();
    	 ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    	 HttpServletRequest request = attributes.getRequest();
    	 tableObjectName=getTableObjectName(request);
         //查询时根据 配置 过滤掉排除的字段
		if (result != null && "getEntity".equals(targetMethod)) {
			ResponseMessage   rspm = (ResponseMessage)result;
			Map<String, Object> resultMap = (Map<String, Object>) rspm.getData();
			if(resultMap!=null && resultMap.size()>0) {
				deleteExcludeFields(tableObjectName,resultMap);
				
			}

		}
    	 if(result!=null && "listEntity".equals(targetMethod)) {
    		 
    		 ResponseMessage rspm = (ResponseMessage) result;
    		 PageResponse  prsp = (PageResponse ) rspm.getData();
    		 List<Map<String, Object>>resultMapList =  prsp.getList();
    		 if(resultMapList!=null && resultMapList.size()>0) {
    			 for(Map<String, Object> m : resultMapList) {
        			 
        			 deleteExcludeFields(tableObjectName, m);
        		 }
    		 }
    		
    	 }
    	 
    	 
        return result;
    }
    
    private void deleteExcludeFields( String tableObjectName ,Map<String, Object> map) {
		Map<String, String[]> exclude = simpleRestConfig.getExclude();
		if (exclude != null && exclude.size() > 0) {

			String[] excludeFields = exclude.get(tableObjectName);
			List<String> excludeFieldsList = Arrays.asList(excludeFields);
			Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> entry = it.next();
				if (excludeFieldsList.contains(entry.getKey()))
					it.remove();
			}
		}
		
	}

	private String getTableObjectName(HttpServletRequest request) {
    	String tableObjectName= null;
    	String uri = request.getRequestURI();
     	String s2=  uri.substring(uri.indexOf("/rest/")+6) ;
     	int last = s2.indexOf("/");
     	if(last==-1) {
     		tableObjectName=s2;
     	}else {
     		tableObjectName=s2.substring(0, last);
     	}
		return tableObjectName;
	}
    
//
//    /**
//     * 方法执行后 并抛出异常
//     *
//     * @param joinPoint
//     * @param myLog
//     * @param ex
//     */
//    @AfterThrowing(value = "pointcut() && @annotation(myLog)", throwing = "ex")
//    public void afterThrowing(JoinPoint joinPoint, SimpleRest simpleRest, Exception ex) {
//        System.out.println("++++执行了afterThrowing方法++++");
//        System.out.println("请求：" + simpleRest.requestUrl() + " 出现异常");
//    }

}
