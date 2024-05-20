package CentralSync.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    //authentication
    public UserDetailsService userDetailsService(){


//        UserDetails admin = User.withUsername("Maleesha")
//                .password(encoder.encode("1234"))
//                .roles("Admin")
//                .build();
//
//
//        UserDetails inRequestHandler = User.withUsername("Ashen")
//                .password(encoder.encode("4567"))
//                .roles("InRequestHandler")
//                .build();
//
//        UserDetails employee = User.withUsername("Kavishka")
//                .password(encoder.encode("7890"))
//                .roles("Employee")
//                .build();
//        return new InMemoryUserDetailsManager(admin,inRequestHandler,employee);
       return new UserInfoUserDetailsService();

    }
//authorization
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
         http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/user/add","/user/welcome").permitAll()
                .anyRequest().authenticated()
                 )
                 .formLogin(formLogin -> formLogin
                         .loginPage("/login")
                         .permitAll()
                 )
                 .rememberMe(Customizer.withDefaults());

        return http.build();
    }
@Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
@Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

}
