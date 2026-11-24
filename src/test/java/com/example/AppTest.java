// Ce test vérifie les principaux scénarios du TP.
package com.example;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class AppTest {
  @Test
  void demonstration() {
    assertDoesNotThrow(() -> App.main(new String[] {"--sans-console"}));
  }
}
