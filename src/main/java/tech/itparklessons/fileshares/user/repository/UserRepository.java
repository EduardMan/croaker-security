package tech.itparklessons.fileshares.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.itparklessons.fileshares.user.model.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findUserByLogin(String login);

    @Query(value = "SELECT id, login, username, email FROM users WHERE login = :loginOrEmail OR email = :loginOrEmail", nativeQuery = true)
    Users findByLoginOrEmailIs(String loginOrEmail);
}
