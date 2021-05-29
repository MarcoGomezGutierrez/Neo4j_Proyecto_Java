package neo4j.Graph;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import neo4j.Cerebro.Intelligence;
import neo4j.Cerebro.NodeLists;

public class GraphNeo4j extends Thread {
	
	@Override
	/**
	 * Método del Thread para ejecutar el Hilo
	 */
	public void run() {
		Graph graph = createGraph();
		addNewNodes(graph);
		System.setProperty("org.graphstream.ui", "swing");
		graph.setStrict(false);
		graph.setAutoCreate(true);
		/**
		 * Método para que me sustituya el window closer de GraphStream a otro.
		 * De esta forma evito que me cierre la aplicación original.
		 * Y al hacer display me lanza el grafo,
		 */
		Viewer viewer = graph.display();
		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
	}

	/**
	 * Crea los nodos que se le añade a la máquina para que aprenda
	 * @param graph
	 */
	private void addNewNodes(Graph graph) {
		/**
		 * Recorro la lista y creo los nodos
		 */
		for (NodeLists node : Intelligence.completeGraph(Intelligence.newNodesGraph())) {
			graph.addNode(node.getNode()).setAttribute("ui.style", "fill-color: #A6C3FC; text-alignment: at-right; text-size: 12;");
			/**
			 * Luego me recorro la lista del nodo y creo las aristas
			 */
			for (String otherNode : node.getList()) {
				graph.addEdge(node.getNode() + otherNode, node.getNode(), otherNode).setAttribute("length", node.getNode());
			}
		}
		/**
		 * Lanzo dos Hilos para que me cambien los atributos a la vez, 
		 * asi no tengo que recorrer los dos uno detras de otro.
		 * Porque no hay el mismo numero de Nodos que Aristas.
		 */
		Thread nodos = new Thread(() -> {
			for (Node node : graph) {
				node.setAttribute("ui.label", node.getId());
			}
		});
		Thread edges = new Thread(() -> {
			for (int i = 0; i < graph.getEdgeCount(); i++) {
				graph.getEdge(i).setAttribute("ui.label", graph.getEdge(i).getAttribute("length"));
			}
		});
		/**
		 * Comienzo de los hilos
		 */
		nodos.start();
		edges.start();
		/**
		 * Espero a que terminen los hilos, si no me puede 
		 * llegar a pintar el grafo antes de que terminen los hilos
		 */
		try {
			nodos.join();
			edges.join();
		} catch (InterruptedException e) {}
		
	}
	/**
	 * Creo el grafo default
	 * @return
	 */
	private Graph createGraph() {
		Graph graph = new SingleGraph("Grafo");
		createNodes(graph);
		crearRelaciones(graph);
		return graph;
	}

	/**
	 * Crea las relaciones importantes del grafo default
	 * @param graph
	 */
	private void crearRelaciones(Graph graph) {
		/**
		 * 							Cerebro-->Sentido
		 */
		graph.addEdge("CerebroGusto", "Cerebro", "Gusto").setAttribute("length", "Sentido");
		graph.addEdge("CerebroVista", "Cerebro", "Vista").setAttribute("length", "Sentido");
		graph.addEdge("CerebroOido1", "Cerebro", "Oido1").setAttribute("length", "Sentido");
		graph.addEdge("CerebroOlfato", "Cerebro", "Olfato").setAttribute("length", "Sentido");
		graph.addEdge("CerebroTacto", "Cerebro", "Tacto").setAttribute("length", "Sentido");
		
		/**
		 * 							Sentido-->Organo
		 */
		graph.addEdge("GustoLengua", "Gusto", "Lengua").setAttribute("length", "Organo");
		graph.addEdge("VistaOjo", "Vista", "Ojo").setAttribute("length", "Organo");
		graph.addEdge("Oido1Oido2", "Oido1", "Oido2").setAttribute("length", "Organo");
		graph.addEdge("OlfatoNariz", "Olfato", "Nariz").setAttribute("length", "Organo");
		graph.addEdge("TactoPiel", "Tacto", "Piel").setAttribute("length", "Organo");
		
		
		/**
		 * 							Lengua-->Atributo
		 */
		graph.addEdge("LenguaDulce", "Lengua", "Dulce").setAttribute("length", "Sabor");
		graph.addEdge("LenguaAmargo", "Lengua", "Amargo").setAttribute("length", "Sabor");
		graph.addEdge("LenguaAcido", "Lengua", "Acido").setAttribute("length", "Sabor");
		graph.addEdge("LenguaSalado", "Lengua", "Salado").setAttribute("length", "Sabor");
		
		/**
		 * 							Ojo-->Atributo
		 */
		graph.addEdge("OjoColor", "Ojo", "Color").setAttribute("length", "Ver");
		graph.addEdge("OjoTamaño", "Ojo", "Tamaño").setAttribute("length", "Ver");
		graph.addEdge("OjoForma Visual", "Ojo", "Forma Visual").setAttribute("length", "Ver");
		
		
		/**
		 * 							Oido-->Atributo
		 */
		graph.addEdge("Oido2Intensidad", "Oido2", "Intensidad").setAttribute("length", "Oir");
		graph.addEdge("Oido2Distancia", "Oido2", "Distancia").setAttribute("length", "Oir");
		
		/**
		 * 							Nariz-->Atributo
		 */
		graph.addEdge("NarizAgradable", "Nariz", "Agradable").setAttribute("length", "Olor");
		graph.addEdge("NarizDesagradable", "Nariz", "Desagradable").setAttribute("length", "Olor");
		
		/**
		 * 							Piel-->Atributo
		 */
		graph.addEdge("PielConsistencia", "Piel", "Consistencia").setAttribute("length", "Tocar");
		graph.addEdge("PielTextura", "Piel", "Textura").setAttribute("length", "Tocar");
		graph.addEdge("PielForma Fisica", "Piel", "Forma Fisica").setAttribute("length", "Tocar");
		graph.addEdge("PielTemperatura", "Piel", "Temperatura").setAttribute("length", "Tocar");
		
	}
	
	/**
	 * Creo los nodos que nunca cambian
	 * @param graph
	 */
	private void createNodes(Graph graph) {
		//Padre
		graph.addNode("Cerebro").setAttribute("ui.style", "fill-color: #FCF865; text-alignment: at-right; text-size: 12;");
		//Sentidos
		graph.addNode("Gusto").setAttribute("ui.style", "fill-color: #7DF529;  text-alignment: at-right; text-size: 12;");
		graph.addNode("Vista").setAttribute("ui.style", "fill-color: #7DF529; text-alignment: at-right; text-size: 12;");
		graph.addNode("Oido1").setAttribute("ui.style", "fill-color: #7DF529; text-alignment: at-right; text-size: 12;");
		graph.addNode("Olfato").setAttribute("ui.style", "fill-color: #7DF529; text-alignment: at-right; text-size: 12;");
		graph.addNode("Tacto").setAttribute("ui.style", "fill-color: #7DF529; text-alignment: at-right; text-size: 12;");
		//Oganos
		graph.addNode("Lengua").setAttribute("ui.style", "fill-color: #29F5F5; text-alignment: at-right; text-size: 12;");
		graph.addNode("Ojo").setAttribute("ui.style", "fill-color: #29F5F5; text-alignment: at-right; text-size: 12;");
		graph.addNode("Nariz").setAttribute("ui.style", "fill-color: #29F5F5; text-alignment: at-right; text-size: 12;");
		graph.addNode("Piel").setAttribute("ui.style", "fill-color: #29F5F5; text-alignment: at-right; text-size: 12;");
		graph.addNode("Oido2").setAttribute("ui.style", "fill-color: #29F5F5; text-alignment: at-right; text-size: 12;");
		
		//Atributos de Organos
		
		//Lengua
		graph.addNode("Dulce").setAttribute("ui.style", "fill-color: #F05DF2; text-alignment: at-right; text-size: 12;");
		graph.addNode("Amargo").setAttribute("ui.style", "fill-color: #F05DF2; text-alignment: at-right; text-size: 12;");
		graph.addNode("Acido").setAttribute("ui.style", "fill-color: #F05DF2; text-alignment: at-right; text-size: 12;");
		graph.addNode("Salado").setAttribute("ui.style", "fill-color: #F05DF2; text-alignment: at-right; text-size: 12;");
		
		//Ojo
		graph.addNode("Color").setAttribute("ui.style", "fill-color: #F05DF2; text-alignment: at-right; text-size: 12;");
		graph.addNode("Tamaño").setAttribute("ui.style", "fill-color: #F05DF2; text-alignment: at-right; text-size: 12;");
		graph.addNode("Forma Visual").setAttribute("ui.style", "fill-color: #F05DF2; text-alignment: at-right; text-size: 12;");
		
		//Nariz
		graph.addNode("Agradable").setAttribute("ui.style", "fill-color: #F05DF2; text-alignment: at-right; text-size: 12;");
		graph.addNode("Desagradable").setAttribute("ui.style", "fill-color: #F05DF2; text-alignment: at-right; text-size: 12;");
		
		//Piel
		graph.addNode("Consistencia").setAttribute("ui.style", "fill-color: #F05DF2; text-alignment: at-right; text-size: 12;");
		graph.addNode("Textura").setAttribute("ui.style", "fill-color: #F05DF2; text-alignment: at-right; text-size: 12;");
		graph.addNode("Forma Fisica").setAttribute("ui.style", "fill-color: #F05DF2; text-alignment: at-right; text-size: 12;");
		graph.addNode("Temperatura").setAttribute("ui.style", "fill-color: #F05DF2; text-alignment: at-right; text-size: 12;");
		
		//Oido
		graph.addNode("Intensidad").setAttribute("ui.style", "fill-color: #F05DF2; text-alignment: at-right; text-size: 12;");
		graph.addNode("Distancia").setAttribute("ui.style", "fill-color: #F05DF2; text-alignment: at-right; text-size: 12;");
	}
	
}
