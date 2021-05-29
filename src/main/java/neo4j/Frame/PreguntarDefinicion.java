package neo4j.Frame;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import neo4j.Cerebro.Intelligence;
import neo4j.Diccionario.Diccionario;
import python.voz.TextToVoice;
import javax.swing.JCheckBox;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PreguntarDefinicion extends JFrame {

	private static final long serialVersionUID = -8918674247804572340L;
	private JPanel contentPane;
	private JTextField text_palabra;
	private JTextField text_definicion;
	private Thread hilo;
	private Thread hiloDefinicion;
	private JTextArea textArea = new JTextArea();
	private JTextArea textArea_1 = new JTextArea();
	
	public PreguntarDefinicion() {
		try { UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel"); } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setContinuousLayout(true);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(splitPane_1);
		
		
		splitPane_1.setRightComponent(textArea);
		
		JSplitPane splitPane_3 = new JSplitPane();
		splitPane_3.setContinuousLayout(true);
		splitPane_3.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_1.setLeftComponent(splitPane_3);
		
		JPanel panel = new JPanel();
		splitPane_3.setLeftComponent(panel);
		
		JLabel lbl_1 = new JLabel("Preguntas Cerebro");
		lbl_1.setFont(new Font("Arial", Font.BOLD, 28));
		panel.add(lbl_1);
		
		JPanel panel_1 = new JPanel();
		splitPane_3.setRightComponent(panel_1);
		
		text_palabra = new JTextField();
		panel_1.add(text_palabra);
		text_palabra.setColumns(10);
		
		JCheckBox bx_vozPalabra = new JCheckBox("Voz");
		panel_1.add(bx_vozPalabra);
		
		JButton btn_preguntarPalabra = new JButton("Preguntar");
		btn_preguntarPalabra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (text_palabra.getText().length() != 0) {
					if (bx_vozPalabra.isSelected()) lanzarHiloVoz(text_palabra.getText());
					else lanzarHiloSinVoz(text_palabra.getText());
				}
			}
			
			private void lanzarHiloVoz(String text) {
				hilo = new Hilo(text, textArea, true);
				hilo.start();
			}
			
			private void lanzarHiloSinVoz(String text) {
				hilo = new Hilo(text, textArea, false);
				hilo.start();
			}
		});
		panel_1.add(btn_preguntarPalabra);
		
		JSplitPane splitPane_2 = new JSplitPane();
		splitPane_2.setContinuousLayout(true);
		splitPane_2.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_2);
		
		
		splitPane_2.setRightComponent(textArea_1);
		
		JSplitPane splitPane_4 = new JSplitPane();
		splitPane_4.setContinuousLayout(true);
		splitPane_4.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_2.setLeftComponent(splitPane_4);
		
		JPanel panel_2 = new JPanel();
		splitPane_4.setLeftComponent(panel_2);
		
		JLabel lbl_2 = new JLabel("Preguntas Diccionario");
		lbl_2.setFont(new Font("Arial", Font.BOLD, 28));
		panel_2.add(lbl_2);
		
		JPanel panel_3 = new JPanel();
		splitPane_4.setRightComponent(panel_3);
		
		text_definicion = new JTextField();
		panel_3.add(text_definicion);
		text_definicion.setColumns(10);
		
		JCheckBox bx_vozDiccionario = new JCheckBox("Voz");
		panel_3.add(bx_vozDiccionario);
		
		JButton btn_preguntarDefinicion = new JButton("Preguntar");
		btn_preguntarDefinicion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (text_definicion.getText().length() != 0) {
					if (bx_vozDiccionario.isSelected()) lanzarHiloVoz(text_definicion.getText().toLowerCase());
					else lanzarHiloSinVoz(text_definicion.getText().toLowerCase());
				}
			}
			private void lanzarHiloVoz(String text) {
				hiloDefinicion = new HiloDefinicion(text, textArea_1, true);
				hiloDefinicion.start();
			}
			
			private void lanzarHiloSinVoz(String text) {
				hiloDefinicion = new HiloDefinicion(text, textArea_1, false);
				hiloDefinicion.start();
			}
		});
		panel_3.add(btn_preguntarDefinicion);
	}
	
	
	class Hilo extends Thread {
		
		private String text;
		private boolean check;
		private JTextArea textArea = new JTextArea();
		private TextToVoice voz = new TextToVoice();
		
		public Hilo(String text, JTextArea textArea2, boolean check) {
			this.text = text;
			this.textArea = textArea2;
			this.check = check;
		}
		
		@Override
		public void run() {
			String dialogo = Intelligence.questionIfKnowElem(text);
			if (check) voz.hablar(dialogo);
			textArea.setText(dialogo);
		}
		
	}
	
	class HiloDefinicion extends Thread {
		
		private String text;
		private boolean check;
		private JTextArea textArea = new JTextArea();
		private TextToVoice voz = new TextToVoice();
		
		public HiloDefinicion(String text, JTextArea textArea2, boolean check) {
			this.text = text;
			this.textArea = textArea2;
			this.check = check;
		}
		
		@Override
		public void run() {
			String dialogo = Diccionario.buscarDefinicion(text);
			if (check) voz.hablar(dialogo);
			textArea.setText(dialogo);
		}
		
	}
}



