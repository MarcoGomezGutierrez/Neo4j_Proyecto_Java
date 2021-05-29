package neo4j.Graph;

import java.util.Map.Entry;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import neo4j.Diccionario.Diccionario;

public class GraphDiccionario extends Thread {
	
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
	 * Creo los nuevos nodos con sus relaciones,
	 * los nodos con las palabras que el usuario mete
	 * @param graph
	 */
	private void addNewNodes(Graph graph) {
		/**
		 * Recorro la lista y creo los nodos
		 */
		for (Entry<String, Integer> entry : Diccionario.contarPalabras().entrySet()) {
			graph.addNode(entry.getKey()).setAttribute("ui.style", "fill-color: #A6C3FC; text-alignment: at-right; text-size: 12;");
			graph.addEdge("Diccionario" + entry.getKey(), "Diccionario", entry.getKey()).setAttribute("length", entry.getValue());
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
				graph.getEdge(i).setAttribute("ui.style", "text-size: 20;");
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
		graph.addNode("Diccionario").setAttribute("ui.style", "fill-color: #FCF865; text-alignment: at-right; text-size: 12;");
		return graph;
	}
	
}
