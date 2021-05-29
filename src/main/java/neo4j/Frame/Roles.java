package neo4j.Frame;

import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import TextPrompt.TextPrompt;
import neo4j.Cerebro.Conexion;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class Roles extends JFrame {

	
	private static final long serialVersionUID = 2455586042840714372L;
	
	private static JPanel contentPane;
	private static JPanel panel_central;
	private static Registrarse panelRegistrarse = new Registrarse();
	private JTextField text_usuario;
	private JTextField text_contraseña;
	@SuppressWarnings("unused")
	private TextPrompt placeholder;
	private JTextField text_port = new JTextField("bolt://localhost:7687");
	private JPanel panel;
	
	public Roles() {
		try {UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(690, 320, 500, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout());
		contentPane.setBackground(Color.lightGray);
		
		panel_central = new JPanel();
		panel_central.setLayout(null);
		panelRegistrarse.setVisible(false);
		contentPane.add(panel_central);
		contentPane.add(panelRegistrarse);
		
		text_usuario = new JTextField();
		text_usuario.setBounds(150, 70, 200, 50);
		placeholder = new TextPrompt("Usuario...", text_usuario);
		panel_central.add(text_usuario);
		
		text_contraseña = new JTextField();
		text_contraseña.setBounds(150, 150, 200, 50);
		placeholder = new TextPrompt("Contraseña...", text_contraseña);
		panel_central.add(text_contraseña);
		
		JButton btn_rol = new JButton("Usar Rol");
		btn_rol.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if (text_usuario.getText().length() != 0 && text_contraseña.getText().length() != 0) {
					Conexion conexion = new Conexion(text_port.getText(), text_usuario.getText(), text_contraseña.getText());
					if(conexion.comprobarCredentials()) {
						Aplication.setFrameVisible();
					} else {
						Thread thread = new Thread(() -> {
							Color color = btn_rol.getBackground();
							btn_rol.setBackground(Color.RED);
							JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
							try { Thread.sleep(1000); } catch (InterruptedException e1) {}
							btn_rol.setBackground(color);
						});
						text_usuario.setText("");
						text_contraseña.setText("");
						thread.start();
						try {
							conexion.close();
						} catch (Exception e2) {}
						
					}
				}
			}
		});
		btn_rol.setBounds(200, 230, 100, 50);
		panel_central.add(btn_rol);
		
		
		text_port.setBounds(10, 330, 158, 20);
		panel_central.add(text_port);
		text_port.setColumns(10);
		placeholder = new TextPrompt("bolt://localhost:7687", text_port);
		
		JButton btn_register = new JButton("Registrase");
		btn_register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelRegistrarse.setVisible(true);
				panel_central.setVisible(false);
			}
		});
		btn_register.setBounds(206, 307, 89, 21);
		panel_central.add(btn_register);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 10, 10);
		panel_central.add(panel);
		
		cerrarPestaña();
	}
	
	public static void atras() {
		panelRegistrarse.setVisible(false);
		panel_central.setVisible(true);
	}
	
	/**
	 * Método cerrar pestaña
	 */
	private void cerrarPestaña() {
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent e) {
				confirmarSalida();
			}
		});
		this.setVisible(true);
	}
	
	/**
	 * Confirmar salida
	 */
	private void confirmarSalida() {
		int valor = JOptionPane.showConfirmDialog(this, "¿Esta seguro de cerrar esta aplicación?", "Advertencia", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (valor == JOptionPane.YES_OPTION) {
			JOptionPane.showMessageDialog(null, "Gracias, vuelva pronto");
			System.exit(0);
		}
	}
}
