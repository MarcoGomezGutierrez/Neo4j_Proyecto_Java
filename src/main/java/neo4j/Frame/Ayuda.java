package neo4j.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import neo4j.Cerebro.Function;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Ayuda extends JFrame {

	private static final long serialVersionUID = 5160939919988883801L;
	
	private JPanel contentPane;
	
	public Ayuda() {
		
		try {UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {}
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 500, 400);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbl_1 = new JLabel("<html>-Si tu Base de Datos esta vacia pulse el botón 'Reconstruir'"
				+ "<br>- Si se ha borrado un nodo importante por<br> error pulse boton 'Reconstruir' Base de Datos</html>");
		lbl_1.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbl_1.setBounds(30, 134, 432, 61);
		contentPane.add(lbl_1);
		
		JButton btn_reconstruir = new JButton("Reconstruir");
		/**
		 * Método que me borra toda la base de datos y me construye la Base de Datos Default
		 */
		btn_reconstruir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!Function.rebuildDB()) JOptionPane.showMessageDialog(null, "No tienes el Rol adecuado para resetear la Base de Datos");
			}
		});
		btn_reconstruir.setBounds(166, 206, 150, 40);
		contentPane.add(btn_reconstruir);
	}

}
