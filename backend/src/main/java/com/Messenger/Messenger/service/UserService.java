package com.Messenger.Messenger.service;

import com.Messenger.Messenger.domain.Role;
import com.Messenger.Messenger.domain.User;
import com.Messenger.Messenger.repository.RoleRepository;
import com.Messenger.Messenger.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @PersistenceContext
    private EntityManager entityManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElse(new User());

        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public User findUserById(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.orElse(new User());
    }

    public User findUserByEmail(String email){
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.orElse(new User());
    }



    public List<User> allUsers(){
        return userRepository.findAll();
    }

    public boolean saveUser(User user){
        User userToFind = userRepository.findByEmail(user.getEmail()).orElse(null);

        if(userToFind != null){
            return false;
        }

        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;

    }

    public boolean addRoleToUser(User user, String role) {
        User userToFind = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (userToFind == null) {
            return false;
        }

        Role roleToFind = roleRepository.findByName(role).orElse(null);
        if (roleToFind == null) {
            return false;
        }

        Set<Role> roles = new HashSet<>(userToFind.getRoles());
        roles.add(roleToFind);
        userToFind.setRoles(roles);

        userRepository.save(userToFind);

        return true;
    }

    public boolean deleteUser(Long userId){
        if(userRepository.findById(userId).isPresent()){
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public List<User> usergtList(Long idMin){
        return entityManager
                .createQuery("SELECT u FROM User u WHERE u.id > :paramId", User.class)
                .setParameter("paramId", idMin).getResultList();
    }
}