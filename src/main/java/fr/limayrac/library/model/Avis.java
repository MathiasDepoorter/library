package fr.limayrac.library.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "avis")
public class Avis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Commentaire", nullable = false, length = 255)
    private String Commentaire;

    @ManyToOne
    @JoinColumn(name = "idLivre")
    private Livres livre;
}
