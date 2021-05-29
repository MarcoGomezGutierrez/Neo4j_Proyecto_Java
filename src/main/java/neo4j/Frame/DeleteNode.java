package neo4j.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import TextPrompt.TextPrompt;
import neo4j.Cerebro.Function;

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;

public class DeleteNode extends JFrame {

	private static final long serialVersionUID = -353393766749830531L;
	
	private JPanel contentPane;
	private JTextField text_node;
	@SuppressWarnings("unused")
	private TextPrompt placeholder;
	private JLabel lbl_invalido = new JLabel();

	public DeleteNode() {
		
		try {UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {}
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 500, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		text_node = new JTextField();
		text_node.setFont(new Font("Tahoma", Font.PLAIN, 20));
		text_node.setBounds(150, 100, 200, 70);
		placeholder = new TextPrompt("Nodo a eliminar...", text_node);
		contentPane.add(text_node);
		
		JButton btn_delete = new JButton("Delete");
		btn_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (text_node.getText().length() != 0) {
					if (comprobarNodoImportante(text_node.getText()) && comprobarRelacionImportante(text_node.getText())) {
						if (Function.deleteNode(text_node.getText())) {
							text_node.setText("");                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
						} else {
							JOptionPane.showMessageDialog(null, "No tienes el Rol adecuado para eliminar nodos o relaciones");
						}
					} else {
						lbl_invalido.setText("Nodo Invalido");
					}
				}
			}

			private boolean comprobarRelacionImportante(String text) {
				if (text == "Cerebro") return false;
				else if (text == "Sentido") return false;
				else if (text == "Organo") return false;
				else if (text == "Olor") return false;
				else if (text == "Oir") return false;
				else if (text == "Tocar") return false;
				else if (text == "Sabor") return false;
				else if (text == "Ver") return false;
				else return true;
			}

			private boolean comprobarNodoImportante(String text) {
				if (Function.matchAllOrgano().contains(text)) return false;
				else if (Function.matchAllSabor().contains(text)) return false;
				else if (Function.matchAllOlor().contains(text)) return false;
				else if (Function.matchAllOir().contains(text)) return false;
				else if (Function.matchAllVer().contains(text)) return false;
				else if (Function.matchAllTocar().contains(text)) return false;
				else if (Function.matchAllSentido().contains(text)) return false;
				else return true;
			}
		});
		btn_delete.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_delete.setBounds(200, 200, 100, 50);
		contentPane.add(btn_delete);
		lbl_invalido.setForeground(Color.RED);
		lbl_invalido.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		lbl_invalido.setBounds(357, 110, 127, 50);
		contentPane.add(lbl_invalido);
	}
}
