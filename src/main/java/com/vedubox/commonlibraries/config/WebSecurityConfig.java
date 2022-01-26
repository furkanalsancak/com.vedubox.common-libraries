package com.vedubox.commonlibraries.config;

// import com.vedubox.commonlibraries.config.properties.AuthNotRequiredURLPath;
// import com.vedubox.commonlibraries.exception.management.AccessDeniedHandler;
// import com.vedubox.commonlibraries.exception.management.AuthenticationFailureHandler;
// import com.vedubox.common.filter.AuthenticationFilter;
// import com.vedubox.common.service.VeduboxUserDetailsService;
import com.vedubox.commonlibraries.exception.management.AccessDeniedHandler;
import com.vedubox.commonlibraries.exception.management.AuthenticationFailureHandler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.http.HttpMethod.OPTIONS;


@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // private final VeduboxUserDetailsService veduboxUserDetailsService;
    // private final AuthenticationFilter authenticationFilter;
    // private final AuthNotRequiredURLPath authNotRequiredURLPath;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Value("${spring.security.user.name}")
    private String springSecurityUserName;
    @Value("${spring.security.user.password}")
    private String springSecurityUserPassword;

    /**
     * AUTHENTICATION
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * 1. In-memory Authentication
         */
        auth.inMemoryAuthentication()
                .withUser(springSecurityUserName)
                .password(passwordEncoder().encode(springSecurityUserPassword))
                .roles("SERVICE");
                // .and().withUser("u").password("u").roles("USER");
    }

    /**
     * AUTHORIZATION
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /**
         * WebSecurityConfigurerAdapter.configure(HttpSecurity http) {
         *      http.authorizeRequests((requests) -> requests.anyRequest().authenticated());
         *      http.formLogin();
         *      http.httpBasic();
         * }
         */

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers(OPTIONS, "/api/**").permitAll()
                .antMatchers("/public/api/**").permitAll();

        http.authorizeRequests()
                .antMatchers("/public/api/**").permitAll()
                .antMatchers("/swagger-ui.html#/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/configuration/ui").permitAll()
                .antMatchers("/webjars/**").permitAll();

        http.authorizeRequests()
                .antMatchers("/h2/**").permitAll()  // permit H2 request
                // The following line breaks POST and PUT methods under /public/api route!!
                // .and().csrf().ignoringAntMatchers("/h2/**");     // Disable the CSRF protection of the H2 console
                .and().headers().frameOptions().sameOrigin();   // Allow H2 console requests from the same source

        http.authorizeRequests()
                .anyRequest().authenticated()   // Enables authentication check
                .and().httpBasic();             // Enables basic authentication

        http.exceptionHandling()
                .authenticationEntryPoint(authenticationFailureHandler)
                .accessDeniedHandler(accessDeniedHandler);

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        // Old ones below...

        // http.csrf().disable()
        //         .authorizeRequests()
        //         .antMatchers(OPTIONS, "/api/**").permitAll()
        //         .antMatchers("/swagger-ui.html#/**").permitAll()
        //         .antMatchers("/admin").hasRole("ADMIN")
        //         .antMatchers("/api/v1/secure").hasRole("ADMIN")
        //         .anyRequest().authenticated();

        // http.csrf().disable()
        //         .authorizeRequests()
        //         .antMatchers("/api/**").permitAll()
        //         .and().formLogin()
        //         .and().httpBasic();

        // http.exceptionHandling()
        //         .authenticationEntryPoint(authenticationFailureHandler)
        //         .accessDeniedHandler(accessDeniedHandler);
        //
        // http.authorizeRequests()
        //         .antMatchers("/h2/**").permitAll() // permit H2 request
        //         .and().csrf().ignoringAntMatchers("/h2/**") // Disable the CSRF protection of the H2 console
        //         .and().headers().frameOptions().sameOrigin(); // Allow H2 console requests from the same source
        //
        // if (authNotRequiredURLPath.getGets() != null) {
        //     http.authorizeRequests().antMatchers(HttpMethod.GET, authNotRequiredURLPath.getGets()).permitAll();
        // }
        // if (authNotRequiredURLPath.getPosts() != null) {
        //     http.authorizeRequests().antMatchers(HttpMethod.POST, authNotRequiredURLPath.getPosts()).permitAll();
        // }
        //
        // http.csrf().disable()
        //         .authorizeRequests()
        //         .antMatchers(OPTIONS, "/api/**").permitAll()
        //         .antMatchers("/swagger-ui.html#/**").permitAll()
        //         .antMatchers("/admin").hasRole("ADMIN")
        //         .antMatchers("/api/v1/secure").hasRole("ADMIN")
        //         .anyRequest().authenticated();
        //
        // http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }


    // @Override
    // @Bean
    // public AuthenticationManager authenticationManagerBean() throws Exception {
    //     return super.authenticationManagerBean();
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
