package ru.mire.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.mire.entity.Friends;
import ru.mire.entity.Message;
import ru.mire.entity.User;
import ru.mire.repository.FriendsRepo;
import ru.mire.repository.MessageRepo;
import ru.mire.repository.UserRepo;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class DefaultController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    MessageRepo messageRepo;

    @Autowired
    FriendsRepo friendsRepo;

    User userNow;

    User chatUser;

    @GetMapping(value = {"/","/authorization"})
    public String authorization(@ModelAttribute("that_user") User thatUser){
        return "/authorization";
    }

    @PostMapping(value = "/login")
    public String login(@ModelAttribute("that_user") User thatUser){
        Iterable<User> user = userRepo.findAll();
        AtomicBoolean isAuthorizatioinError = new AtomicBoolean(true);
        AtomicBoolean isAuthorizaionErrorPassword = new AtomicBoolean(false);

        user.forEach(user1 -> {
            boolean isLogin = Objects.equals(user1.getLogin(), thatUser.getLogin());
            boolean isPassword = Objects.equals(user1.getPassword(), thatUser.getPassword());
            if (isLogin && isPassword) {userNow = user1;
                isAuthorizatioinError.set(false);
            }
            if (isLogin && !isPassword) isAuthorizaionErrorPassword.set(true);

        });
        if (isAuthorizaionErrorPassword.get()) return "/authorizationErrorPassword";
        else if (isAuthorizatioinError.get()) return "/authorizationError";
        return "redirect:/profile";

    }

    @GetMapping(value = "/authorizationError")
    public String authorizationError(){
        return "/authorizationError";
    }

    @GetMapping(value = "/authorizationErrorPassword")
    public String authorizationErrorPassword(){
        return "/authorizationErrorPassword";
    }

    @GetMapping(value = "/profile")
    public String profile(@ModelAttribute("new_friend") User newFriend,  Model myUser, Model usersOnline, Model userFriends){
        myUser.addAttribute("user_now",userNow);
        ArrayList<User> users = (ArrayList<User>) userRepo.findAll();
        ArrayList<User> users2 = (ArrayList<User>) userRepo.findAll();
        users.removeIf(user -> user.getId().equals(userNow.getId()));
        usersOnline.addAttribute("users", users);
        ArrayList<Friends> friends = (ArrayList<Friends>) friendsRepo.findFriendsByMainUser_Id(userNow.getId());
        friends.forEach(friends1 -> users.removeIf(user -> user.getId().equals(friends1.getFriend())));
        users.forEach(user -> users2.removeIf(user1 -> user1.equals(user)));
        users2.removeIf(user -> user.getId().equals(userNow.getId()));
        userFriends.addAttribute("friends", users2);
        return "/profile";
    }


    @PostMapping(value = "/add_friend")
    public String addFriend(Long id1, Model mod1){
        mod1.addAttribute("id1", id1);
        Friends friend;
        AtomicInteger i= new AtomicInteger();
        friendsRepo.findAll().forEach(friends -> i.getAndIncrement());
        friend = new Friends((long) i.get()+1, id1, userNow);
        friendsRepo.save(friend);
        friend = new Friends((long) i.get()+1, userNow.getId(), userRepo.getUserById(id1));
        friendsRepo.save(friend);
        i.set(0);
        return "redirect:/profile";
    }

    @PostMapping(value = "/delete_friend")
    public String deleteFriend(Long id1, Model mod1){
        mod1.addAttribute("id1", id1);
        User doll = new User();
        Friends friend = friendsRepo.findFriendsByFriendAndMainUser_Id(id1,userNow.getId());
        friend.setMainUser(doll);
        friendsRepo.delete(friend);
        friend = friendsRepo.findFriendsByFriendAndMainUser_Id(userNow.getId(), id1);
        friend.setMainUser(doll);
        friendsRepo.delete(friend);

        return "redirect:/profile";
    }


    @GetMapping(value = "/messenger")
    public String messenger(@ModelAttribute("new_message") Message newMessage, Long id1, Model mod1, Model chatMessages, Model un){
        mod1.addAttribute("id1", id1);
        un.addAttribute("user_now", userNow);
        chatUser = userRepo.getUserById(id1);
        ArrayList<Message> userNowMessages = (ArrayList<Message>) messageRepo.findAllBySenderAndRecipient(userNow, chatUser);
        ArrayList<Message> chatUserMessages = (ArrayList<Message>) messageRepo.findAllBySenderAndRecipient(chatUser, userNow);

        ArrayList<Message> messages = new ArrayList<>();

        messages.addAll(userNowMessages);
        messages.addAll(chatUserMessages);

        messages.sort((o1, o2) -> o1.getId().compareTo(o2.getId()));

        chatMessages.addAttribute("messages", messages);

        return "/messenger";
    }

    @PostMapping(value = "/send_message")
    public String sendMessage(@ModelAttribute("new_message") Message newMessage){
        System.out.println(newMessage.getMess());
        AtomicInteger i = new AtomicInteger();
        messageRepo.findAll().forEach(message -> i.getAndIncrement());
        messageRepo.save(new Message((long) i.get()+1, newMessage.getMess(), userNow, chatUser));
        return "redirect:/messenger?id1=" + chatUser.getId();
    }


    @GetMapping(value = "/registration")
    public String registration(@ModelAttribute("new_user") User newUser){
        return "/registration";
    }

    @PostMapping(value = "/registration_new_user")
    public String registrationNewUser(@ModelAttribute("new_user") User newUser){
        userRepo.save(newUser);
        return "redirect:/authorization";
    }

    @GetMapping(value = "exit")
    public String exit(){
        userNow = null;
        return "redirect:/authorization";
    }

    @GetMapping(value = "exit_messenger")
    public String exitMessenger(){
        chatUser = null;
        return "redirect:/profile";
    }
}
