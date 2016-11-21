package com.vieztech;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vieztech.security.CustomSuccessHandler;
import com.vieztech.security.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
// @ComponentScan(basePackageClasses = CustomUserDetailsService.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private LogoutSuccessHandler logoutHandler;

	@Autowired
	private CustomSuccessHandler successHandler;

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		//auth.userDetailsService(userDetailsService).passwordEncoder(passwordencoder());
	}

	// @Autowired
	// public void configureGlobal(AuthenticationManagerBuilder auth) throws
	// Exception {
	// auth.inMemoryAuthentication().withUser("user").password("password").roles("USER").and().withUser("admin")
	// .password("password").roles("ADMIN", "USER");
	// }

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**"); // #3
		web.ignoring().antMatchers("/js/**");
		web.ignoring().antMatchers("/css/**");
		web.ignoring().antMatchers("/img/**");
		web.ignoring().antMatchers("/fonts/**");
		// web.ignoring().antMatchers("/console/**");
	}

	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
	// http.authorizeRequests().antMatchers("/host**").access("hasRole('ROLE_ADMIN')").anyRequest().permitAll().and()
	// .formLogin().loginPage("/login").usernameParameter("email").passwordParameter("password").and().logout()
	// .logoutSuccessUrl("/login?logout").and().exceptionHandling().accessDeniedPage("/403").and().csrf();
	// }
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO: add superadmin here
		// @formatter:off

		// Public URLs
		http.authorizeRequests()
				.antMatchers("/", "/u/signup", "/u/user-signup", "/u/user-login", "/u/password-reset", "/events/**",
						"/sms", "/sms/**", "/u/login", "/u/new-password*", "/u/checkout/**", "/u/create-password*",
						"/u/superadmin/login")
				.permitAll();

		// ADMIN & Super Admin Access
		http.authorizeRequests().antMatchers("/host/**").access("hasAnyRole('ADMIN','SUPERADMIN')")
				.antMatchers("/u/superadmin/**").hasRole("SUPERADMIN").anyRequest().authenticated().and().formLogin()
				.loginPage("/u/login").successHandler(successHandler).permitAll().and().logout().logoutUrl("/u/logout")
				.logoutSuccessHandler(logoutHandler).permitAll();

		// Un-comment below two line if you want to login into H2 Console
		http.csrf().disable();
		http.headers().frameOptions().disable();
		// @formatter:on
	}

	@Bean(name = "passwordEncoder")
	public PasswordEncoder passwordencoder() {
		return new BCryptPasswordEncoder();
	}
}
