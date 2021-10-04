package ru.mire.entity;

import javax.persistence.*;

@Entity(name = "message")
public class Message {

    @Id
    private Long id;

    @Column
    private String mess;

    @Column(name = "sender_id")
    private Long senderID;

    @Column(name = "recipient_id")
    private Long recipientID;

    @Column
    private String status;
}
