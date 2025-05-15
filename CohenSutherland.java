package com.mycompany.editorsegmentos;

public class CohenSutherland {

    public static int codigoPonto(int x, int y, int xmin, int xmax, int ymin, int ymax) {
        int codigo = 0;
        if (x < xmin) codigo |= 8;
        if (x > xmax) codigo |= 4;
        if (y < ymin) codigo |= 2;
        if (y > ymax) codigo |= 1;
        return codigo;
    }

    public static Segmento recortarLinha(Segmento s, int xmin, int xmax, int ymin, int ymax) {
        int x1 = s.pontoInicial.x;
        int y1 = s.pontoInicial.y;
        int x2 = s.pontoFinal.x;
        int y2 = s.pontoFinal.y;

        int codigo1 = codigoPonto(x1, y1, xmin, xmax, ymin, ymax);
        int codigo2 = codigoPonto(x2, y2, xmin, xmax, ymin, ymax);

        while (true) {
            if ((codigo1 | codigo2) == 0) {
                return new Segmento(new java.awt.Point(x1, y1), new java.awt.Point(x2, y2), s.cor);
            } else if ((codigo1 & codigo2) != 0) {
                return null;
            } else {
                int codigoFora = (codigo1 != 0) ? codigo1 : codigo2;
                int x = 0, y = 0;
                double m = (x2 - x1) != 0 ? (double) (y2 - y1) / (x2 - x1) : 0;

                if ((codigoFora & 1) != 0) {
                    x = x1 + (int) ((ymax - y1) / m);
                    y = ymax;
                } else if ((codigoFora & 2) != 0) {
                    x = x1 + (int) ((ymin - y1) / m);
                    y = ymin;
                } else if ((codigoFora & 4) != 0) {
                    y = y1 + (int) (m * (xmax - x1));
                    x = xmax;
                } else if ((codigoFora & 8) != 0) {
                    y = y1 + (int) (m * (xmin - x1));
                    x = xmin;
                }

                if (codigoFora == codigo1) {
                    x1 = x;
                    y1 = y;
                    codigo1 = codigoPonto(x1, y1, xmin, xmax, ymin, ymax);
                } else {
                    x2 = x;
                    y2 = y;
                    codigo2 = codigoPonto(x2, y2, xmin, xmax, ymin, ymax);
                }
            }
        }
    }
}
