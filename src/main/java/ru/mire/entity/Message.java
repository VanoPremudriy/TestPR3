package ru.mire.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Entity(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String mess;

    @Column
    private String status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipient_id")
    private User recipient;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_id")
    private User sender;

    public Message(){

    }
}
