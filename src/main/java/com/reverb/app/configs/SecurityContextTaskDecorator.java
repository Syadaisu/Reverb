package com.reverb.app.configs;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.core.task.TaskDecorator;

// SecurityContextTaskDecorator.java
public class SecurityContextTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        // Capture the context from the parent thread
        final SecurityContext context = SecurityContextHolder.getContext();

        return () -> {
            try {
                // Set the context for the child thread
                SecurityContextHolder.setContext(context);
                runnable.run();
            } finally {
                // Clear context after execution
                SecurityContextHolder.clearContext();
            }
        };
    }
}

