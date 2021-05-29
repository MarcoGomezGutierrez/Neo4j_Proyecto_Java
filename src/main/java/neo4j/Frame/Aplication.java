package neo4j.Frame;

import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UnsupportedLookAndFeelException;
import neo4j.Business.Intelligence.BusinessIntelligence;
import neo4j.Graph.GraphDiccionario;
import neo4j.Graph.GraphNeo4j;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

public class Aplication {

	
	private static JFrame frame;
	private static Roles role = new Roles();
	private IntelligenceLearn panelLearn = new IntelligenceLearn();
	private JButton btn_preguntar = new JButton("Preguntar");
	private final JButton btn_graphCerebro = new JButton("Grafo Cerebro");
	private final JButton btn_graphDiccionario = new JButton("Grafo Diccionario");
	private final JButton btn_excel = new JButton("Mostrar Excel");
	private final JButton btn_grafica = new JButton("Ver Gr\u00E1fica");
	private final JButton btn_delete = new JButton("Eliminar Nodo");
	private final JButton btn_ayuda = new JButton("Ayuda");
	private final JLabel lbl_atras = new JLabel("");
	
	
	/**
	 * Launch the application.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("static-access")
			public void run() {
				try {
					Aplication window = new Aplication();
					window.role.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public Aplication() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	private void initialize() {
		
		
		frame = new JFrame();
		frame.setBounds(400, 100, 1000, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel_sur = new JPanel();
		frame.getContentPane().add(panel_sur, BorderLayout.SOUTH);
		
		/**
		 * Botón que me cambia al panel IntelligenceQuestion
		 */
		btn_preguntar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PreguntarDefinicion framePreguntar = new PreguntarDefinicion();
				framePreguntar.setVisible(true);
				framePreguntar.setDefaultCloseOperation(1);
			}
		});
		
		final ImageIcon imagen_atras = new ImageIcon("C:\\Users\\marco\\eclipse-workspace\\Neo4jProyectoFinal\\src\\main\\java\\Imagenes\\atras.png");
		lbl_atras.setIcon(new ImageIcon(imagen_atras.getImage().getScaledInstance(30, 20, Image.SCALE_DEFAULT)));
		lbl_atras.setSize(20, 20);
		lbl_atras.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Aplication.frame.setVisible(false);
				Aplication.role.setVisible(true);
			}
		});
		
		panel_sur.add(lbl_atras);
		panel_sur.add(btn_preguntar);
		
		/**
		 * Botón que me reconstruye la Base de Datos en un Hilo pero dibujada en Java
		 */
		btn_graphCerebro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GraphNeo4j thread = new GraphNeo4j();
				thread.start();
			}
		});
		panel_sur.add(btn_graphCerebro);
		
		btn_graphDiccionario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GraphDiccionario thread = new GraphDiccionario();
				thread.start();
			}
		});
		panel_sur.add(btn_graphDiccionario);
		
		/**
		 * Botón que me Crea/Actualiza el Excel y luego me lo muestra
		 */
		btn_excel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BusinessIntelligence.crearExcel();
				BusinessIntelligence.mostrar();
			}
		});
		panel_sur.add(btn_excel);
		
		/**
		 * Botón que me abre un JFrame y me gráfica la Tabla Nodos Nuevos con Nº de Relaciones
		 */
		btn_grafica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							PanelBarChart frame = new PanelBarChart();
							frame.setVisible(true);
							frame.setDefaultCloseOperation(1);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		panel_sur.add(btn_grafica);
		
		/**
		 * Botón que me borra los nuevos nodos solo puede el Admin
		 */
		btn_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						DeleteNode delete = new DeleteNode();
						delete.setVisible(true);
					}
				});
			}
		});
		panel_sur.add(btn_delete);
		
		btn_ayuda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							Ayuda ayuda = new Ayuda();
							ayuda.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		panel_sur.add(btn_ayuda);
		
		frame.getContentPane().add(panelLearn, BorderLayout.CENTER);
		
		Aplication.cerrarPestaña();
	}
	
	/**
	 * Método para cambiar del Panel Role al Frame(aplicación original)
	 */
	public static void setFrameVisible() {
		Aplication.frame.setVisible(true);
		Aplication.role.setVisible(false);
	}
	
	/**
	 * Método cerrar pestaña
	 */
	private static void cerrarPestaña() {
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				confirmarSalida();
			}
		});
	}
	
	/**
	 * Confirmar salida
	 */
	private static void confirmarSalida() {
		int valor = JOptionPane.showConfirmDialog(frame, "¿Esta seguro de cerrar esta aplicación?", "Advertencia", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (valor == JOptionPane.YES_OPTION) {
			JOptionPane.showMessageDialog(null, "Gracias, vuelva pronto");
			System.exit(0);
		}
	}

}
