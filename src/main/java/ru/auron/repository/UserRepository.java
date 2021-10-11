package ru.auron.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.auron.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
