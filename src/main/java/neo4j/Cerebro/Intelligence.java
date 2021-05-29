package neo4j.Cerebro;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.IntStream;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;

public class Intelligence {
	
	/**
	 * Si contiene elem te dice sus características si no contiene te dice que "No sabe que es"
	 * @param elem (String)
	 * @return Un String con lo que has preguntado si lo conoce con descripción
	 */
	public static String questionIfKnowElem(String elem) {
		TreeMap<Float, String> map = Function.getTheNodeRelationshipNode(elem);
		if (!map.isEmpty()) {
			List<String> result = Function.mapToString(map);
			return IntStream
					.range(0, result.size())
					.mapToObj(i -> i == 0 ? result.get(i) : (result.get(i).charAt(0) == 'P' ? ".\n " + result.get(i) : result.get(i)))
					.reduce("Tengo almacenado " + elem + " y obtengo que: ", (accum, x) -> accum + x);
		}
		else return "No se que es un '" + elem + "', pero siempre puedes enseñarmelo.";
	}
	
	/**
	 * Este método me inserta los nodos en la base de datos apuntando a los 
	 * nodos con los atributos que tu quieres que la inteligencia artificial aprenda con su descripción.
	 *
	 * @param learn (Lo que quieres que aprenda) -> Ejemplo: enseñarle lo que es un Pato
	 * @param gusto (Atributos del gusto)
	 * @param descriptionGusto (Descripcion del gusto)
	 * @param vista (Atributos de la vista)
	 * @param descriptionVista (Descripcion del vista)
	 * @param tacto (Atributos del tacto)
	 * @param descriptionTacto (Descripcion del tacto)
	 * @param oido (Atributos del oido)
	 * @param descriptionOido (Descripcion del oido)
	 * @param olfato (Atributos del olfato)
	 * @param descriptionOlfato (Descripcion del olfato)
	 */
	public static boolean learn(final String learn, final List<String> gusto, final List<String> descriptionGusto,
			final List<String> vista, final List<String> descriptionVista,
			final List<String> tacto, final List<String> descriptionTacto,
			final List<String> oido, final List<String> descriptionOido,
			final List<String> olfato, final List<String> descriptionOlfato) {
		
		try ( Session session = Conexion.getDriver().session(SessionConfig.forDatabase("cerebro")) ) {
            return session.writeTransaction( new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    tx.run( "CREATE (x:New:" + learn + " {nombre : '" + learn + "'})");
                    if (!gusto.isEmpty()) for (int i = 0; i < gusto.size(); i++) 
                     	Function.nodeRelationshipNode(tx, learn, gusto.get(i), "Sabor", "sabor", descriptionGusto.get(i));
                    if (!vista.isEmpty()) for (int i = 0; i < vista.size(); i++) 
                    	Function.nodeRelationshipNode(tx, learn, vista.get(i), "Ver", "ver", descriptionVista.get(i));
                    if (!olfato.isEmpty()) for (int i = 0; i < olfato.size(); i++) 
                    	Function.nodeRelationshipNode(tx, learn, olfato.get(i), "Olor", "olor", descriptionOlfato.get(i));
                    if (!oido.isEmpty()) for (int i = 0; i < oido.size(); i++) 
                    	Function.nodeRelationshipNode(tx, learn, oido.get(i), "Oir", "oir", descriptionOido.get(i));
                    if (!tacto.isEmpty()) for (int i = 0; i < tacto.size(); i++) 
                    	Function.nodeRelationshipNode(tx, learn, tacto.get(i), "Tocar", "tocar", descriptionTacto.get(i));
                	return true;
                }
            } );
        } catch(Exception e) {
        	return false;
        }
	}
	
	/**
	 * Me saca todos los nodos nuevos que he insertado. Ejemplo: Pato
	 * @return 
	 */
	public static List<String> newNodesGraph() {
		return getNewNodes();
	}
	
	/**
	 * Método que mezclado con el método newNodesGraph() me saca los otros nodos de la relación que busco
	 * Ejemplo si Pato esta conectado con Dulce, Forma Física, Tamaño. Me saca estos tres últimos nodos en la lista 
	 * Construida de la forma Objeto->(Nodo, List<Relaciones>(Dulce, Forma, Tamaño))
	 * @param list
	 * @return Devuelve una lista de Objetos (Nodo, List<Relaciones>)
	 */
	public static List<NodeLists> completeGraph(List<String> list) {
		List<NodeLists> matrix = new ArrayList<NodeLists>();
		for (String node : list) {
			matrix.add(getNodeList(node));
		}
		return matrix;
	}
	
	/**
	 * Obtengo la lista de las Relaciones: Dulce, Forma Física
	 * @param node
	 * @return
	 */
	private static NodeLists getNodeList(String node) {
		try ( Session session = Conexion.getDriver().session(SessionConfig.forDatabase("cerebro")) ) {
            return session.writeTransaction( new TransactionWork<NodeLists>() {
                @Override
                public NodeLists execute(Transaction tx) {
                   Result result = tx.run( "MATCH ()-[r:" + node + "]->(y) RETURN y.nombre");
                   return getNodeList(node, result);
                }

				private NodeLists getNodeList(String node, Result result) {
					List<String> list = new ArrayList<String>();
					for (String text : Function.resultToList(result)) {
						list.add(text);
					}
					return new NodeLists(node, list);
				}
            } );
        }
	}
	
	/**
	 * Obtengo la lista de los nuevos nodos. Ejemplo: Pato, Avion, Edificio...
	 * @return
	 */
	public static List<String> getNewNodes() {
		try ( Session session = Conexion.getDriver().session(SessionConfig.forDatabase("cerebro")) ) {
            return session.writeTransaction( new TransactionWork<List<String>>() {
                @Override
                public List<String> execute(Transaction tx) {
                   Result result = tx.run( "MATCH (x:New) RETURN x.nombre");
                   return Function.resultToList(result);
                }
            } );
        }
	}
}
