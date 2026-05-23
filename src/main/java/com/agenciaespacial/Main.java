package com.agenciaespacial;

import com.agenciaespacial.ui.App;

/**
 * Entrada auxiliar para iniciar la aplicación JavaFX desde un entorno que
 * espera una clase {@code Main} con método {@code public static void main}.
 *
 * Responsabilidad:
 * - Delegar el arranque en {@link com.agenciaespacial.ui.App#main(String[])}.
 *
 * Autor: Antonio Manuel Rodriguez Palenzuela
 */
public class Main {
    public static void main(String[] args) {
        App.main(args);
    }
}