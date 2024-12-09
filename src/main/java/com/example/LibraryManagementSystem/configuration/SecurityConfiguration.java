/*
package com.example.LibraryManagementSystem.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {



    @Bean
   public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
       httpSecurity
               .authorizeHttpRequests(authorize -> authorize
               .requestMatchers("/public/**", "/login").permitAll()
               .requestMatchers("/admin/**").hasRole("ADMINISTRATOR")
               .anyRequest().authenticated()
               )
                .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
                )
               .logout(logout -> logout
                       .logoutSuccessUrl("/login?logout")
                       .permitAll()
               );

       return httpSecurity.build();
   }

   @Bean
   public PasswordEncoder passwordEncoder(){
      return  new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception{return authenticationConfiguration.getAuthenticationManager();
    }


}
*/
