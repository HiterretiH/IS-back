package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.Role;
import org.lab.model.User;
import org.lab.repository.UserRepository;
import org.lab.utils.JwtUtils;
import org.lab.utils.PasswordUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class UserService {

    @Inject
    private UserRepository userRepository;

    public Map<String, String> register(User user) {
        user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
        int numberOfUsers = userRepository.findAll().size();
        user.setRole(numberOfUsers == 0 ? Role.MANAGER : Role.USER);
        User createdUser = userRepository.save(user);
        String token = JwtUtils.generateToken(createdUser);

        Map<String, String> response = new HashMap<>();
        response.put("id", createdUser.getId().toString());
        response.put("username", createdUser.getUsername());
        response.put("role", createdUser.getRole().toString());
        response.put("token", token);
        return response;
    }

    public Map<String, String> login(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null && existingUser.getPassword().equals(PasswordUtils.hashPassword(user.getPassword()))) {
            String token = JwtUtils.generateToken(existingUser);
            Map<String, String> response = new HashMap<>();
            response.put("id", existingUser.getId().toString());
            response.put("username", existingUser.getUsername());
            response.put("role", existingUser.getRole().toString());
            response.put("token", token);
            return response;
        }
        return null;
    }

    public User getById(Integer id) {
        return userRepository.findById(id);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void delete(Integer id) {
        userRepository.delete(id);
    }
}
