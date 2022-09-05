    package site.operations.Config;

import site.operations.Security.BasicAuthDetails;
import site.operations.Services.ExtraFunctions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private BasicAuthDetails basicAuthDetails;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
        and().authorizeRequests().
        antMatchers(HttpMethod.GET,"/admin/emp/{id}").hasAnyAuthority("ADMIN","ORG").
        antMatchers(HttpMethod.GET,"/admin/emp/{id}/{property}").hasAnyAuthority("ADMIN","ORG").
        antMatchers(HttpMethod.POST,"/admin/emp/add").hasAnyAuthority("ADMIN","ORG").
        antMatchers(HttpMethod.POST,"/admin/ast/add").hasAnyAuthority("ADMIN","ORG").
        antMatchers(HttpMethod.PUT,"/admin/emp/update/{id}").hasAnyAuthority("ADMIN","ORG").
        antMatchers(HttpMethod.PUT,"/admin/ast/update/{id}").hasAnyAuthority("ADMIN","ORG").
        
        antMatchers(HttpMethod.GET,"/basic/assets/{id}").hasAnyAuthority("ORG","ADMIN","EMPLOYEE").
        antMatchers(HttpMethod.GET,"/basic/org").hasAnyAuthority("ORG","ADMIN","EMPLOYEE").
        antMatchers(HttpMethod.GET,"/basic/login").hasAnyAuthority("ORG","ADMIN","EMPLOYEE").
        antMatchers(HttpMethod.PUT,"/basic/update").hasAnyAuthority("ORG","ADMIN","EMPLOYEE").
        antMatchers(HttpMethod.GET,"/basic/employee/profile").hasAnyAuthority("EMPLOYEE","ADMIN").
        
        antMatchers(HttpMethod.POST,"/org/add").permitAll().
        antMatchers(HttpMethod.DELETE,"/org/emp/delete/{id}").hasAuthority("ORG").
        antMatchers(HttpMethod.DELETE,"/org/ast/delete/{id}").hasAuthority("ORG").
        antMatchers(HttpMethod.DELETE,"/org/delete").hasAuthority("ORG").
        antMatchers(HttpMethod.PUT,"/org/update").hasAuthority("ORG").
        and().httpBasic().and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(this.basicAuthDetails).passwordEncoder(passwordEncoder());
    }
    
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ExtraFunctions getExtraFunctions()
    {
        ExtraFunctions extraFunctions = new ExtraFunctions();
        return extraFunctions ;
    }
}