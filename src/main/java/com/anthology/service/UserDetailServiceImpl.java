package com.anthology.service;


import com.anthology.repository.CredentialsRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final CredentialsRepository credentialsRepository;
        @Override
        public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException {
            return credentialsRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("No se encontro al usuario con ese username: ( " + username + " )"));
        }

}
