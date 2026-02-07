package com.java.test.entity;

import com.java.test.enums.RuoloEnum;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Getter
@Entity
@Table(name = "RUOLO")
public class RuoloEntity {

    protected RuoloEntity()
    {

    }

    public RuoloEntity(RuoloEnum ruolo)
    {
        this.ruolo = ruolo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,unique = true)
    private RuoloEnum ruolo;

    @Column(name = "RUOLO_ID",unique = true,nullable = false)
    private String ruoloId;

    @PrePersist
    private void init()
    {
        this.ruoloId = UUID.randomUUID().toString();
    }
}
