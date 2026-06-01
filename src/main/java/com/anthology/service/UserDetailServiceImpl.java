package com.anthology.service;


import com.anthology.model.User;
import com.anthology.model.UserEntity;
import com.anthology.repository.UserEntityRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

        private final UserEntityRepository userEntityRepository;
        @Override
        public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException {

            UserEntity userEntity = userEntityRepository.findUserEntityByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("No se encontro usuario con ese username: (" + username + ")"));


            List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

            userEntity.getRoles()
                    .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole().name()))));

            userEntity.getRoles().stream()
                    .flatMap(role -> role.getPermissionList().stream())
                    .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));



/*
            return new User(userEntity.getUsername(),
                    userEntity.getPassword(),
                    userEntity.isEnabled(),
                    userEntity.isAccountNoExpired(),
                    userEntity.isCredentialNoExpired(),
                    userEntity.isAccountNoLocked(),
                    authorityList);
        */
        }
}
