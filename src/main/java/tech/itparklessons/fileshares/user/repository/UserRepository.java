package tech.itparklessons.fileshares.user.repository;

import tech.itparklessons.fileshares.user.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findUserByLogin(String login);
}
