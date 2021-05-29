package neo4j.Frame;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import neo4j.Cerebro.Conexion;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

public class Registrarse extends JPanel {
	
	private static final long serialVersionUID = -8822937469087958467L;
	
	private JTextField text_user;
	private JTextField text_password;
	private JRadioButton rdbtn_publisher = new JRadioButton("Publisher");
	private JRadioButton rdbtn_editor = new JRadioButton("Editor");
	private JRadioButton rdbtn_reader = new JRadioButton("Reader");
	private JRadioButton rdbtn_architect = new JRadioButton("Architect");
	private JButton btn_atras = new JButton("Atras");
	private final JTextField text_puerto = new JTextField("bolt://localhost:7687");
	
	public Registrarse() {
		try {UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {}
		
		setLayout(null);
		
		text_user = new JTextField();
		text_user.setBounds(149, 87, 143, 34);
		add(text_user);
		text_user.setColumns(10);
		
		text_password = new JTextField();
		text_password.setColumns(10);
		text_password.setBounds(149, 143, 143, 34);
		add(text_password);
		
		JButton btn_registrarse = new JButton("Registrarse");
		btn_registrarse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (text_user.getText().length() != 0 && text_password.getText().length() != 0) {
					Conexion conexion = new Conexion(text_puerto.getText(), "neo4j", "123");
					registrarUsuario(text_user.getText(), text_password.getText());
					try { conexion.close(); } catch (Exception e1) {}
				}
			}

			private void registrarUsuario(String user, String password) {
				List<String> roles = new ArrayList<String>();
				if (rdbtn_publisher.isSelected()) roles.add("publisher");
				if (rdbtn_editor.isSelected()) roles.add("editor");
				if (rdbtn_reader.isSelected()) roles.add("reader");
				if (rdbtn_architect.isSelected()) roles.add("architect");
				Conexion.createUserRole(user, password, roles);
			}
		});
		btn_registrarse.setBounds(179, 203, 89, 23);
		add(btn_registrarse);
		
		JLabel lbl_roles = new JLabel("Roles");
		lbl_roles.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lbl_roles.setBounds(44, 73, 57, 14);
		add(lbl_roles);
		
		rdbtn_publisher.setBounds(30, 98, 71, 23);
		add(rdbtn_publisher);
		
		rdbtn_editor.setBounds(30, 124, 71, 23);
		add(rdbtn_editor);
		
		rdbtn_reader.setBounds(30, 149, 71, 23);
		add(rdbtn_reader);
		
		rdbtn_architect.setBounds(30, 175, 71, 23);
		add(rdbtn_architect);
		btn_atras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Roles.atras();
			}
		});
		
		btn_atras.setBounds(24, 254, 89, 23);
		add(btn_atras);
		
		text_puerto.setBounds(24, 223, 119, 20);
		text_puerto.setColumns(10);
		add(text_puerto);
		
		JButton btn_info = new JButton("Informaci\u00F3n sobre Roles");
		btn_info.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Admin -> Editar, Leer, Crear Roles, Reset Database\n"
						+ "Publisher -> Editar, Leer\n"
						+ "Editor -> Leer y Modificar, No Crear\n"
						+ "Reader -> Leer\n"
						+ "Architect -> Editar y Leer");
			}
		});
		btn_info.setBounds(147, 254, 178, 23);
		add(btn_info);

	}
}
