// Ce service regroupe les opérations proposées pour les réservations.
package com.example.service;

import com.example.model.Reservation;
import java.util.List;

public interface ReservationService {
  Reservation creerReservation(Reservation r);

  Reservation modifierReservation(Reservation r);

  Reservation trouverParId(Long id);

  List<Reservation> listerReservations();

  List<Reservation> rechercher(String salle, String statut);
}
