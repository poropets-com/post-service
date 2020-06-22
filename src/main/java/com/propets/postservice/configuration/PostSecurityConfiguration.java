package com.propets.postservice.configuration;

import com.propets.postservice.security.JwtTokenValidatorFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
//@EnableWebSecurity
public class PostSecurityConfiguration {
    private static final String USER = "USER";
    private static final String MODERATOR = "MODERATOR";

    @Bean
    public FilterRegistrationBean<JwtTokenValidatorFilter> filterRegistrationBean() {
        FilterRegistrationBean < JwtTokenValidatorFilter > registrationBean = new FilterRegistrationBean();
        JwtTokenValidatorFilter filter=new JwtTokenValidatorFilter();

        registrationBean.setFilter(filter);
        return registrationBean;
    }
    

//    @Override
//    protected void configure(HttpSecurity httpSecurity)throws Exception{
//        httpSecurity.httpBasic().disable();
//        httpSecurity.csrf().disable().
//                sessionManagement().
//                sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        httpSecurity.authorizeRequests().
//                antMatchers(HttpMethod.POST,PREFIX+OWNER+"/**").hasRole(USER).
//                antMatchers(HttpMethod.PUT,PREFIX+"/*/*").hasRole(USER).
//                antMatchers(HttpMethod.DELETE,PREFIX+"/*/*").hasRole(USER).
//                antMatchers(HttpMethod.GET, PREFIX+"/*").hasRole(USER).
//                antMatchers(HttpMethod.GET,PREFIX+VIEW+"?**").hasRole(USER).
//                antMatchers(HttpMethod.PUT,PREFIX+COMPLAIN+"/*").hasRole(USER).
//                antMatchers(HttpMethod.PUT,PREFIX+HIDE+"/*").hasRole(MODERATOR);
//    }
}
