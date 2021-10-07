package ru.mire.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.mire.repository.MessageRepo;
import ru.mire.repository.UserRepo;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
public class AuthorizationController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    MessageRepo messageRepo;

    User userNow;

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
        return "/authorizationErrorpassword";
    }

    @GetMapping(value = "profile")
    public String profile(Model myUser){
        myUser.addAttribute("user_now",userNow);
        return "profile";
    }




    @GetMapping(value = "/registration")
    public String registration(@ModelAttribute("new_user") User newUser){
        return "/registration";
    }

    @PostMapping(value = "/registration_new_user")
    public String registrationNewUser(@ModelAttribute("new_user") User newUser){
        System.out.println(newUser);
        userRepo.save(newUser);
        return "redirect:/authorization";
    }
}
