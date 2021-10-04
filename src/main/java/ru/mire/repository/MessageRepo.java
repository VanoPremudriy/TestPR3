package ru.mire.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.mire.entity.User;

@Repository
public interface MessageRepo extends CrudRepository<User, Long> {
}
