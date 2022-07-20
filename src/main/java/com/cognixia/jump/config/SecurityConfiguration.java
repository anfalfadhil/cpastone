package com.cognixia.jump.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cognixia.jump.filter.JwtRequestFilter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	JwtRequestFilter jwtRequestFilter;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Override
	protected void configure( AuthenticationManagerBuilder auth ) throws Exception {
		
		auth.userDetailsService( userDetailsService );
		
	}
	
	@Override
	protected void configure( HttpSecurity http ) throws Exception {

		
		
		http.csrf().disable()
			.authorizeRequests()
//			.antMatchers("/v3/**").permitAll()
//			.antMatchers("/openapi.html").permitAll()
//			.antMatchers("/swagger-ui/**").permitAll()
			.antMatchers(HttpMethod.POST, "/authenticate").permitAll() // anyone can create token if they're a user
			.antMatchers(HttpMethod.POST, "/user/new").permitAll()
//			.antMatchers(HttpMethod.GET, "/api/user").hasRole("ADMIN")
//			.antMatchers("/api/update/game").hasRole("ADMIN")
//			.antMatchers("/api/add/game").hasRole("ADMIN")
//			.antMatchers("/api/delete/game/**").hasRole("ADMIN")
			.anyRequest().authenticated().and()

			// make sure we use stateless session; session won't be used to
			// store user's state.
			
			.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//		
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
	}
	
	@Bean
	protected PasswordEncoder passwordEncoder() {
		
		
		return new BCryptPasswordEncoder();

		
	}
	

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		
		return super.authenticationManagerBean();
	}
	
	
	
}
