package com.rodrigodmd.core.gateway.config;

import com.rodrigodmd.core.gateway.security.AuthoritiesConstants;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.CorsFilter;

import io.github.jhipster.security.AjaxLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableOAuth2Sso
@Configuration
public class OAuth2SsoConfiguration extends WebSecurityConfigurerAdapter {

    private final RequestMatcher authorizationHeaderRequestMatcher;

    private final CorsFilter corsFilter;

    public OAuth2SsoConfiguration(@Qualifier("authorizationHeaderRequestMatcher")
                                  RequestMatcher authorizationHeaderRequestMatcher, CorsFilter corsFilter) {
        this.authorizationHeaderRequestMatcher = authorizationHeaderRequestMatcher;
        this.corsFilter = corsFilter;
    }

    @Bean
    public AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler() {
        return new MyLogoutSuccessHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .addFilterBefore(corsFilter, CsrfFilter.class)
            .headers()
            .frameOptions()
            .disable()
        .and()
            .logout()
            .logoutUrl("/api/logout")
            .logoutSuccessHandler(ajaxLogoutSuccessHandler())
        .and()
            .requestMatcher(new NegatedRequestMatcher(authorizationHeaderRequestMatcher))
            .authorizeRequests()
            .antMatchers("/api/profile-info").permitAll()
            .antMatchers("/api/logout-redirect").permitAll()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .anyRequest().permitAll();
    }

    @SuppressWarnings("unchecked")
    public class MyLogoutSuccessHandler extends AjaxLogoutSuccessHandler {

        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

            authentication.setAuthenticated(false);

            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    cookie.setMaxAge(0);
                    cookie.setValue(null);
                    cookie.setPath("/");
                    cookie.setComment("EXPIRING COOKIE at " + System.currentTimeMillis());
                    response.addCookie(cookie);
                }
            }

            response.setContentType("text/html");
            response.setStatus(200);
        }
    }

    /**
     *
     * @param name
     * @return
     */
    private Cookie createCookie(String name){
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0); // will remove the cookie
        return cookie;
    }
}
