package com.courseselection.courseservice.aspect;

import com.courseselection.courseservice.annotation.RateLimiter;
import com.courseselection.courseservice.controller.CourseController;
import com.courseselection.courseservice.dto.RateLimitExceededResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Aspect
@Component
public class RateLimiterAspect {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    private static final Logger logger = Logger.getLogger(RateLimiterAspect.class.getName());

    @Around("@annotation(rateLimiter)")
    public Object rateLimiter(ProceedingJoinPoint joinPoint, RateLimiter rateLimiter) throws Throwable {
        String key = getClientIP();

        if(rateLimited(key, rateLimiter.limit(), rateLimiter.duration())) {
            RateLimitExceededResponse response = RateLimitExceededResponse
                    .builder()
                    .message("Rate limit exceeded. Please try again later.")
                    .status(HttpStatus.TOO_MANY_REQUESTS.value())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.TOO_MANY_REQUESTS);
        }

        return joinPoint.proceed();
    }

    private synchronized boolean rateLimited(String key, Integer limit, Integer duration) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        long epoch = Instant.now().toEpochMilli();

        Set<ZSetOperations.TypedTuple<Object>> elements = zSetOperations.rangeWithScores(key, 0, -1);
        if(elements != null) {
            Set<Object> elementsToRemove = elements.stream()
                    .filter(tuple -> tuple.getScore() < epoch - (duration * 60 * 1000))
                    .map(ZSetOperations.TypedTuple::getValue)
                    .collect(Collectors.toSet());
            if (!elementsToRemove.isEmpty()) {
                zSetOperations.remove(key, elementsToRemove.toArray());
            }
        }
        if(zSetOperations.size(key) > limit) {
            return true;
        }
        logger.info("Number of requests within " + duration + " minutes: " + zSetOperations.size(key));
        zSetOperations.add(key, UUID.randomUUID().toString(), epoch);
        return false;
    }

    private String getClientIP() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader != null) {
            return xForwardedForHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
