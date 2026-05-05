package com.agenciaespacial;

import com.agenciaespacial.ui.Menu;

/**
 * Punto de entrada de la aplicación.
 * Instancia el menú principal y arranca el bucle de interacción con el usuario.
 */
public class Main {

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.iniciar();
    }
}
