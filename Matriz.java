package com.mycompany.editorsegmentos;

public class Matriz {
	public static double[][] multiplicaMatriz(double[][] a, double[][] b) {
        double[][] resultado = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                resultado[i][j] = 0;
                for (int k = 0; k < 3; k++) {
                    resultado[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return resultado;
    }

    // Multiplica um ponto (vetor coluna 3x1) por uma matriz 3x3
    public static double[] multiplicaPonto(double[] ponto, double[][] matriz) {
        double[] resultado = new double[3];
        for (int i = 0; i < 3; i++) {
            resultado[i] = 0;
            for (int j = 0; j < 3; j++) {
                resultado[i] += matriz[i][j] * ponto[j];
            }
        }
        return resultado;
    }
}
