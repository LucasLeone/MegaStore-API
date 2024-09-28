package grupo11.megastore.users.interfaces;

import org.springframework.http.ResponseEntity;
import grupo11.megastore.users.dto.user.RegisterUserDTO;
import grupo11.megastore.users.dto.user.UpdateUserDTO;
import grupo11.megastore.users.dto.user.UserDTO;

import java.util.List;

/**
 * Service interface for managing User entities.
 * Defines methods for CRUD operations and other business logic related to
 * users.
 */
public interface IUserService {
    /**
     * Retrieves all users.
     *
     * @return A ResponseEntity containing a list of UserDTO objects.
     */
    ResponseEntity<List<UserDTO>> getAllUsers();

    /**
     * Retrieves a specific user by its unique identifier.
     *
     * @param id The unique identifier of the user to retrieve.
     * @return A ResponseEntity containing the UserDTO if found, or an
     * appropriate HTTP status otherwise.
     */
    ResponseEntity<UserDTO> getUserById(Long id);

    /**
     * Retrieves a specific user by its email.
     *
     * @param email The email of the user to retrieve.
     * @return A ResponseEntity containing the UserDTO if found, or an
     * appropriate HTTP status otherwise.
     */
    UserDTO getUserByEmail(String email);

    /**
     * Registers a new user.
     *
     * @param body The RegisterUserDTO containing the details of the user to
     *             be registered.
     * @return The created UserDTO.
     */
    UserDTO registerUser(RegisterUserDTO body);

    /**
     * Updates an existing user.
     *
     * @param id   The unique identifier of the user to update.
     * @param user The UpdateUserDTO containing the updated details of the
     *             user.
     * @return A ResponseEntity containing the updated UserDTO if the update is
     * successful, or an appropriate HTTP status otherwise.
     */
    ResponseEntity<UserDTO> updateUser(Long id, UpdateUserDTO user);

    /**
     * Deletes a user.
     *
     * @param id The unique identifier of the user to delete.
     */
    void deleteUser(Long id);
}
