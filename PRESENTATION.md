# Présentation du TP 6 — salle de formation

Le modèle ajoute l'organisateur, le sujet, le nombre de participants et le statut de la formation. Le service filtre par salle et statut. Une formation refuse un effectif nul ou des informations manquantes.

La première transaction met à jour la réservation et augmente `version`. Une seconde copie devenue ancienne provoque `OptimisticLockException`. Le retry recharge l'état courant et réapplique uniquement le changement de salle : il ne fusionne pas automatiquement les autres champs.

À expliquer à l'oral : pourquoi une version est nécessaire ? Pour détecter une écriture basée sur des données anciennes. Que fait le retry ? Il recharge la dernière version, puis réessaie au maximum trois fois. Les données H2 sont temporaires.

Commandes : `mvn test` puis `mvn compile exec:java`. Les tests sont exécutés avec JUnit et H2.
