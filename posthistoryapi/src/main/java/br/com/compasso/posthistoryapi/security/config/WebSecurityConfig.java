//package br.com.compasso.posthistoryapi.security.config;
//
//import br.com.compasso.posthistoryapi.security.filter.CsrfCookieFilter;
//import br.com.compasso.posthistoryapi.security.filter.JwtAuthFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.logout.LogoutHandler;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
//import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
//
//import java.util.Arrays;
//import java.util.Collections;
//
//import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
//@RequiredArgsConstructor
//public class WebSecurityConfig {
//
//    private final JwtAuthFilter jwtAuthFilter;
//    private final AuthenticationProvider authenticationProvider;
//    private final LogoutHandler logoutHandler;
//    @Bean
//    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
//        return new MvcRequestMatcher.Builder(introspector);
//    }
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
//
//      CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
//      requestHandler.setCsrfRequestAttributeName("_csrf");
//        http
//                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
//                            @Override
//                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//                                CorsConfiguration config = new CorsConfiguration();
//                                config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
//                                config.setAllowedMethods(Collections.singletonList("*"));
//                                config.setAllowCredentials(true);
//                                config.setAllowedHeaders(Collections.singletonList("*"));
//                                config.setExposedHeaders(Arrays.asList("Authorization"));
//                                config.setMaxAge(3600L);
//                                return config;
//                            }
//                        }))
//          .csrf(csrf -> csrf.csrfTokenRequestHandler(requestHandler).ignoringRequestMatchers(
//                  antMatcher("/users/**"), antMatcher("/auth/**"), antMatcher("/h2/**"))
//            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
//            .authorizeHttpRequests(requests ->
//                requests
//                    .requestMatchers(mvc.pattern("/h2/**")).permitAll()
//                        .requestMatchers(mvc.pattern("/auth/**")).permitAll()
//                        .requestMatchers(mvc.pattern("/users/**")).permitAll()
//                        .anyRequest().authenticated()
//            )
//            .headers().frameOptions().disable()
//            .and()
//            .sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            .and()
//            .authenticationProvider(authenticationProvider)
//            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//            .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
//                .logout()
//                .logoutUrl("/auth/logout").addLogoutHandler(logoutHandler)
//                .logoutSuccessHandler((request, response, authentication) ->
//                        SecurityContextHolder.clearContext());
//        return http.build();
//    }
//}
