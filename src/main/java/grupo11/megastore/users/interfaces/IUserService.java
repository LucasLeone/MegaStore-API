package grupo11.megastore.users.interfaces;

import grupo11.megastore.users.dto.user.CreateUserDTO;
import grupo11.megastore.users.dto.user.RegisterUserDTO;
import grupo11.megastore.users.dto.user.UpdateUserDTO;
import grupo11.megastore.users.dto.user.UserDTO;

import java.util.List;

public interface IUserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO getUserByEmail(String email);
    UserDTO getMyProfile();
    UserDTO registerUser(RegisterUserDTO body);
    UserDTO createUser(CreateUserDTO body);
    UserDTO updateUser(Long id, UpdateUserDTO user);
    void deleteUser(Long id);
}
