package fr.limayrac.library.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "livre")
public class Livres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titre", nullable = false, length = 100)
    private String titre;

    @Column(name = "auteur", nullable = false, length = 50)
    private String auteur;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "nb_pages", nullable = false, length = 50)
    private String nbPages;

    @Column(name = "etat", length = 50)
    private String etat;

    @Column(name = "date_parution", length = 50)
    private String dateParution;

    @OneToMany(mappedBy = "livre")
    protected List<Emprunts> emprunts = new ArrayList<>();

    @OneToMany(mappedBy = "livre")
    protected List<Avis> avis = new ArrayList<>();


}
