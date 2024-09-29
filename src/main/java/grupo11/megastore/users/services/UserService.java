package grupo11.megastore.users.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.exception.APIException;
import grupo11.megastore.exception.ResourceNotFoundException;
import grupo11.megastore.users.dto.IUserMapper;
import grupo11.megastore.users.interfaces.IUserService;
import grupo11.megastore.users.model.repository.RoleRepository;
import grupo11.megastore.users.model.repository.UserRepository;
import grupo11.megastore.users.model.Role;
import grupo11.megastore.users.model.User;
import grupo11.megastore.users.dto.user.UpdateUserDTO;
import grupo11.megastore.users.dto.user.UserDTO;
import grupo11.megastore.users.dto.user.RegisterUserDTO;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private IUserMapper userMapper;

    @Override
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = this.userRepository.findByStatus(EntityStatus.ACTIVE);

        List<UserDTO> dtos = new ArrayList<>();
        users.forEach(user -> {
            dtos.add(this.userMapper.userToUserDTO(user));
        });

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(Long id) {
        User user = this.userRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        UserDTO dto = this.userMapper.userToUserDTO(user);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        Optional<User> user = this.userRepository.findByEmailAndStatus(email, EntityStatus.ACTIVE);

        if (user.isEmpty()) {
            throw new ResourceNotFoundException("Usuario", "email", email);
        }

        return this.userMapper.userToUserDTO(user.get());
    }

    @Override
    public UserDTO registerUser(RegisterUserDTO body) {
        try {
            User user = this.userMapper.registerUserDTOToUser(body);

            Role role = this.roleRepository.findByName("USER").get();
            user.getRoles().add(role);

            User registeredUser = this.userRepository.save(user);

            return this.userMapper.userToUserDTO(registeredUser);
        } catch (DataIntegrityViolationException e) {
            throw new APIException("Ya existe un usuario con ese email");
        }
    }

    @Override
    public ResponseEntity<UserDTO> updateUser(Long id, UpdateUserDTO body) {
        User entity = this.userRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (body.isEmpty()) {
            throw new APIException("No hay datos para actualizar");
        }

        if (body.getFirstName() != null) {
            entity.setFirstName(body.getFirstName());
        }

        if (body.getLastName() != null) {
            entity.setLastName(body.getLastName());
        }

        User updatedUser = this.userRepository.save(entity);

        UserDTO dto = this.userMapper.userToUserDTO(updatedUser);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public void deleteUser(Long id) {
        User entity = this.userRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        entity.delete();

        this.userRepository.save(entity);
    }
}
