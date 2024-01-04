package com.manjot.snapnote.aspect;

import com.manjot.snapnote.annotation.RateLimited;
import com.manjot.snapnote.exception.RateLimitException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
@Order(1)
public class RateLimitingAspect {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Around("@annotation(rateLimited)")
    public Object checkRateLimit(ProceedingJoinPoint joinPoint, RateLimited rateLimited) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Bucket bucket = getBucket(methodName);

        if (bucket.tryConsume(1)) {
            return joinPoint.proceed();
        } else {
            throw new RateLimitException("Too many requests. Please try again later.");
        }
    }

    private Bucket getBucket(String methodName) {
        return buckets.computeIfAbsent(methodName, key ->
                Bucket.builder().addLimit(getLimit()).build()
        );
    }

    private Bandwidth getLimit() {
        // Define your bandwidth limit here (e.g., 10 requests per minute)
        return Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1)));
    }
}

