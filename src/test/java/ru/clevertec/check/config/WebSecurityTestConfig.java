package ru.clevertec.check.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.clevertec.check.model.user.Role;

import javax.sql.DataSource;

import static ru.clevertec.check.service.CheckConstants.ROLE_ADMIN;
import static ru.clevertec.check.service.CheckConstants.ROLE_USER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityTestConfig extends WebSecurityConfigurerAdapter {
private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
        auth.inMemoryAuthentication()
        .withUser("admin").password(passwordEncoder.encode("123456")).roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/admin/**").permitAll()//.hasRole("ADMIN")
                .antMatchers("/shop/**").permitAll()//.hasRole("USER")
                //.antMatchers("/registration").permitAll()
                .antMatchers("/all").permitAll()
                .and()
                .csrf().disable().formLogin().loginPage("/login");
    }

}
