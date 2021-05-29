package neo4j.Diccionario;

import java.util.ArrayList;
import java.util.List;

public class Document {
	private final String name;
	private final String contents;

	public Document(String name, String contents) {
		this.name = name;
		this.contents = getDefinicion(contents);
	}
	
	/**
	 * Este método es una aproximazión a la definición de la palabra
	 * ya que saca transformo el html que esta formado por: 
	 *  1.Definición
	 *  2.Definición 
	 *  3...........
	 * Pues algunos no cumplen eso si la palabra que buscas no esta en el diccionario
	 * @param contents
	 * @return String con las definiciones de la palabra
	 */
	private String getDefinicion(String contents) {
		try {
			String [] text = contents.split(".1.")[1].split(" ");
			int num = 2;
			List<String> list = new ArrayList<String>();
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < text.length; i++) {
				if (i == text.length - 1) {
					list.add(buffer.toString());
				}
				else if (text[i].contains("." + num + ".")) {
					buffer.append(text[i].substring(0, text[i].length() - 3));
					list.add(buffer.toString() + "\\n");
					buffer.setLength(0);
					num++;
				} else {
					if (text[i].contains("'")) {
						buffer.append(text[i].replace("'", "") + " ");
					} else {
						buffer.append(text[i] + " ");
					}
				}
			}
			return list.toString();
		} catch (Exception e) {
			return "Sin definición";
		}
	}
	
	public String getName() {
		return name;
	}


	public String getContents() {
		return contents;
	}


	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
