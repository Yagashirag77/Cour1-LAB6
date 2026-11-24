// Cette classe recharge une réservation lorsqu'une modification concurrente est détectée.
package com.example;

import com.example.model.Reservation;
import com.example.service.ReservationService;
import javax.persistence.OptimisticLockException;

public class ReservationRetryHandler {
  private final ReservationService service;

  public ReservationRetryHandler(ReservationService service) {
    this.service = service;
  }

  public Reservation modifierAvecRetry(Reservation r) {
    String salle = r.getSalle();
    for (int i = 0; i < 3; i++) {
      try {
        return service.modifierReservation(r);
      } catch (OptimisticLockException e) {
        if (i == 2) throw e;
        r = service.trouverParId(r.getId());
        if (r == null) throw new IllegalStateException("Réservation supprimée", e);
        r.setSalle(salle);
      }
    }
    throw new IllegalStateException();
  }
}
