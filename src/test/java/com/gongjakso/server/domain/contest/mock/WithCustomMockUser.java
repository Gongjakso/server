package com.gongjakso.server.domain.contest.mock;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface WithCustomMockUser {

    String email() default "gongjakso@gmail.com";
}