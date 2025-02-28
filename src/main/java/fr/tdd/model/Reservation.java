package fr.tdd.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reservation")
@Getter
@Setter
public class Reservation {
    @Id
    @UuidGenerator
    private String id;

    @Column(name = "livre")
    private Livre livre;

    @Column(name = "adherent")
    private Adherent adherent;

    @Column(name = "dateReservation")
    private LocalDateTime dateReservation;

    @Column(name = "dateFin")
    private LocalDateTime dateFin;


}
