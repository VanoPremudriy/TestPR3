package ru.mire.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.mire.entity.Friends;
import ru.mire.entity.Message;
import ru.mire.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepo extends CrudRepository<Friends, Long> {

    List<Friends> findFriendsByMainUser_Id(Long id);

    Friends findFriendByMainUserNull();
    Friends findFriendsByFriendAndMainUser_Id(Long friends, Long mainUser);
    Friends findFriendsByFriendAndMainUser(Long friends, User mainUser);

}
