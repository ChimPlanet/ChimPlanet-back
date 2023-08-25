package com.wak.chimplanet.common.config.aspects;

import com.wak.chimplanet.exception.UnauthorizedException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class IpBlockAspect {

    private final String adminUrlPattern = "/admin/**";
    // 허용할 IP 주소목록
    private static final List<String> ALLOWED_IPS = Arrays.asList("127.0.0.1");

    @Pointcut("within(com.wak.chimplanet..*) && execution(public * com.wak.chimplanet..*Controller.*(..))")
    public void controllerMethod() {}

    @Before("controllerMethod()")
    public void verifyIp(JoinPoint joinPoint) throws UnauthorizedException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        // 요청한 IP 주소가 허용되는 IP 주소 리스트에 속해있는지 검사
        if (!ALLOWED_IPS.contains(ipAddress) && request.getRequestURI().startsWith("/admin")) {
            throw new UnauthorizedException(
                "Access Denied: Your IP address is not allowed to access this resource");
        }
    }
}
