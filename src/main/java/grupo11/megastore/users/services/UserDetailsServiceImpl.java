package grupo11.megastore.users.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import grupo11.megastore.config.UserInfoConfig;
import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.users.model.User;
import grupo11.megastore.users.model.repository.UserRepository;
import grupo11.megastore.exception.ResourceNotFoundException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = userRepository.findByEmailAndStatus(username, EntityStatus.ACTIVE);

        return user.map(UserInfoConfig::new).orElseThrow(() -> new ResourceNotFoundException("User", "email", username));
    }
}
