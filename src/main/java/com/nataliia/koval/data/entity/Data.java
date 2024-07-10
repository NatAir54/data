package com.nataliia.koval.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@DynamicUpdate
@Table(name = "data_table")
@Entity
@Getter
@Setter
public class Data {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "data_sequence")
    @SequenceGenerator(name = "data_sequence", sequenceName = "data_sequence")
    private int id;

    @Column(name = "data", nullable = false)
    private byte[] data;

    @Column(name = "modify_at", nullable = false)
    private LocalDateTime modifyAt;
}
