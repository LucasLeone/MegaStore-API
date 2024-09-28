package grupo11.megastore.users.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.users.model.User;

import java.util.Optional;
import java.util.List;

/**
 * Repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find a user by email and status.
     *
     * @param email The email to search for.
     * @return An optional containing the user if found.
     */
    Optional<User> findByEmailAndStatus(String email, EntityStatus status);

    /**
     * Find a user by email.
     *
     * @param email The email to search for.
     * @return An optional containing the user if found.
     */
    List<User> findByStatus(EntityStatus status);

    /**
     * Find a user by its unique identifier and status.
     *
     * @param id     The unique identifier of the user to search for.
     * @param status The status to search for.
     * @return An optional containing the user if found.
     */
    Optional<User> findByIdAndStatus(Long id, EntityStatus status);
}
