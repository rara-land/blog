package rara.board.config;

import rara.board.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${custom.path.file-real-upload-path}")
    private String fileUploadPath;

    @Value("${custom.path.file-path}")
    private String filePath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/member/login", "/member/register/**", "/member/logout", "/member/kakao", "/board",
                        "/css/**","/*.ico", "/error", "/upload/**", "/icon/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler(filePath+"**")
                    .addResourceLocations("file:///" + fileUploadPath);
//                    .setCachePeriod(3600)
//                    .resourceChain(true)
//                    .addResolver(new PathResourceResolver());

    }
}
