package grupo11.megastore.users.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import grupo11.megastore.carts.model.Cart;
import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.exception.APIException;
import grupo11.megastore.exception.ResourceNotFoundException;
import grupo11.megastore.users.interfaces.IUserService;
import grupo11.megastore.users.model.repository.RoleRepository;
import grupo11.megastore.users.model.repository.UserRepository;
import grupo11.megastore.users.model.Role;
import grupo11.megastore.users.model.User;
import grupo11.megastore.users.model.Address;
import grupo11.megastore.users.dto.IAddressMapper;
import grupo11.megastore.users.dto.IUserMapper;
import grupo11.megastore.users.dto.user.UpdateUserDTO;
import grupo11.megastore.users.dto.user.UserDTO;
import grupo11.megastore.users.dto.user.CreateUserDTO;
import grupo11.megastore.users.dto.user.RegisterUserDTO;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private IUserMapper userMapper;

    @Autowired
    private IAddressMapper addressMapper;

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = this.userRepository.findByStatus(EntityStatus.ACTIVE);

        List<UserDTO> dtos = new ArrayList<>();
        users.forEach(user -> {
            dtos.add(this.userMapper.userToUserDTO(user));
        });

        return dtos;
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = this.userRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        UserDTO dto = this.userMapper.userToUserDTO(user);

        return dto;
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
    public UserDTO getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = this.userRepository.findByEmailAndStatus(email, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));

        return this.userMapper.userToUserDTO(user);
    }

    @Override
    public UserDTO registerUser(RegisterUserDTO body) {
        try {
            User user = this.userMapper.registerUserDTOToUser(body);

            Role role = this.roleRepository.findByName("USER").get();
            user.getRoles().add(role);

            Cart cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);

            User registeredUser = this.userRepository.save(user);

            return this.userMapper.userToUserDTO(registeredUser);
        } catch (DataIntegrityViolationException e) {
            throw new APIException("Ya existe un usuario con ese email o número de teléfono");
        }
    }

    @Override
    public UserDTO createUser(CreateUserDTO body) {
        try {
            User user = this.userMapper.createUserDTOToUser(body);

            Set<Role> roles = new HashSet<>();
            for (String roleName : body.getRoles()) {
                Role role = this.roleRepository.findByName(roleName)
                        .orElseThrow(() -> new ResourceNotFoundException("Rol", "nombre", roleName));
                roles.add(role);
            }
            user.setRoles(roles);

            Cart cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);

            User createdUser = this.userRepository.save(user);

            return this.userMapper.userToUserDTO(createdUser);
        } catch (DataIntegrityViolationException e) {
            throw new APIException("Ya existe un usuario con ese email o número de teléfono");
        }
    }

    @Override
    public UserDTO updateUser(Long id, UpdateUserDTO body) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User currentUser = userRepository.findByEmailAndStatus(currentUsername, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", currentUsername));

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));

        if (!isAdmin && !currentUser.getId().equals(id)) {
            throw new APIException("No tienes permiso para actualizar este usuario");
        }

        User entity = this.userRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        if (body.isEmpty()) {
            throw new APIException("No hay datos para actualizar");
        }

        boolean updated = false;

        if (body.getFirstName() != null) {
            entity.setFirstName(body.getFirstName());
            updated = true;
        }

        if (body.getLastName() != null) {
            entity.setLastName(body.getLastName());
            updated = true;
        }

        if (body.getPhoneNumber() != null) {
            entity.setPhoneNumber(body.getPhoneNumber());
            updated = true;
        }

        if (body.getAddress() != null) {
            Address address = addressMapper.createAddressDTOToAddress(body.getAddress());
            entity.setAddress(address);
            updated = true;
        }

        if (isAdmin) {
            if (body.getEmail() != null) {
                entity.setEmail(body.getEmail());
                updated = true;
            }

            if (body.getRoles() != null && !body.getRoles().isEmpty()) {
                Set<Role> roles = new HashSet<>();
                for (String roleName : body.getRoles()) {
                    Role role = this.roleRepository.findByName(roleName)
                            .orElseThrow(() -> new ResourceNotFoundException("Rol", "nombre", roleName));
                    roles.add(role);
                }
                entity.setRoles(roles);
                updated = true;
            }
        } else {
            if (body.getEmail() != null || (body.getRoles() != null && !body.getRoles().isEmpty())) {
                throw new APIException("No tienes permiso para actualizar email o roles");
            }
        }

        if (!updated) {
            throw new APIException("No hay datos válidos para actualizar");
        }

        try {
            User updatedUser = this.userRepository.save(entity);
            UserDTO dto = this.userMapper.userToUserDTO(updatedUser);
            return dto;
        } catch (DataIntegrityViolationException e) {
            throw new APIException("Ya existe un usuario con ese email o número de teléfono");
        }
    }

    @Override
    public UserDTO updateSelfInfo(UpdateUserDTO body) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User entity = this.userRepository.findByEmailAndStatus(email, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));

        if (body.isEmpty()) {
            throw new APIException("No hay datos para actualizar");
        }

        boolean updated = false;

        if (body.getFirstName() != null) {
            entity.setFirstName(body.getFirstName());
            updated = true;
        }

        if (body.getLastName() != null) {
            entity.setLastName(body.getLastName());
            updated = true;
        }

        if (body.getPhoneNumber() != null) {
            entity.setPhoneNumber(body.getPhoneNumber());
            updated = true;
        }

        if (body.getAddress() != null) {
            Address address = addressMapper.createAddressDTOToAddress(body.getAddress());
            entity.setAddress(address);
            updated = true;
        }

        if (!updated) {
            throw new APIException("No hay datos válidos para actualizar");
        }

        try {
            User updatedUser = this.userRepository.save(entity);
            UserDTO dto = this.userMapper.userToUserDTO(updatedUser);
            return dto;
        } catch (DataIntegrityViolationException e) {
            throw new APIException("Ya existe un usuario con ese email o número de teléfono");
        }
    }

    @Override
    public void deleteUser(Long id) {
        User entity = this.userRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        entity.delete();

        this.userRepository.save(entity);
    }
}
