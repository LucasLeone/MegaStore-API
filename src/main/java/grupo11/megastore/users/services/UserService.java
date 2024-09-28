package grupo11.megastore.users.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.exception.BadRequestException;
import grupo11.megastore.exception.UserNotFoundException;
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
        Optional<User> user = this.userRepository.findByIdAndStatus(id, EntityStatus.ACTIVE);

        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario no existe");
        }

        UserDTO dto = this.userMapper.userToUserDTO(user.get());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        Optional<User> user = this.userRepository.findByEmailAndStatus(email, EntityStatus.ACTIVE);

        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario no existe");
        }

        return this.userMapper.userToUserDTO(user.get());
    }

    @Override
    public UserDTO registerUser(RegisterUserDTO body) {
        User existingUser = this.userRepository.findByEmailAndStatus(body.getEmail(), EntityStatus.ACTIVE)
                .orElseThrow(() -> new UserNotFoundException("El usuario con email " + body.getEmail() + " ya existe"));

        if (existingUser != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya existe");
        }

        User user = this.userMapper.registerUserDTOToUser(body);
        user.setPassword(body.getPassword());
        user.setStatus(EntityStatus.ACTIVE);

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new BadRequestException("Rol 'USER' no encontrado"));
        user.getRoles().add(userRole);

        User savedUser = userRepository.save(user);

        return this.userMapper.userToUserDTO(savedUser);
    }

    @Override
    public ResponseEntity<UserDTO> updateUser(Long id, UpdateUserDTO body) {
        // Recupera la entidad directamente del repositorio
        User entity = this.userRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new UserNotFoundException("El usuario con id " + id + " no existe"));

        if (body.isEmpty()) {
            throw new BadRequestException("No hay datos para actualizar");
        }

        // Actualiza solo los campos necesarios
        if (body.getFirstName() != null) {
            entity.setFirstName(body.getFirstName());
        }

        if (body.getLastName() != null) {
            entity.setLastName(body.getLastName());
        }

        // Guarda la entidad actualizada sin alterar otros campos como la contraseña
        User updatedUser = this.userRepository.save(entity);

        // Mapea la entidad actualizada a UserDTO para la respuesta
        UserDTO dto = this.userMapper.userToUserDTO(updatedUser);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public void deleteUser(Long id) {
        User entity = this.userRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new UserNotFoundException("El usuario con id " + id + " no existe"));

        entity.delete();

        this.userRepository.save(entity);
    }
}
