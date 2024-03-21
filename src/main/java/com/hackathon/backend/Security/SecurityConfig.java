package com.hackathon.backend.Security;


import com.hackathon.backend.Entities.CountryEntity;
import com.hackathon.backend.Entities.RoleEntity;
import com.hackathon.backend.Repositories.CountryRepository;
import com.hackathon.backend.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JWTAuthEntryPoint jwtAuthEntryPoint;

    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          JWTAuthEntryPoint jwtAuthEntryPoint){
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(configure-> configure.authenticationEntryPoint(jwtAuthEntryPoint))
                .sessionManagement(configure-> configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/v1/**").permitAll()
                                .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }

//    @Bean
//    CommandLineRunner commandLineRunner(RoleRepository roleRepository, CountryRepository countryRepository){
//        return args -> {
//            RoleEntity roleEntity = new RoleEntity("USER");
//            roleRepository.save(roleEntity);
//
//            CountryEntity countryEntity = new CountryEntity("Palestine");
//            countryRepository.save(countryEntity);
//
//            List<TodoListEntity> l = new ArrayList<>();
//            l.add(new TodoListEntity("Test Todos 1",false));
//            l.add(new TodoListEntity("Test Todos 2",false));
//            l.add(new TodoListEntity("Test Todos 3",false));
//            l.add(new TodoListEntity("Test Todos 4",false));
//            l.add(new TodoListEntity("Test Todos 5",false));
//            l.add(new TodoListEntity("Test Todos 6",false));
//            l.add(new TodoListEntity("Test Todos 7",false));
//
//            CountryEntity country = countryRepository.findByCountry("Palestine").orElseThrow(()-> new EntityNotFoundException("error"));
//            for (TodoListEntity todoList : l) {
//                country.getTodos().add(todoList);
//            }
//            countryRepository.save(country);
//
//            CountryEntity countrys = countryRepository.findByCountry("Palestine").orElseThrow(()-> new EntityNotFoundException("error"));
//            RoadmapEntity roadmapEntity = new RoadmapEntity("Filename_egypt");
//            country.setRoadmap(List.of(roadmapEntity));
//            countryRepository.save(country);
//
//        };
//    }

}
