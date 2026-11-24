// Une réservation conserve les informations nécessaires au suivi d'une formation.
package com.example.model;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "reservations")
public class Reservation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String salle;

  @Column(nullable = false)
  private LocalDateTime dateDebut;

  @Column(nullable = false)
  private LocalDateTime dateFin;

  // Hibernate incrémente cette version à chaque modification.
  @Version private int version;

  @Column(nullable = false)
  private String organisateur = "Non renseigné";

  @Column(nullable = false)
  private String sujet = "Formation";

  @Column(nullable = false)
  private int participants = 1;

  @Column(nullable = false)
  private String statut = "PLANIFIEE";

  protected Reservation() {}

  public Reservation(String salle, LocalDateTime debut, LocalDateTime fin) {
    this.salle = salle;
    dateDebut = debut;
    dateFin = fin;
    valider();
  }

  public void definirFormation(String organisateur, String sujet, int participants, String statut) {
    if (organisateur == null
        || organisateur.trim().isEmpty()
        || sujet == null
        || sujet.trim().isEmpty()
        || participants < 1
        || !("PLANIFIEE".equals(statut) || "ANNULEE".equals(statut) || "TERMINEE".equals(statut)))
      throw new IllegalArgumentException("Informations de formation invalides");
    this.organisateur = organisateur;
    this.sujet = sujet;
    this.participants = participants;
    this.statut = statut;
  }

  public String getOrganisateur() {
    return organisateur;
  }

  public String getSujet() {
    return sujet;
  }

  public int getParticipants() {
    return participants;
  }

  public String getStatut() {
    return statut;
  }

  public void valider() {
    if (salle == null
        || salle.trim().isEmpty()
        || dateDebut == null
        || dateFin == null
        || !dateFin.isAfter(dateDebut))
      throw new IllegalArgumentException("Salle et dates valides obligatoires");
  }

  public Long getId() {
    return id;
  }

  public String getSalle() {
    return salle;
  }

  public void setSalle(String s) {
    salle = s;
  }

  public LocalDateTime getDateDebut() {
    return dateDebut;
  }

  public void setDateDebut(LocalDateTime d) {
    dateDebut = d;
  }

  public LocalDateTime getDateFin() {
    return dateFin;
  }

  public void setDateFin(LocalDateTime d) {
    dateFin = d;
  }

  public int getVersion() {
    return version;
  }
}
