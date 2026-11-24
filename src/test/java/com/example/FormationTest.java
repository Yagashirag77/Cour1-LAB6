// Ce test vérifie les règles ajoutées pour les formations.
package com.example;

import static org.junit.jupiter.api.Assertions.*;

import com.example.model.Reservation;
import com.example.service.*;
import java.time.LocalDateTime;
import javax.persistence.*;
import org.junit.jupiter.api.Test;

// Vérifie les nouveaux champs et les recherches en base H2.
class FormationTest {
  @Test
  void formations() {
    EntityManagerFactory f = Persistence.createEntityManagerFactory("reservationPU");
    try {
      ReservationService s = new ReservationServiceImpl(f);
      LocalDateTime d = LocalDateTime.now().plusDays(1);
      Reservation r = new Reservation("Atlas", d, d.plusHours(1));
      r.definirFormation("Nadia", "Java", 12, "PLANIFIEE");
      s.creerReservation(r);
      assertEquals(1, s.rechercher("Atlas", "PLANIFIEE").size());
      assertEquals(0, s.rechercher("Atlas", "ANNULEE").size());
      assertEquals("Java", s.trouverParId(r.getId()).getSujet());
      assertThrows(
          IllegalArgumentException.class,
          () -> r.definirFormation("Nadia", "Java", 0, "PLANIFIEE"));
    } finally {
      f.close();
    }
  }
}
