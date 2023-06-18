package com.money.money.auth.conf.filter;

import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class MoneyLogoutSuccessHandler extends HttpStatusReturningLogoutSuccessHandler {
}
