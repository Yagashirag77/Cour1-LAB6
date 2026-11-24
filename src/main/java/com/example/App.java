// La classe principale prépare les exemples et lance la démonstration.
package com.example;

import com.example.model.Reservation;
import com.example.service.*;
import java.time.LocalDateTime;
import java.io.IOException;
import java.sql.SQLException;
import javax.persistence.*;
import org.h2.tools.Server;

public class App {
  public static void main(String[] args) {
    EntityManagerFactory f = Persistence.createEntityManagerFactory("reservationPU");
    try {
      ReservationService s = new ReservationServiceImpl(f);
      LocalDateTime d = LocalDateTime.now().plusDays(1);
      Reservation r = s.creerReservation(new Reservation("Atlas", d, d.plusHours(2)));
      r.definirFormation("Nadia", "Initiation Java", 12, "PLANIFIEE");
      r = s.modifierReservation(r);
      System.out.println("Formations planifiées : " + s.rechercher("Atlas", "PLANIFIEE").size());
      // Deux personnes lisent séparément la même version de la réservation.
      Reservation a = s.trouverParId(r.getId());
      Reservation b = s.trouverParId(r.getId());
      System.out.println("\n=== Première personne : modification de la salle ===");
      a.setSalle("Rif");
      s.modifierReservation(a);
      // La deuxième personne possède encore la version avant cette modification.
      b.setSalle("Sahara");
      System.out.println("\n=== Deuxième personne : détection du conflit ===");
      try {
        s.modifierReservation(b);
        throw new IllegalStateException("Conflit attendu");
      } catch (OptimisticLockException e) {
        System.out.println("Conflit détecté : version périmée");
      }
      // Le retry recharge les données et réapplique le changement de salle.
      System.out.println("\n=== Nouvelle tentative après rechargement ===");
      Reservation updated = new ReservationRetryHandler(s).modifierAvecRetry(b);
      System.out.println(
          "Nouvelle salle : " + updated.getSalle() + ", version " + updated.getVersion());

      // Les tests automatiques ne doivent pas attendre une saisie au clavier.
      if (args.length == 0 || !"--sans-console".equals(args[0])) {
        consulterBaseH2();
      }
    } finally {
      f.close();
    }
  }

  private static void consulterBaseH2() {
    Server console = null;
    try {
      // La console démarre dans le même programme que la base en mémoire.
      console = Server.createWebServer("-webPort", "8083").start();
      System.out.println("\n=== Consulter la base H2 ===");
      System.out.println("Ouvre : http://localhost:8083");
      System.out.println("URL JDBC : jdbc:h2:mem:reservations;DB_CLOSE_DELAY=-1");
      System.out.println("Utilisateur : sa | Mot de passe : laisser vide");
      System.out.println("Requête : SELECT * FROM reservations;");
      System.out.println("Garde cette fenêtre ouverte, puis appuie sur Entrée pour terminer.");
      System.in.read();
    } catch (SQLException | IOException exception) {
      throw new IllegalStateException("Impossible de démarrer ou de fermer la console H2", exception);
    } finally {
      if (console != null) {
        console.stop();
      }
    }
  }
}
