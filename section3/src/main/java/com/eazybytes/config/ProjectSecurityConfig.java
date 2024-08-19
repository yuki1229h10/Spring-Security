package com.eazybytes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

@Configuration
public class ProjectSecurityConfig {

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		//		http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
		http.authorizeHttpRequests(
				(requests) -> requests.requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards")
						.authenticated().requestMatchers("/notices", "/contact", "/error").permitAll());
		http.formLogin();
		http.httpBasic();
		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user = User.withUsername("user").password("{noop}EazyBytes@12345").authorities("read").build();
		UserDetails admin = User.withUsername("admin")
				.password("{bcrypt}$2a$12$5uzab2z./O/7UjE9YtCWe.eHVw.epHtBiTImB4ySwJMbWyQYlqkXu").authorities("admin")
				.build();
		return new InMemoryUserDetailsManager(user, admin);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public CompromisedPasswordChecker compromisedPasswordChecker() {
		return new HaveIBeenPwnedRestApiPasswordChecker();
	}

}
