package neo4j.Cerebro;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;

public class Function {
	
	/**
	 * Método que me borra todos los nodos y relaciones siempre que tengas el rol adecuado 
	 * @return
	 */
	public static boolean rebuildDB() {
		try ( Session session = Conexion.getDriver().session(SessionConfig.forDatabase("system")) ) {
            session.writeTransaction( new TransactionWork<Boolean>() {
                public Boolean execute(Transaction tx) {
                	tx.run("CREATE DATABASE cerebro IF NOT EXISTS");
                	tx.run("CREATE DATABASE diccionario IF NOT EXISTS");
                	return true;
                }
            });
		} catch (Exception e) {
			return false;
		}
		try ( Session session = Conexion.getDriver().session(SessionConfig.forDatabase("cerebro")) ) {
            session.writeTransaction( new TransactionWork<Boolean>() {
                public Boolean execute(Transaction tx) {
                    tx.run( "MATCH (n) DETACH DELETE n");
                    tx.run( "CREATE (Cerebro:Nombre {nombre:'Cerebro'}) "
                    		+ "CREATE (Gusto:Sentido {sentido:'Gusto'}) "
                    		+ "CREATE (Oido:Sentido {sentido:'Oido'}) "
                    		+ "CREATE (Vista:Sentido {sentido:'Vista'}) "
                    		+ "CREATE (Tacto:Sentido {sentido:'Tacto'}) "
                    		+ "CREATE (Olfato:Sentido {sentido:'Olfato'}) "
                    		+ "CREATE (Lengua:Organo {organo:'Lengua'}) "
                    		+ "CREATE (Nariz:Organo {organo:'Nariz'}) "
                    		+ "CREATE (Oido1:Organo {organo:'Oido'}) "
                    		+ "CREATE (Piel:Organo {organo:'Piel'}) "
                    		+ "CREATE (Ojo:Organo {organo:'Ojo'}) "
                    		+ "CREATE (Dulce:Sabor {sabor:'Dulce', nombre:'Dulce'}) "
                    		+ "CREATE (Amargo:Sabor {sabor:'Amargo', nombre:'Amargo'}) "
                    		+ "CREATE (Acido:Sabor {sabor:'Acido', nombre:'Acido'}) "
                    		+ "CREATE (Salado:Sabor {sabor:'Salado', nombre:'Salado'}) "
                    		+ "CREATE (Agradable:Olor {olor:'Agradable', nombre:'Agradable'}) "
                    		+ "CREATE (Desagradable:Olor {olor:'Desagradable', nombre:'Desagradable'}) "
                    		+ "CREATE (Intensidad:Oir {oir:'Intensidad', nombre:'Intensidad'}) "
                    		+ "CREATE (Distancia:Oir {oir:'Distancia', nombre:'Distancia'}) "
                    		+ "CREATE (Color:Ver {ver:'Color', nombre:'Color'}) "
                    		+ "CREATE (Tamaño:Ver {ver:'Tamaño', nombre:'Tamaño'}) "
                    		+ "CREATE (Forma1:Ver {ver:'Forma Visual', nombre:'Forma Visual'}) "
                    		+ "CREATE (Consistencia:Tocar {tocar:'Consistencia', nombre:'Consistencia'}) "
                    		+ "CREATE (Temperatura:Tocar {tocar:'Temperatura', nombre:'Temperatura'}) "
                    		+ "CREATE (Forma2:Tocar {tocar:'Forma Fisica', nombre:'Forma Fisica'}) "
                    		+ "CREATE (Textura:Tocar {tocar:'Textura', nombre:'Textura'}) ");
                    tx.run( "MATCH (Cerebro:Nombre {nombre:'Cerebro'}) "
                    		+ "MATCH (Gusto:Sentido {sentido:'Gusto'}) "
                    		+ "MATCH (Oido:Sentido {sentido:'Oido'}) "
                    		+ "MATCH (Vista:Sentido {sentido:'Vista'}) "
                    		+ "MATCH (Tacto:Sentido {sentido:'Tacto'}) "
                    		+ "MATCH (Olfato:Sentido {sentido:'Olfato'}) "
                    		+ "CREATE (Cerebro)-[:Sentido]->(Gusto) "
                    		+ "CREATE (Cerebro)-[:Sentido]->(Oido) "
                    		+ "CREATE (Cerebro)-[:Sentido]->(Vista) "
                    		+ "CREATE (Cerebro)-[:Sentido]->(Tacto) "
                    		+ "CREATE (Cerebro)-[:Sentido]->(Olfato) ");
                    tx.run( "MATCH (Gusto:Sentido {sentido:'Gusto'}) "
                    		+ "MATCH (Oido:Sentido {sentido:'Oido'}) "
                    		+ "MATCH (Vista:Sentido {sentido:'Vista'}) "
                    		+ "MATCH (Tacto:Sentido {sentido:'Tacto'}) "
                    		+ "MATCH (Olfato:Sentido {sentido:'Olfato'}) "
                    		+ "MATCH (Lengua:Organo {organo:'Lengua'}) "
                    		+ "MATCH (Nariz:Organo {organo:'Nariz'}) "
                    		+ "MATCH (Oido1:Organo {organo:'Oido'}) "
                    		+ "MATCH(Piel:Organo {organo:'Piel'}) "
                    		+ "MATCH(Ojo:Organo {organo:'Ojo'}) "
                    		+ "CREATE (Gusto)-[:Organo]->(Lengua) "
                    		+ "CREATE (Oido)-[:Organo]->(Oido1) "
                    		+ "CREATE (Vista)-[:Organo]->(Ojo) "
                    		+ "CREATE (Tacto)-[:Organo]->(Piel) "
                    		+ "CREATE (Olfato)-[:Organo]->(Nariz)");
                    tx.run( "MATCH (Lengua:Organo {organo:'Lengua'})"
                    		+ "MATCH (Dulce:Sabor {sabor:'Dulce'})"
                    		+ "MATCH (Amargo:Sabor {sabor:'Amargo'}) "
                    		+ "MATCH (Acido:Sabor {sabor:'Acido'}) "
                    		+ "MATCH (Salado:Sabor {sabor:'Salado'}) "
                    		+ "CREATE (Lengua)-[:Sabor]->(Dulce) "
                    		+ "CREATE (Lengua)-[:Sabor]->(Amargo) "
                    		+ "CREATE (Lengua)-[:Sabor]->(Acido) "
                    		+ "CREATE (Lengua)-[:Sabor]->(Salado)");
                    tx.run( "MATCH (Nariz:Organo {organo:'Nariz'}) "
                    		+ "MATCH (Agradable:Olor {olor:'Agradable'})  "
                    		+ "MATCH (Desagradable:Olor {olor:'Desagradable'}) "
                    		+ "CREATE (Nariz)-[:Olor]->(Agradable) "
                    		+ "CREATE (Nariz)-[:Olor]->(Desagradable)");
                    tx.run( "MATCH (Oido1:Organo {organo:'Oido'}) "
                    		+ "MATCH (Intensidad:Oir {oir:'Intensidad'}) "
                    		+ "MATCH (Distancia:Oir {oir:'Distancia'}) "
                    		+ "CREATE (Oido1)-[:Oir]->(Intensidad) "
                    		+ "CREATE (Oido1)-[:Oir]->(Distancia)");
                    tx.run( "MATCH(Ojo:Organo {organo:'Ojo'}) "
                    		+ "MATCH (Color:Ver {ver:'Color'}) "
                    		+ "MATCH (Tamaño:Ver {ver:'Tamaño'}) "
                    		+ "MATCH (Forma:Ver {ver:'Forma Visual'}) "
                    		+ "CREATE (Ojo)-[:Ver]->(Color) "
                    		+ "CREATE (Ojo)-[:Ver]->(Tamaño) "
                    		+ "CREATE (Ojo)-[:Ver]->(Forma)");
                    tx.run( "MATCH(Piel:Organo {organo:'Piel'}) "
                    		+ "MATCH (Consistencia:Tocar {tocar:'Consistencia'}) "
                    		+ "MATCH (Temperatura:Tocar {tocar:'Temperatura'}) "
                    		+ "MATCH (Forma:Tocar {tocar:'Forma Fisica'}) "
                    		+ "MATCH (Textura:Tocar {tocar:'Textura'}) "
                    		+ "CREATE (Piel)-[:Tocar]->(Consistencia) "
                    		+ "CREATE (Piel)-[:Tocar]->(Temperatura) "
                    		+ "CREATE (Piel)-[:Tocar]->(Forma) "
                    		+ "CREATE (Piel)-[:Tocar]->(Textura)");
                    return true;
                }

            } );
        } catch (Exception e) {
        	System.out.println("cerebro");
        	return false;
        }
		try ( Session session = Conexion.getDriver().session(SessionConfig.forDatabase("diccionario")) ) {
            return session.writeTransaction( new TransactionWork<Boolean>() {
                public Boolean execute(Transaction tx) {
                    tx.run( "MATCH (n) DETACH DELETE n");
                    tx.run("CREATE (n:Diccionario {nombre:'Diccionario'})");
                    return true;
                }

            } );
        } catch (Exception e) {
        	System.out.println("diccionario");
        	return false;
        }
	}
	
	public static List<String> matchAllOrgano()  {
    	return getTheNodeWithAttribute("Organo", "organo");
    }
	
	public static List<String> matchAllSabor()  {
		return getTheNodeWithAttribute("Sabor", "sabor");
    }
	
	public static List<String> matchAllOlor()  {
		return getTheNodeWithAttribute("Olor", "olor");
    }
	
	public static List<String> matchAllOir()  {
		return getTheNodeWithAttribute("Oir", "oir");
    }
	
	public static List<String> matchAllVer()  {
		return getTheNodeWithAttribute("Ver", "ver");
    }
	
	
	public static List<String> matchAllTocar()  {
		return getTheNodeWithAttribute("Tocar", "tocar");
    }
	public static List<String> matchAllSentido()  {
		return getTheNodeWithAttribute("Sentido", "sentido");
    }
	
	//TODO Contar relaciones de nodo que paso
	public static int countRelationship(final String relationship) {
		try ( Session session = Conexion.getDriver().session(SessionConfig.forDatabase("cerebro")) ) {
            return session.writeTransaction( new TransactionWork<Integer>() {
                @Override
                public Integer execute(Transaction tx) {
                    Result result = tx.run( "MATCH p=()-[r:" + relationship + "]->() RETURN count(p)");
                    return resultCount(result);
                }

				private int resultCount(Result result) {
					return Integer.valueOf(result.list().get(0).get(0).toString());
				}
            } );
        }
	}
	
	/**
	 * Transforma el map a un String con -> Sentido: atributo,... Descripcion,...
	 * Me guardo el anterior para saber si no se repite el mismo Sentido 
	 * y no repetir su descripción
	 * @param map
	 * @return
	 */
	public static List<String> mapToString(TreeMap<Float, String> map) {
		List<String> list = new ArrayList<String>();
		float f = 0.0f;
		for (Map.Entry<Float, String> entry : map.entrySet()) {
			list.add(getSentido(entry.getKey(), f) + relationalLogicFloat(entry.getKey()) + ", " + entry.getValue());
			f = entry.getKey();
		}
		return list;
	}

	
	/**
	 * 
	 * Devuelve el sentido siempre y cuando no se haya repetido si no añade una ", "
	 * Como las claves son:
	 
	  	1.0-Gusto		1.1-Dulce			1.2-Amargo			1.3-Salado			1.4-Acido
		2.0-Vista		2.1-Tamaño			2.2-Forma Visual	2.3-Color		
		3.0-Tacto		3.1-Forma Fisica	3.2-Temperatura		3.3-Consistencia	3.4-Textura	
		4.0-Oido		4.1-Intensidad		4.2-Distancia		
		5.0-Olfato		5.1-Agradable		5.2-Desagradable
		
	 *  Al castear el float a int puedo obtener el padre(Sentido) borrando la parte decimal 
	 *  y quedandome con la parte entera
	 * 
	 * @param elem -> nueva Key
	 * @param anterior -> Key anterior
	 * @return Devuelve El String del Sentido correspondiente a la clave
	 */
	private static String getSentido(float elem, float anterior) {
		if (((int) elem) == 1 && ((int) anterior)  != ((int)elem)) return "Por el sentido del gusto obtengo que: ";
		else if (((int) elem) == 2 && ((int) anterior)  != ((int)elem)) return "Por el sentido de la vista obtengo que: ";
		else if (((int) elem) == 3 && ((int) anterior)  != ((int)elem)) return "Por el sentido del tacto obtengo que: ";
		else if (((int) elem) == 4 && ((int) anterior)  != ((int)elem)) return "Por el sentido del oido obtengo que: ";
		else if (((int) elem) == 5 && ((int) anterior)  != ((int)elem)) return "Por el sentido del olfato obtengo que: ";
		else return ", ";
	}
	
	
	/**
	 * Método que transforma la lógica de Floats a String
	 * @param f
	 * @return
	 */
	private static String relationalLogicFloat(final float f) {
		String s = String.valueOf(f);
		switch (s) {
			case "1.1":
				return "sabe dulce";
			case "1.2":
				return "sabe amargo";
			case "1.3":
				return "sabe salado";
			case "1.4":
				return "sabe acido";
			case "2.1":
				return "veo un tamaño";
			case "2.2":
				return "veo una forma";
			case "2.3":
				return "veo un color";
			case "3.1":
				return "noto una forma";
			case "3.2":
				return "noto una temperatura";
			case "3.3":
				return "noto consistencia";
			case "3.4":
				return "noto textura";
			case "4.1":
				return "oigo una intensidad";
			case "4.2":
				return "oigo una distancia";
			case "5.1":
				return "su olor es agradable";
			case "5.2":
				return "su olor es desagradable";
			default:
				return "";
		}
	}
	
	/**
	 * 					  Node1--Attribute-->Node2
	 * Creo la relación del learn --learn--> node
	 * @param tx
	 * @param learn
	 * @param node
	 * @param attribute
	 * @param property
	 */
	public static void nodeRelationshipNode(Transaction tx, final String learn, final String node, final String attribute, final String property, final String description) {
		final float num = relationalLogic(node);
		if (num != 0.0f) tx.run( "MATCH (x:" + learn +" {nombre:'" + learn + "'}) "
								+ "MATCH (y:" + attribute + " {" + property  + " :'" + node + "'}) "
								+ "CREATE (x)-[:" + learn + "{index:"+ num +", description:'" + description + "'}]->(y)");
	}
	
	private static float relationalLogic(final String text) {
		switch (text) {
			case "Dulce":
				return 1.1f;
			case "Amargo":
				return 1.2f;
			case "Salado":
				return 1.3f;
			case "Acido":
				return 1.4f;
			case "Tamaño":
				return 2.1f;
			case "Forma Visual":
				return 2.2f;
			case "Color":
				return 2.3f;
			case "Forma Fisica":
				return 3.1f;
			case "Temperatura":
				return 3.2f;
			case "Consistencia":
				return 3.3f;
			case "Textura":
				return 3.4f;
			case "Intensidad":
				return 4.1f;
			case "Distancia":
				return 4.2f;
			case "Agradable":
				return 5.1f;
			case "Desagradable":
				return 5.2f;
			default:
				return 0.0f;
		}
		
			
	}
	
	
	/**
	 * Borro una relación en concreto.
	 *
	 * @param relationship
	 */
	private static boolean deleteRelationship(final String relationship) {
		try ( Session session = Conexion.getDriver().session(SessionConfig.forDatabase("cerebro")) ) {
            return session.writeTransaction( new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    tx.run( "MATCH p=()-[r:"+ relationship +"]->() DELETE r");
                    return true;
                }
            } );
        } catch (Exception e) {
        	return false;
        }
	}
	
	
	/**
	 * Borra el nodo, y su relación.
	 * @param node
	 */
	public static boolean deleteNode(final String node) {
		if (!deleteRelationship(node)) return false;
		try ( Session session = Conexion.getDriver().session(SessionConfig.forDatabase("cerebro")) ) {
            return session.writeTransaction( new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    tx.run( "MATCH (x:" + node + ") DELETE x");
                    return true;
                }
            } );
        } catch(Exception e) {
        	return false;
        }
	}
	
	
	/**
	 * (x)-[:Relationship]->(y) RETURN y:---
	 * Devuelve todos los nodos con su nombre que esten en y
	 * @param relationship
	 * @return List<String>
	 */
	public static TreeMap<Float, String> getTheNodeRelationshipNode(final String relationship) {
		try ( Session session = Conexion.getDriver().session(SessionConfig.forDatabase("cerebro")) ) {
			return session.writeTransaction( new TransactionWork<TreeMap<Float, String>>() {
            	@Override
                public TreeMap<Float, String> execute( Transaction tx ) {
                    Result resultFloat = tx.run( "MATCH p=(x)-[r:"+ relationship +"]->(y) "
                    		+ "RETURN r.index");
                    Result resultString = tx.run( "MATCH p=(x)-[r:"+ relationship +"]->(y) "
                    		+ "RETURN r.description");
                    return twoListToMap(listResultToListFloat(resultFloat), listResultToListString(resultString));
                }
            	
            	private TreeMap<Float, String> twoListToMap(List<Float> key, List<String> value) {
            		TreeMap<Float, String> map = new TreeMap<Float, String>();
					for (int i = 0; i < key.size(); i++) {
						map.put(key.get(i), value.get(i));
					}
					return map;
				}

				private List<Float> listResultToListFloat(Result result) {
					return extractFloat(result.list());
				}
            } );
        }
	}
	
	/**
	 * Devuelvo los atributos 
	 * @param attribute
	 * @param property
	 * @return List<String>
	 */
	public static List<String> getTheNodeWithAttribute(final String attribute, final String property) {
		try ( Session session = Conexion.getDriver().session(SessionConfig.forDatabase("cerebro")) ) {
            List<String> greeting = session.writeTransaction( new TransactionWork<List<String>>() {
            	@Override
                public List<String> execute( Transaction tx ) {
                    Result result = tx.run( "MATCH (x:" + attribute + ") " +
                                                     "RETURN x." + property + "");
                    return resultToList(result);
                }
            } );
            return greeting;
        }
	}
	
	
	/**
	 * Transforma la query a una List<String>
	 * @param result
	 * @return List<String>
	 */
	private static List<String> listResultToListString(Result result) {
		final List<Record> list = result.list();
		return matrixToList(recordToMatrix(list));
	}
	
	/**
	 * Crea una List<List<String>> que transformo de tipo Record a Matrix,
	 * elimino todos los valores NULL y elimino el caracter " que rodea los Strings
	 * @param list
	 * @return List<List<String>>
	 */
	private static List<List<String>> recordToMatrix(List<Record> list) {
		return IntStream.range(0, list.size())
			.mapToObj(n -> list.get(n))
			.map(elem -> {
				return IntStream
						.range(0, elem.size())
						.mapToObj(n -> elem.get(n).toString())
						.filter(string -> string != "NULL")
						.map(string -> string.replace(Character.toString('"'), ""))
						.collect(Collectors.toList());
			})
			.collect(Collectors.toList());
	}
	
	
	/**
	 * Transformo de una List<List<String>> a List<String>
	 * @param matrix
	 * @return List<String>
	 */
	private static List<String> matrixToList(List<List<String>> matrix) {
		return matrix
				.stream()
                .collect(ArrayList::new, List::addAll, List::addAll);
	}
	
	
	/**
	 * Transformo el tipo de resultado Result a una List<String> 
	 * y elimino el caracter " que rodea los Strings
	 * @param result
	 * @return List<String>
	 */
	public static List<String> resultToList(Result result) {
		return result
				.stream()
				.map(elem -> elem.get(elem.size() - 1)
						.toString())
				.map(elem -> elem
						.replace(Character.toString('"'), ""))
				.collect(Collectors.toList());
        		
	}
	
	/**
	 * Transforma la lista de Record a una lista 
	 * con los valores transformados a Float
	 * @param list
	 * @return Una List<Float>
	 */
	private static List<Float> extractFloat(List<Record> list) {
		return list
			.stream()
			.map(elem -> elem.get(0).toString())
			.map(elem -> Float.valueOf(elem))
			.collect(Collectors.toList());
	}
	
}
