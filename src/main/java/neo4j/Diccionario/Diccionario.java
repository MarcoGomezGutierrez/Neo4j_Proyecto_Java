package neo4j.Diccionario;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;

import neo4j.Cerebro.Conexion;

public class Diccionario {
	
	/**
	 * Devuelve la definición de cada palabra, borrando las comillas 
	 * y los corchetes para que el bot lo pueda leer
	 * @param palabra
	 * @return
	 */
	public static String buscarDefinicion(String palabra) {
		try (Session session = Conexion.getDriver().session(SessionConfig.forDatabase("diccionario"))) {
			return session.writeTransaction(new TransactionWork<String>() {
				@Override
				public String execute(Transaction tx) {
					Result result = tx.run("MATCH (n:Palabra {nombre:'" + palabra + "'}) RETURN n.definicion");
					final List<Record> list = result.list();
					if (!list.isEmpty()) {
						return list.get(0).get(0).toString().replace(Character.toString('"'), "").replace("[", "").replace("]", "");
					} else {
						return "No se encontro";
					}
				}
			});
		}
	}
	
	/**
	 * La lista contiene frases separadas por parrafos y las mismas palabras las inserto en minusculas 
	 * lo que más tarda de este método es taerse toda la información de la RAE
	 * @param list
	 */
	public static void insertarPalabras(List<String> list) {
		try (Session session = Conexion.getDriver().session(SessionConfig.forDatabase("diccionario"))) {
			session.writeTransaction(new TransactionWork<Boolean>() {
				@Override
				public Boolean execute(Transaction tx) {
					for (String palabra : list) {
						final String lowerCaseWord = palabra.toLowerCase();
						Result result = tx.run("MATCH (n:Palabra {nombre:'"+ lowerCaseWord + "'}) RETURN n");
						if (result.list().isEmpty()) {
							tx.run("CREATE (n:Palabra {nombre:'"+ lowerCaseWord + "', definicion:'"+ RaePalabra.searchWord(lowerCaseWord).getContents() + "'})");
							tx.run("MATCH (d:Diccionario {nombre:'Diccionario'}) "
									+ "MATCH (p:Palabra {nombre:'"+ lowerCaseWord + "'})"
									+ "CREATE (d)-[:Palabra {palabra:'" + lowerCaseWord + "', repeticiones:1}]->(p)");
						} else {
							result = tx.run("MATCH (d:Diccionario)-[x:Palabra {palabra:'" + lowerCaseWord + "'}]->()"
									+ "RETURN x.repeticiones");
							int num = Integer.valueOf(result.list().get(0).get(0).toString()) + 1;
							tx.run("MATCH (d:Diccionario)-[x:Palabra {palabra:'" + lowerCaseWord + "'}]->()"
									+ "DELETE x");
							tx.run("MATCH (d:Diccionario {nombre:'Diccionario'})"
									+ "MATCH (p:Palabra {nombre:'"+ lowerCaseWord + "'})"
									+ "CREATE (d)-[x:Palabra {palabra:'" + lowerCaseWord + "', repeticiones:" + num + "}]->(p)");
						}
						
					}
					return true;
				}
			});
		}
	}
	
	/**
	 * Retorna un Map<String, Integer> con la palabra y su número de repeticiones
	 * @return Palabra, Número de Repeticiones
	 */
	public static Map<String, Integer> contarPalabras() {
		try (Session session = Conexion.getDriver().session(SessionConfig.forDatabase("diccionario"))) {
			return session.writeTransaction(new TransactionWork<Map<String, Integer>>() {
				@Override
				public Map<String, Integer> execute(Transaction tx) {
					Result result = tx.run("MATCH (d:Diccionario)-[x:Palabra]->() RETURN x.palabra, x.repeticiones");
					return resultToMap(result.list());
				}

				private Map<String,Integer> resultToMap(List<Record> list) {
					return list
							.parallelStream()
							.collect(Collectors.toMap(
									elem -> elem.get(0).toString().replace(Character.toString('"'), ""), 
									elem -> Integer.valueOf(elem.get(1).toString())
									));
				}
			});
		}
	}

}
