//package edu.xuecj.wiki.config;
//
//import edu.xuecj.wiki.interceptor.LogInterceptor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import javax.annotation.Resource;
//
///**
// * @author xuecj
// * @version 1.0
// * @date 2022/4/10 18:05
// */
//@Configuration
//public class SpringMvcConfig implements WebMvcConfigurer {
//    @Resource
//    LogInterceptor logInterceptor;
//    @Override
//    public void addInterceptors(InterceptorRegistry registry){
//        registry.addInterceptor(logInterceptor)
//                .addPathPatterns("/**").excludePathPatterns("/login");
//    }
//}
