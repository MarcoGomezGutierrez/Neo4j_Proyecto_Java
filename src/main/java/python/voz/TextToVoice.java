package python.voz;

import java.io.IOException;

public class TextToVoice {
	public void hablar(String texto) {
		try {
			Runtime.getRuntime().exec("cmd /c python C:\\Users\\marco\\eclipse-workspace\\Neo4jProyectoFinal\\src\\main\\java\\python\\voz\\Voz.py \"" + texto + "\"", null);
		} catch (IOException e) {
			System.out.println(" " + e);
		}
	}
	
}
