package neo4j.Frame;

import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import neo4j.Cerebro.Intelligence;
import neo4j.Diccionario.Diccionario;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JTable;


/**
 * Este JPanel me inserta en una tabla todos los atributos que quiero enseñarle a la maquina.
 * 		-Sí esta todo rellenado. Lanza un Hilo a la par que el programa principal me borra el contenido 
 * 		de la tabla. El otro hilo que lanzo me ejecuta la algoritmia e inserta el contenido en la tabla.
 * 		-No esta todo rellenad. No hace nada. Directamente no se ejecuta código.
 * @author marco
 *
 */
public class IntelligenceLearn extends JPanel {

	private static final long serialVersionUID = -2804703922701517200L;

	private static int filas_table = 0;

	private static JTextField text_learn;
	private JComboBox<String> comboBox_gusto = new JComboBox<String>();
	private JComboBox<String> comboBox_vista = new JComboBox<String>();
	private JComboBox<String> comboBox_tacto = new JComboBox<String>();
	private JComboBox<String> comboBox_oido = new JComboBox<String>();
	private JComboBox<String> comboBox_olfato = new JComboBox<String>();
	private final JButton btn_insertar = new JButton("Insertar");
	private DefaultTableModel model;
	private JScrollPane scrollPane;
	private final JTable table = new JTable(0, 0);
	private final JTextField text_descripcion = new JTextField();
	private Map<Float, String> mapFloat = new HashMap<Float, String>();
	private List<Float> listIdentificador = new ArrayList<Float>();
	private List<String> listDocumentacion = new ArrayList<String>();

	private final JButton btn_eliminar = new JButton("Eliminar");
	private final JPanel panel = new JPanel();
	public final static JButton btn_learn = new JButton("Aprender");

	public IntelligenceLearn() {
		setBounds(100, 100, 1920, 1080);
		setLayout(new BorderLayout(0, 0));
		JPanel panel_learn = new JPanel();
		add(panel_learn, BorderLayout.NORTH);
		
		text_learn = new JTextField();
		panel_learn.add(text_learn);
		text_learn.setFont(new Font("Times New Roman", Font.BOLD, 30));
		text_learn.setHorizontalAlignment(SwingConstants.CENTER);
		text_learn.setPreferredSize(new Dimension(500, 50));

		JPanel panel_central = new JPanel();
		add(panel_central, BorderLayout.CENTER);
		panel_central.setLayout(new BorderLayout(0, 0));

		JPanel panel_sentidos = new JPanel();
		panel_central.add(panel_sentidos, BorderLayout.NORTH);

		/**
		 * Sentidos
		 */
		JComboBox<String> comboBox_sentidos = new JComboBox<String>();
		comboBox_sentidos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sincronizarComboBox(comboBox_sentidos.getSelectedItem().toString());
			}

			private void sincronizarComboBox(String string) {
				allInvisible();
				if (string == "Gusto") comboBox_gusto.setVisible(true);
				else if (string == "Vista") comboBox_vista.setVisible(true);
				else if (string == "Tacto") comboBox_tacto.setVisible(true);
				else if (string == "Oido") comboBox_oido.setVisible(true);
				else if (string == "Olfato") comboBox_olfato.setVisible(true);
			}

			private void allInvisible() {
				comboBox_gusto.setVisible(false);
				comboBox_vista.setVisible(false);
				comboBox_tacto.setVisible(false);
				comboBox_oido.setVisible(false);
				comboBox_olfato.setVisible(false);
			}
		});

		comboBox_sentidos.setModel(
				new DefaultComboBoxModel<String>(new String[] { "Gusto", "Vista", "Tacto", "Oido", "Olfato" }));
		comboBox_sentidos.setPreferredSize(new Dimension(100, 20));
		panel_sentidos.add(comboBox_sentidos);

		/***
		 * Propiedades de cada Sentido
		 */
		comboBox_gusto.setModel(new DefaultComboBoxModel<String>(new String[] { "Dulce", "Amargo", "Salado", "Acido" }));
		comboBox_gusto.setPreferredSize(new Dimension(100, 20));
		panel_sentidos.add(comboBox_gusto);

		comboBox_vista.setModel(new DefaultComboBoxModel<String>(new String[] { "Tamaño", "Forma Visual", "Color" }));
		comboBox_vista.setPreferredSize(new Dimension(100, 20));
		comboBox_vista.setVisible(false);
		panel_sentidos.add(comboBox_vista);

		comboBox_tacto.setModel(new DefaultComboBoxModel<String>(
				new String[] { "Forma Fisica", "Temperatura", "Consistencia", "Textura" }));
		comboBox_tacto.setPreferredSize(new Dimension(100, 20));
		comboBox_tacto.setVisible(false);
		panel_sentidos.add(comboBox_tacto);

		comboBox_oido.setModel(new DefaultComboBoxModel<String>(new String[] { "Intensidad", "Distancia" }));
		comboBox_oido.setPreferredSize(new Dimension(100, 20));
		comboBox_oido.setVisible(false);
		panel_sentidos.add(comboBox_oido);

		comboBox_olfato.setModel(new DefaultComboBoxModel<String>(new String[] { "Agradable", "Desagradable" }));
		comboBox_olfato.setPreferredSize(new Dimension(100, 20));
		comboBox_olfato.setVisible(false);
		panel_sentidos.add(comboBox_olfato);

		text_descripcion.setHorizontalAlignment(SwingConstants.LEFT);
		text_descripcion.setColumns(10);
		panel_sentidos.add(text_descripcion);
		
		
		/**
		 * Insertar en la tabla
		 */
		btn_insertar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if (text_descripcion.getText().length() != 0) insertarTabla(whoIsVisible(), getIdentificador());
			}
			
			/**
			 * Sumo 1 las filas añado a las listas y al mapa el resultado ya cifrado.
			 * @param whoIsVisible
			 * @param identificador
			 */
			
			private void insertarTabla(String whoIsVisible, float identificador) {
				model = (DefaultTableModel) table.getModel();
				
				boolean dontRepeat = true;
				for (int i = 0; i < model.getRowCount(); i++) {
					if (model.getValueAt(i, 0) == whoIsVisible) {
						dontRepeat = false;
						break;
					}
				}
				
				if (dontRepeat) {
					model.addRow(new Object[filas_table]);
					model.setValueAt(whoIsVisible, filas_table, 0);
					model.setValueAt(text_descripcion.getText(), filas_table, 1);
					
					mapFloat.put(identificador, whoIsVisible);
					listIdentificador.add(identificador);
					listDocumentacion.add(text_descripcion.getText());
					List<String> listDiccionario = Arrays.asList(text_descripcion.getText().split(" "));
					Thread hiloDiccionario = new Thread(() -> {
						Diccionario.insertarPalabras(listDiccionario);
					});
					hiloDiccionario.start();
					filas_table++;
				}
				text_descripcion.setText("");
			}
			
			/**
			 * Devolver el selected box
			 * @return
			 */
			private String whoIsVisible() {
				if (comboBox_gusto.isVisible())
					return comboBox_gusto.getSelectedItem().toString();
				else if (comboBox_vista.isVisible())
					return comboBox_vista.getSelectedItem().toString();
				else if (comboBox_tacto.isVisible())
					return comboBox_tacto.getSelectedItem().toString();
				else if (comboBox_oido.isVisible())
					return comboBox_oido.getSelectedItem().toString();
				else
					return comboBox_olfato.getSelectedItem().toString();
			}
			
			/**
			 * Devolver el identificador en concreto.
			 * @return
			 */
			private float getIdentificador() {
				if (comboBox_gusto.isVisible()) return obtenerGusto();
				else if (comboBox_vista.isVisible()) return obtenerVista();
				else if (comboBox_tacto.isVisible()) return obtenerTacto();
				else if (comboBox_oido.isVisible()) return obtenerOido();
				else return obtenerOlfato();
			}
			
			/**
			 * Algoritmia inventada por mi
			 * @return
			 */
			private float obtenerGusto() {
				if (comboBox_gusto.getSelectedIndex() == 0)
					return 1.1f;
				else if (comboBox_gusto.getSelectedIndex() == 1)
					return 1.2f;
				else if (comboBox_gusto.getSelectedIndex() == 2)
					return 1.3f;
				else
					return 1.4f;

			}

			private float obtenerVista() {
				if (comboBox_vista.getSelectedIndex() == 0)
					return 2.1f;
				else if (comboBox_vista.getSelectedIndex() == 1)
					return 2.2f;
				else
					return 2.3f;
			}

			private float obtenerTacto() {
				if (comboBox_tacto.getSelectedIndex() == 0)
					return 3.1f;
				else if (comboBox_tacto.getSelectedIndex() == 1)
					return 3.2f;
				else if (comboBox_tacto.getSelectedIndex() == 2)
					return 3.3f;
				else
					return 3.4f;
			}

			private float obtenerOido() {
				if (comboBox_oido.getSelectedIndex() == 0)
					return 4.1f;
				else
					return 4.2f;
			}

			private float obtenerOlfato() {
				if (comboBox_olfato.getSelectedIndex() == 0)
					return 5.1f;
				else
					return 5.2f;
			}
		});

		panel_sentidos.add(btn_insertar);
		
		/**
		 * Eliminar una fila de la tabla
		 */
		btn_eliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model = (DefaultTableModel) table.getModel();
				mapFloat.remove(listIdentificador.get(table.getSelectedRow()));
				listIdentificador.remove(table.getSelectedRow());
				listDocumentacion.remove(table.getSelectedRow());
				model.removeRow(table.getSelectedRow());
				filas_table--;
			}
		});

		panel_sentidos.add(btn_eliminar);

		model = (DefaultTableModel) table.getModel();
		model.addColumn("Atributo");
		model.addColumn("Descripción");
		scrollPane = new JScrollPane(table);

		panel_central.add(scrollPane);

		panel_central.add(panel, BorderLayout.SOUTH);
		
		/**							  ___          ___
		 * 						|	 |	     /\   |___| |\  |
		 * 						|    |__    /__\  |\    | \ | 
		 * 						|___ |____ /    \ | \   |  \|
		 * 
		 * Boton que lanza un hilo para ejecutar el resultado de insertar en la base de datos 
		 * mientras que el proceso Padre me borra el resultado de la tabla y que de sensacion de 
		 * rapidez de carga, ya que manejar todo el proceso de la algoritmía e insertar en la base de datos es
		 * muy lento y da sensación de lag en la aplicación. De esta forma engañamos de cirta manera al 
		 * usuario. Ya que nunca va a ser más rápido el que la máquina en insertar de nuevo. 
		 * 
		 */
		btn_learn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (text_learn.getText().length() != 0 && filas_table != 0) {
					Runnable runnable = //Le paso los parametros de la ejecucion del hilo
							new RunnableThread(mapFloat, listIdentificador, listDocumentacion, text_learn.getText());
					Thread thread = new Thread(runnable);
					thread.start(); //Empieza la ejecucion del hilo
					removeTable(); //Borro la tabla
					text_learn.setText("");
				}
			}

			private void removeTable() {
				int rows = table.getRowCount();
				for (int i = 0; rows > i; i++) {
					model.removeRow(0);
					filas_table--;
				}
			}

		});
		btn_learn.setFont(new Font("Times New Roman", Font.PLAIN, 20));

		panel.add(btn_learn);
	}
	
}


/**
 * Clase que implementa un Runnable
 * 
 * Creo la clase Runnable ya que Thread implementa la interfaz Runnable
 * Y luego creo un Objeto de tipo Thread y le asigno la clase de tipo Runnable 
 * par que sepa donde se tiene que ejecutar
 * @author marco
 *
 */
class RunnableThread implements Runnable {
	
	private Map<Float, String> mapFloat = new HashMap<Float, String>();
	private List<Float> listIdentificador = new ArrayList<Float>();
	private List<String> listDocumentacion = new ArrayList<String>();
	
	private List<String> listGusto = new ArrayList<String>();
	private List<String> listGustoDescripcion = new ArrayList<String>();
	private List<String> listVista = new ArrayList<String>();
	private List<String> listVistaDescripcion = new ArrayList<String>();
	private List<String> listTacto = new ArrayList<String>();
	private List<String> listTactoDescripcion = new ArrayList<String>();
	private List<String> listOido = new ArrayList<String>();
	private List<String> listOidoDescripcion = new ArrayList<String>();
	private List<String> listOlfato = new ArrayList<String>();
	private List<String> listOlfatoDescripcion = new ArrayList<String>();
	private String learn;
	
	/**
	 * Constructor que le paso los parametros que quiero que me almacene el hilo
	 * @param mapFloat
	 * @param listIdentificador
	 * @param listDocumentacion
	 * @param learn
	 */
	public RunnableThread (Map<Float, String> mapFloat, List<Float> listIdentificador, List<String> listDocumentacion, String learn) {
		this.mapFloat = mapFloat;
		this.listIdentificador = listIdentificador;
		this.listDocumentacion = listDocumentacion;
		this.learn = learn;
	}
	
	/**
	 * Método que al hacer thread.start() comienze el hilo aqui
	 */
	@Override
	public void run() {
		/**
		 * Me relleno las listas de su gusto con sus nodos y sus descripciones,
		 *  en el mismo orden para crearlos en el mismo orden
		 */
		for (int i = 0; i < listIdentificador.size(); i++) {
			if (Math.round(listIdentificador.get(i)) == 1)
				insertarGusto(mapFloat.get(listIdentificador.get(i)), listDocumentacion.get(i));
			else if (Math.round(listIdentificador.get(i)) == 2) 
				insertarVista(mapFloat.get(listIdentificador.get(i)), listDocumentacion.get(i));
			else if (Math.round(listIdentificador.get(i)) == 3) 
				insertarTacto(mapFloat.get(listIdentificador.get(i)), listDocumentacion.get(i));
			else if (Math.round(listIdentificador.get(i)) == 4) 
				insertarOido(mapFloat.get(listIdentificador.get(i)), listDocumentacion.get(i));
			else if (Math.round(listIdentificador.get(i)) == 5) 
				insertarOlfato(mapFloat.get(listIdentificador.get(i)), listDocumentacion.get(i));
		}
		
		/*Thread threadDiccionario = new Thread(() -> {
			List<String> listSplit = new ArrayList<String>();
			listSplit.addAll(listGustoDescripcion.subList(0, listGustoDescripcion.size()));
			listSplit.addAll(listVistaDescripcion.subList(0, listVistaDescripcion.size()));
			listSplit.addAll(listTactoDescripcion.subList(0, listTactoDescripcion.size()));
			listSplit.addAll(listOidoDescripcion.subList(0, listOidoDescripcion.size()));
			listSplit.addAll(listOlfatoDescripcion.subList(0, listOlfatoDescripcion.size()));
			System.out.println(listSplit.toString());
			List<String> listSplitResult = new ArrayList<String>();
			for (List<String> lista : listSplit
					.parallelStream()
					.map(elem -> elem.split(" "))
					.map(elem -> {
						return new ArrayList<String>(Arrays.asList(elem));
					})
					.collect(Collectors.toList())) {
				listSplitResult.addAll(lista);
			}
			Diccionario.insertarPalabras(listSplitResult);
		});
		threadDiccionario.start();*/
		/**
		 * Si no tiene el rol adecuado para crear nodos y relaciones, el método Learn
		 * de la clase Intelligence devuelve true si se ha logrado hacer todo.
		 * Si lanza una excepción la captura y lanza false, en ese caso pintaremos el boton en rojo
		 * para que vea el usuario que no tiene credenciales.
		 */
		if (!Intelligence.learn(learn, 
				listGusto, listGustoDescripcion,
				listVista, listVistaDescripcion, 
				listTacto, listTactoDescripcion, 
				listOido, listOidoDescripcion, 
				listOlfato, listOlfatoDescripcion )) {
			Color color = IntelligenceLearn.btn_learn.getBackground();
			IntelligenceLearn.btn_learn.setBackground(Color.red);
			JOptionPane.showMessageDialog(null, "No tienes el Rol adecuado para crear nodos o relaciones");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {}
			IntelligenceLearn.btn_learn.setBackground(color);
		}
		
		/*try {
			threadDiccionario.join();
		} catch (InterruptedException e) {}*/
		deleteListAndMap();
	}
	
	private void insertarGusto(String gusto, String descripcion) {
		listGusto.add(gusto);
		listGustoDescripcion.add(descripcion);
	}
	private void insertarVista(String vista, String descripcion) {
		listVista.add(vista);
		listVistaDescripcion.add(descripcion);
	}
	
	private void insertarTacto(String tacto, String descripcion) {
		listTacto.add(tacto);
		listTactoDescripcion.add(descripcion);
	}
	
	private void insertarOido(String oido, String descripcion) {
		listOido.add(oido);
		listOidoDescripcion.add(descripcion);
	}
	
	private void insertarOlfato(String olfato, String descripcion) {
		listOlfato.add(olfato);
		listOlfatoDescripcion.add(descripcion);
	}
	
	private void deleteListAndMap() {
		listIdentificador.clear();
		listDocumentacion.clear();
		mapFloat.clear();
	}
	
}

