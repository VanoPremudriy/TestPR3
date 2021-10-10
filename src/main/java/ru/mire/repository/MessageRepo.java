package ru.mire.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.mire.entity.Message;
import ru.mire.entity.User;

import java.util.List;

@Repository
public interface MessageRepo extends CrudRepository<Message, Long> {
    List<Message> findAllBySenderAndRecipient(User sender, User recipient);

}
