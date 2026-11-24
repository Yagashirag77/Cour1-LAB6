// Ce test vérifie la détection des conflits et les dates invalides.
package com.example;

import static org.junit.jupiter.api.Assertions.*;

import com.example.model.Reservation;
import com.example.service.*;
import java.time.LocalDateTime;
import javax.persistence.*;
import org.junit.jupiter.api.*;

class ReservationTest {
  @Test
  void conflitEtRetry() {
    EntityManagerFactory f = Persistence.createEntityManagerFactory("reservationPU");
    try {
      ReservationService s = new ReservationServiceImpl(f);
      LocalDateTime d = LocalDateTime.now();
      Reservation r = s.creerReservation(new Reservation("A", d, d.plusHours(1)));
      Reservation a = s.trouverParId(r.getId()), b = s.trouverParId(r.getId());
      a.setSalle("B");
      s.modifierReservation(a);
      b.setSalle("C");
      assertThrows(OptimisticLockException.class, () -> s.modifierReservation(b));
      new ReservationRetryHandler(s).modifierAvecRetry(b);
      assertEquals("C", s.trouverParId(r.getId()).getSalle());
    } finally {
      f.close();
    }
  }

  @Test
  void datesInvalides() {
    LocalDateTime d = LocalDateTime.now();
    assertThrows(IllegalArgumentException.class, () -> new Reservation("A", d, d));
  }
}
