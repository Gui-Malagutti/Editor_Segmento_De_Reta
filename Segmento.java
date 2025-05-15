package com.mycompany.editorsegmentos;

import java.awt.*;

public class Segmento {
	Point pontoInicial;
	Point pontoFinal;
	Color cor;

	public Segmento(Point pontoInicial, Point pontoFinal, Color cor) {
		this.pontoInicial = pontoInicial;
		this.pontoFinal = pontoFinal;
		this.cor = cor;
	}

	public void desenhar(Graphics g) {
		g.setColor(cor);
		g.drawLine(pontoInicial.x, pontoInicial.y, pontoFinal.x, pontoFinal.y);
	}

	public boolean contemPoint(Point ponto) {
		int margem = 5;
		int minX = Math.min(pontoInicial.x, pontoFinal.x) - margem;
		int maxX = Math.max(pontoInicial.x, pontoFinal.x) + margem;
		int minY = Math.min(pontoInicial.y, pontoFinal.y) - margem;
		int maxY = Math.max(pontoInicial.y, pontoFinal.y) + margem;
		return (ponto.x >= minX && ponto.x <= maxX && ponto.y >= minY && ponto.y <= maxY);
	}

	public void translacao(int tx, int ty) {
		double[][] matriz = { { 1, 0, tx }, { 0, 1, ty }, { 0, 0, 1 } };
		aplicarTransformacao(matriz);
	}

	public void rotacao(int anguloGraus) {
		double angulo = Math.toRadians(anguloGraus);
		int px = pontoInicial.x;
		int py = pontoInicial.y;

		// Transladar para a origem
		double[][] translacaoOrigem = { { 1, 0, -px }, { 0, 1, -py }, { 0, 0, 1 } };

		// Rotação em torno da origem
		double[][] rotacao = { { Math.cos(angulo), -Math.sin(angulo), 0 }, { Math.sin(angulo), Math.cos(angulo), 0 },
				{ 0, 0, 1 } };

		// Transladar de volta
		double[][] translacaoVolta = { { 1, 0, px }, { 0, 1, py }, { 0, 0, 1 } };

		// Compor matriz de rotação em torno de pontoInicial
		double[][] composta = Matriz.multiplicaMatriz(translacaoVolta,
				Matriz.multiplicaMatriz(rotacao, translacaoOrigem));
		aplicarTransformacao(composta);
	}

	public void escala(int ex, int ey) {
		int px = pontoInicial.x;
        int py = pontoInicial.y;

        // Transladar para a origem
        double[][] translacaoOrigem = {
            {1, 0, -px},
            {0, 1, -py},
            {0, 0, 1}
        };

        // Escala
        double[][] escala = {
            {ex, 0, 0},
            {0, ey, 0},
            {0, 0, 1}
        };

        // Transladar de volta
        double[][] translacaoVolta = {
            {1, 0, px},
            {0, 1, py},
            {0, 0, 1}
        };

        // Compor transformação
        double[][] composta = Matriz.multiplicaMatriz(translacaoVolta, Matriz.multiplicaMatriz(escala, translacaoOrigem));
        aplicarTransformacao(composta);
	}

	private void aplicarTransformacao(double[][] matriz) {
		double[] p1 = { pontoInicial.x, pontoInicial.y, 1 };
		double[] p2 = { pontoFinal.x, pontoFinal.y, 1 };

		double[] novoP1 = Matriz.multiplicaPonto(p1, matriz);
		double[] novoP2 = Matriz.multiplicaPonto(p2, matriz);

		pontoInicial = new Point((int) novoP1[0], (int) novoP1[1]);
		pontoFinal = new Point((int) novoP2[0], (int) novoP2[1]);
	}

}