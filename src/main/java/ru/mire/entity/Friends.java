package ru.mire.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Entity(name = "friends")
public class Friends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long friend;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "main_user_id")
    private User mainUser;

    public Friends(Long id, Long friend, User mainUser) {
        this.id = id;
        this.friend = friend;
        this.mainUser = mainUser;
    }

    public Friends() {

    }
}
