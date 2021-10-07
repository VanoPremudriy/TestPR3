package ru.mire.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.mire.entity.Message;

@Repository
public interface MessageRepo extends CrudRepository<Message, Long> {
}
