package com.beanbeanjuice.cafeapi.controller;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(authorities = {"ROLE_USER", "ROLE_ADMIN"})
public @interface WithAdminUser {
}
