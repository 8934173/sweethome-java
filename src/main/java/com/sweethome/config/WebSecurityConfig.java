package com.sweethome.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sweethome.filter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("securityUserDetailServiceImp")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUrlAccessDecisionManager accessDecisionManager;

    @Autowired
    private JwtFilterInvocationSecurityMetadataSource securityMetadataSource;

    @Autowired
    private JwtLoginSuccessHandler jwtLoginSuccessHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtLoginFilter jwtLoginFilter = new JwtLoginFilter();
        jwtLoginFilter.setAuthenticationManager(authenticationManagerBean());
        jwtLoginFilter.setAuthenticationSuccessHandler(jwtLoginSuccessHandler);
        jwtLoginFilter.setAuthenticationFailureHandler(new JwtLoginFailureHandler());
        jwtLoginFilter.setFilterProcessesUrl("/sys/login");

        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(userDetailsService, bCryptPasswordEncoder());

        http.csrf().disable()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/static/**", "/sys/captcha.jpg", "/chat", "/clock/**").permitAll()
                .anyRequest().authenticated()
//                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//                    @Override
//                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
//                        o.setAccessDecisionManager(accessDecisionManager);
//                        o.setSecurityMetadataSource(securityMetadataSource);
//                        return o;
//                    }
//                })
                .and().exceptionHandling().accessDeniedHandler(new RestAuthAccessDeniedHandler())
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .and().logout().logoutSuccessHandler(new LogoutSuccessHandlerImpl())
                .and().authenticationProvider(jwtAuthenticationProvider);

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterAt(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtTokenFilter(), JwtLoginFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/sys/captcha.jpg", "/sys/logon", "/clock/**", "/ws/**");
        //super.configure(web);
    }

    private BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
