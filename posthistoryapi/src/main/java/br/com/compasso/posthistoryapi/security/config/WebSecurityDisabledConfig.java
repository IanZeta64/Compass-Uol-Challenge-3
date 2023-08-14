//package br.com.compasso.posthistoryapi.security.config;
//
//import br.com.compasso.posthistoryapi.security.filter.JwtAuthFilter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.logout.LogoutHandler;
//import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
//import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
//;
//
//@Configuration
//@RequiredArgsConstructor
//public class WebSecurityDisabledConfig {
//
//
//    private final JwtAuthFilter jwtAuthFilter;
//
//    private final AuthenticationProvider authenticationProvider;
//    private final LogoutHandler logoutHandler;
//
//
//    @Bean
//    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
//        return new MvcRequestMatcher.Builder(introspector);
//    }
//
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
//
//        return http.csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(requests ->
//                        requests.anyRequest().permitAll()
//                ).build();
//
//    }
//
//}
