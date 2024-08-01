package com.ContactSphere.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class MyConfig {
	
	@Bean
	public UserDetailsService getUserDetailService()
	{
		return new UserDetailServiceImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	 @Bean
	   public DaoAuthenticationProvider authenticationProvider()
	   {
		 DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
		 daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
		 daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		 return daoAuthenticationProvider;
	   }
	 @Bean
	 public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		    http.csrf(csrf -> csrf.disable())
//		        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
//		        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		        .authorizeHttpRequests(auth -> 
		          auth.requestMatchers("/admin/**").hasAuthority("ADMIN")
		          .requestMatchers("/user/**").hasAuthority("ROLE_USER")
		              .requestMatchers("/**").permitAll()
		            //  .anyRequest().authenticated()
		        )
		    .formLogin(formLogin -> formLogin
                    .loginPage("/signin")
                    .loginProcessingUrl("/dologin")
                    .defaultSuccessUrl("/user/index")
                    .permitAll()
            );
		    
		    http.authenticationProvider(authenticationProvider());

		   // http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		    
		    return http.build();
	 
	}
	



   

}