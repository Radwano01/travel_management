package com.hackathon.backend.Security;


import com.hackathon.backend.RelationShips.RoleEntity;
import com.hackathon.backend.Entities.UserEntity;
import com.hackathon.backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Autowired
    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username not found"));
        return new User(userEntity.getUsername(),userEntity.getPassword(), mapAuthority(userEntity.getRole()));
    }

    private Collection<GrantedAuthority> mapAuthority(List<RoleEntity> roles){
        return roles.stream().map(role->new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
    }
}
