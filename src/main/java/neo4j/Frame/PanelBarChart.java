package neo4j.Frame;


import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import neo4j.Business.Intelligence.BusinessIntelligence;
import neo4j.Business.Intelligence.NodeRelationship;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PanelBarChart extends JFrame {

	
	private static final long serialVersionUID = -3120908464590087030L;
	private JPanel contentPane;
	private JPanel panel_central = new JPanel();
	private int num = 0;
	private JRadioButton rdbtn_3D = new JRadioButton("3D");
	private JRadioButton rdbtn_2D = new JRadioButton("2D");
	private final JRadioButton rdbtn_nodos = new JRadioButton("Gr\u00E1fica Nodos");
	private final JRadioButton rdbtn_palabras = new JRadioButton("Gr\u00E1fica Palabras");

	public PanelBarChart() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_sur = new JPanel();
		contentPane.add(panel_sur, BorderLayout.SOUTH);
		
		/**
		 * Es para que uno de los RadioBotons este seleccionado
		 */
		rdbtn_3D.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtn_2D.isSelected()) rdbtn_2D.setSelected(false);
				else rdbtn_3D.setSelected(true);
			}
		});
		
		panel_sur.add(rdbtn_3D);
		
		JButton btn_grafico = new JButton("Graficar");
		
		/**
		 * Boton que pinta las gráficas leyendo los datos de Excel
		 */
		btn_grafico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BusinessIntelligence.crearExcel();
				
				DefaultPieDataset dtsc = new DefaultPieDataset();
				if (rdbtn_nodos.isSelected()) {
					List<NodeRelationship> list = BusinessIntelligence.leerTabla();
					
					for (int i = 0; i < list.size(); i++) dtsc.setValue(list.get(i).getNode(), list.get(i).getRelationship());
				} else {
					List<NodeRelationship> list = BusinessIntelligence.leerTablaDiccionario();
					
					for (int i = 0; i < list.size(); i++) dtsc.setValue(list.get(i).getNode(), list.get(i).getRelationship());
				}
				
				if (rdbtn_3D.isSelected()) circular3D(dtsc);
				else circular2D(dtsc);
			}
			
			/**
			 * Método para pintar un gráfico circular en 3D
			 * @param dtsc
			 */
			private void circular3D(DefaultPieDataset dtsc) {
				JFreeChart ch = ChartFactory.createPieChart3D("Grafica circular 3D", dtsc, true, true, false);
				ChartPanel cp = new ChartPanel(ch);
				cp.setVisible(true);
				if (num > 0) panel_central.remove(0);
				panel_central.add(cp);
				cp.setBounds(panel_central.getBounds());
				num++;
			}
			
			/**
			 * Método para pintar un gráfico circular en 2D
			 * @param dtsc
			 */
			private void circular2D(DefaultPieDataset dtsc) {
				JFreeChart ch = ChartFactory.createPieChart("Grafica circular 2D", dtsc, true, true, false);
				ChartPanel cp = new ChartPanel(ch);
				cp.setVisible(true);
				if (num > 0) panel_central.remove(0);
				panel_central.add(cp);
				cp.setBounds(panel_central.getBounds());
				num++;
			}

		});
		
		/**
		 * Es para que uno de los RadioBotons este seleccionado
		 */
		rdbtn_2D.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtn_3D.isSelected()) rdbtn_3D.setSelected(false);
				else rdbtn_2D.setSelected(true);
			}
		});
		
		
		rdbtn_2D.setSelected(true);
		panel_sur.add(rdbtn_2D);
		rdbtn_nodos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtn_palabras.isSelected()) rdbtn_palabras.setSelected(false);
				else rdbtn_nodos.setSelected(true);
			}
		});
		rdbtn_nodos.setSelected(true);
		
		panel_sur.add(rdbtn_nodos);
		rdbtn_palabras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtn_nodos.isSelected()) rdbtn_nodos.setSelected(false);
				else rdbtn_palabras.setSelected(true);
			}
		});
		
		panel_sur.add(rdbtn_palabras);
		panel_sur.add(btn_grafico);
		
		
		contentPane.add(panel_central, BorderLayout.CENTER);
		
	}

}
