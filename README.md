# TP 6 — Verrouillage optimiste

Depuis ce dossier, exécuter `mvn test` puis `mvn compile exec:java`.

Deux copies de la même réservation sont chargées. La première modification incrémente le champ `@Version` ; la deuxième est rejetée car sa version est ancienne. Le service recharge alors la réservation et réapplique la modification de salle, avec trois tentatives maximum. Le retry porte volontairement sur la salle seulement ; il ne fusionne pas automatiquement les autres champs.

La base H2 est en mémoire et recréée à chaque démarrage. Les tests vérifient le conflit, le retry, les dates invalides et l'exécution de la démonstration.
