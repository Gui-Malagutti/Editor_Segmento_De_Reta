package com.mycompany.editorsegmentos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class EditorSegmentos extends JFrame {
	private JPanel painelDesenho;
	private JButton botaoCorAzul, botaoCorVermelho, botaoRecortar, botaoApagar, botaoMover, botaoCorVerde, botaoCorAmarela;
	private JButton botaoTranslacao, botaoRotacao, botaoEscala;
	private Color corAtual = Color.BLUE;
	private Point pontoInicial = null;
	private List<Segmento> segmentos = new ArrayList<>();
	private Segmento segmentoSelecionado = null;
	private boolean recortando = false;
	private Rectangle janelaRecorte = null;
	private boolean movendo = false;
	private Point pontoInicialMovimento = null;
	private boolean modoMover = false;


	public EditorSegmentos() {
		setTitle("Editor de Segmentos");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		painelDesenho = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				desenharSegmentos(g);
				darOutlineNoSegmento(g);
				desenharJanelaRecorte(g);
			}
		};
		painelDesenho.setBackground(Color.WHITE);
		painelDesenho.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    	if (modoMover) {
		            segmentoSelecionado = selecionarSegmento(e.getPoint());
		            pontoInicialMovimento = e.getPoint();
		            return;
		        }
		    	if (!recortando && pontoInicial == null) {
		    		Segmento segmento = selecionarSegmento(e.getPoint());
		    		
		    		if (segmento != null) {
		    			segmentoSelecionado = segmento;
		    			repaint();
		    			return;
		    		} 
		    		if (segmento == null && segmentoSelecionado != null) {
		    			segmentoSelecionado = null;
		    			repaint();
		    			return;
		    		}
		    	}
		        if (recortando) {
		        	pontoInicialMovimento = e.getPoint();
		            movendo = true;
		            janelaRecorte = null;
		        } else {
		            if (pontoInicial == null) {
		                pontoInicial = e.getPoint();
		            } else {
		                segmentos.add(new Segmento(pontoInicial, e.getPoint(), corAtual));
		                pontoInicial = null;
		                repaint();
		            }
		        }
		    }

		    @Override
		    public void mouseReleased(MouseEvent e) {
		        if (recortando && pontoInicialMovimento != null) {
		            Point pontoFinal = e.getPoint();
		            if (pontoInicialMovimento.equals(pontoFinal)) {
		                janelaRecorte = null;
		            } else {
		                int x = Math.min(pontoInicialMovimento.x, pontoFinal.x);
		                int y = Math.min(pontoInicialMovimento.y, pontoFinal.y);
		                int w = Math.abs(pontoInicialMovimento.x - pontoFinal.x);
		                int h = Math.abs(pontoInicialMovimento.y - pontoFinal.y);
		                janelaRecorte = new Rectangle(x, y, w, h);
		            }
		            movendo = false;
		            pontoInicialMovimento = null;
		            if (janelaRecorte != null) {
			            List<Segmento> recortados = new ArrayList<>();
			            for (Segmento s : segmentos) {
			                Segmento novo = CohenSutherland.recortarLinha(
			                    s,
			                    janelaRecorte.x,
			                    janelaRecorte.x + janelaRecorte.width,
			                    janelaRecorte.y,
			                    janelaRecorte.y + janelaRecorte.height
			                );
			                if (novo != null) {
			                    recortados.add(novo);
			                }
			            }
			            segmentos = recortados;
			            recortando = false;
			        }
			        janelaRecorte = null;
			        botaoRecortar.setText("Recortar");
			       
		            repaint();
		        }
		    }
		});

		painelDesenho.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
			    if (modoMover && segmentoSelecionado != null && pontoInicialMovimento != null) {
			        int dx = e.getX() - pontoInicialMovimento.x;
			        int dy = e.getY() - pontoInicialMovimento.y;

			        segmentoSelecionado.translacao(dx, dy);

			        pontoInicialMovimento = e.getPoint();
			        repaint();
			        return;
			    }

			    if (recortando && pontoInicialMovimento != null) {
			        int x = Math.min(pontoInicialMovimento.x, e.getX());
			        int y = Math.min(pontoInicialMovimento.y, e.getY());
			        int w = Math.abs(pontoInicialMovimento.x - e.getX());
			        int h = Math.abs(pontoInicialMovimento.y - e.getY());
			        janelaRecorte = new Rectangle(x, y, w, h);
			        repaint();
			    }
			}
		});
		add(painelDesenho, BorderLayout.CENTER);

		JPanel painelBotoes = new JPanel();
		botaoCorAzul = criarBotaoCor(Color.BLUE);
		botaoCorVermelho = criarBotaoCor(Color.RED);
		botaoCorVerde = criarBotaoCor(Color.GREEN);
		botaoCorAmarela = criarBotaoCor(Color.YELLOW);
		botaoRecortar = new JButton("Recortar");
		botaoApagar = new JButton("Apagar");
		botaoMover = new JButton("Mover");
		
		botaoTranslacao = new JButton("Translação 10px");
		botaoRotacao = new JButton("Rotação 90°");
		botaoEscala = new JButton("Escala ao Dobro");

		botaoMover.addActionListener(e -> {
		    modoMover = !modoMover;
		    botaoMover.setText(modoMover ? "Mover: ON" : "Mover");
		});
		botaoTranslacao.addActionListener(e -> {
		    for (Segmento s : segmentos) {
		        s.translacao(10, 10);
		    }
		    repaint();
		});

		botaoRotacao.addActionListener(e -> {
		    for (Segmento s : segmentos) {
		        s.rotacao(90); // 90 graus
		    }
		    repaint();
		});

		botaoEscala.addActionListener(e -> {
		    for (Segmento s : segmentos) {
		        s.escala(2, 2); // dobra o tamanho
		    }
		    repaint();
		});
		botaoRecortar.addActionListener(e -> {
		    recortando = !recortando;
		});
		
		botaoApagar.addActionListener(e -> apagarSegmentoSelecionado());

		painelBotoes.add(botaoCorAzul);
		painelBotoes.add(botaoCorVermelho);
		painelBotoes.add(botaoCorVerde);
		painelBotoes.add(botaoCorAmarela);
		painelBotoes.add(botaoRecortar);
		painelBotoes.add(botaoApagar);
		painelBotoes.add(botaoMover);
		painelBotoes.add(botaoTranslacao);
		painelBotoes.add(botaoRotacao);
		painelBotoes.add(botaoEscala);
		add(painelBotoes, BorderLayout.SOUTH);
		
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private JButton criarBotaoCor(Color cor) {
		JButton botao = new JButton();
		botao.setPreferredSize(new Dimension(30, 30));
		botao.setBackground(cor);
		botao.setOpaque(true);
		botao.setBorderPainted(false);
		botao.addActionListener(e -> corAtual = cor);
		return botao;
	}

	private void desenharSegmentos(Graphics g) {
		for (Segmento segmento : segmentos) {
			segmento.desenhar(g);
		}
	}
	
	private void darOutlineNoSegmento(Graphics g) {
	    if (segmentoSelecionado != null) {
	        Graphics2D g2d = (Graphics2D) g.create();

	        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));

	        g2d.setColor(Color.BLACK);
	        g2d.setStroke(new BasicStroke(5));
	        g2d.drawLine(
	            segmentoSelecionado.pontoInicial.x,
	            segmentoSelecionado.pontoInicial.y,
	            segmentoSelecionado.pontoFinal.x,
	            segmentoSelecionado.pontoFinal.y
	        );

	        g2d.dispose(); 
	    }
	}
	
	private void desenharJanelaRecorte(Graphics g) {
	    if (janelaRecorte != null) {
	        Graphics2D g2d = (Graphics2D) g;
	        g2d.setColor(Color.BLACK);
	        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
	        g2d.drawRect(janelaRecorte.x, janelaRecorte.y, janelaRecorte.width, janelaRecorte.height);
	    }
	}

	private Segmento selecionarSegmento(Point ponto) {
		return segmentos.stream()
				.filter(segmento -> segmento.contemPoint(ponto))
				.findFirst().orElse(null);
	}

	private void apagarSegmentoSelecionado() {
		if (segmentoSelecionado != null) {
			segmentos.remove(segmentoSelecionado);
			segmentoSelecionado = null;
			repaint();
		}
	}

	public static void main(String[] args) {
		new EditorSegmentos();
	}
}