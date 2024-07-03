/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplication22;

import java.util.Random;

/**
 *
 * @author metal
 */


import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class JavaApplication22 {

    public static void main(String[] args) {
        final int tamaño = 100;
        int[][] matriz = new int[tamaño][tamaño];

        Random random = new Random();
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                matriz[i][j] = random.nextInt(100); // Genera números entre 0 y 999
            }
        }

        System.out.println("Matriz generada:");

        
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                System.out.print(matriz[i][j] + "\t");
            }
            System.out.println();
        }

        
        int[] array = new int[tamaño * tamaño];
        int index = 0;
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                array[index++] = matriz[i][j];
            }
        }

       
        Arrays.sort(array);

        try (Scanner input = new Scanner(System.in)) {
            boolean continuarBuscando;

            do {
                
                System.out.print("Por favor ingrese un número de la matriz: ");
                int target = input.nextInt();

                
                int bus = Arrays.binarySearch(array, target);

                if (bus >= 0) {
                    
                    int fila = bus / tamaño;
                    int columna = bus % tamaño;
                    System.out.println("El numero " + target + " esta en (" + (fila + 1) + ", " + (columna + 1) + ")");
                } else {
                    System.out.println("Lo siento " + target + " no está en la matriz.");
                }

                System.out.println("-------------------------------------------");
                System.out.print("Desea buscar otro número? (SI/NO): ");
                continuarBuscando = input.next().equalsIgnoreCase("SI");

            } while (continuarBuscando);
        }
    }
}



    

