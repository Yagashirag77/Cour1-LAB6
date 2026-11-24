// Cette classe réalise les opérations de réservation dans des transactions JPA.
package com.example.service;

import com.example.model.Reservation;
import java.util.List;
import javax.persistence.*;

public class ReservationServiceImpl implements ReservationService {
  private final EntityManagerFactory factory;

  public ReservationServiceImpl(EntityManagerFactory f) {
    factory = f;
  }

  public Reservation creerReservation(Reservation r) {
    r.valider();
    EntityManager em = factory.createEntityManager();
    try {
      em.getTransaction().begin();
      em.persist(r);
      em.getTransaction().commit();
      return r;
    } catch (RuntimeException e) {
      if (em.getTransaction().isActive()) em.getTransaction().rollback();
      throw e;
    } finally {
      em.close();
    }
  }

  public Reservation modifierReservation(Reservation r) {
    r.valider();
    EntityManager em = factory.createEntityManager();
    try {
      em.getTransaction().begin();
      Reservation result = em.merge(r);
      em.flush();
      em.getTransaction().commit();
      return result;
    } catch (RuntimeException e) {
      if (em.getTransaction().isActive()) em.getTransaction().rollback();
      throw e;
    } finally {
      em.close();
    }
  }

  public Reservation trouverParId(Long id) {
    EntityManager em = factory.createEntityManager();
    try {
      return em.find(Reservation.class, id);
    } finally {
      em.close();
    }
  }

  // Les paramètres évitent de construire la requête à partir du texte saisi.
  public List<Reservation> rechercher(String salle, String statut) {
    EntityManager em = factory.createEntityManager();
    try {
      return em.createQuery(
              "select r from Reservation r where (:salle is null or r.salle=:salle) and (:statut is null or r.statut=:statut)",
              Reservation.class)
          .setParameter("salle", salle)
          .setParameter("statut", statut)
          .getResultList();
    } finally {
      em.close();
    }
  }

  public List<Reservation> listerReservations() {
    EntityManager em = factory.createEntityManager();
    try {
      return em.createQuery("select r from Reservation r", Reservation.class).getResultList();
    } finally {
      em.close();
    }
  }
}
