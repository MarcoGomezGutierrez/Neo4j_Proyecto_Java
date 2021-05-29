package neo4j.function;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import neo4j.Cerebro.Function;
import neo4j.Cerebro.Intelligence;
import python.voz.TextToVoice;

class FunctionTest {
	
	final TextToVoice voice = new TextToVoice();
	
	@BeforeAll
    public static void learnSomething() {
		Intelligence.learn("Pato", Arrays.asList("Dulce", "Amargo"), Arrays.asList("Sabe mas o menos dulce", "Sabe amargo tambien"),
				Arrays.asList("Tamaño", "Forma Visual"), Arrays.asList("Tiene un tamaño grande", "Es forma bonito"),
				Arrays.asList("Temperatura", "Forma Fisica"), Arrays.asList("No quema", "Un animal con patas y plumas"),
				Arrays.asList("Intensidad", "Distancia"), Arrays.asList("Hace CuaCua", "Cuando le oi estaba lejos"),
				Arrays.asList("Agradable"), Arrays.asList("Huele bien"));
    }
	
	@Test
	public void testQuestionKnowElem() {
		String text  = Intelligence.questionIfKnowElem("Pato");
		System.out.println(text);
		voice.hablar(text);
		assertTrue(true);
	}
	
	@AfterAll
    public static void deleteNode() {
		Function.deleteNode("Pato");
    }
	
	
	
	@Test
	void testString() {
		System.out.println(Function.countRelationship("Sabor"));
	}
	
	@Test
	void testMatchAllOrgano() {
		Set<String> expected = new TreeSet<String>(Set.of("Lengua", "Nariz", "Oido", "Piel", "Ojo"));
		Set<String> list = new TreeSet<String>(Function.matchAllOrgano());
		assertArrayEquals(expected.toArray(), list.toArray());
	}

	@Test
	void testMatchAllSentido() {
		Set<String> expected = new TreeSet<String>(Set.of("Gusto", "Olfato", "Oido", "Vista", "Tacto"));
		Set<String> list = new TreeSet<String>(Function.matchAllSentido());
		assertArrayEquals(expected.toArray(), list.toArray());
	}

	@Test
	void testMatchAllSabor() {
		Set<String> expected = new TreeSet<String>(Set.of("Dulce", "Salado", "Acido", "Amargo"));
		Set<String> list = new TreeSet<String>(Function.matchAllSabor());
		assertArrayEquals(expected.toArray(), list.toArray());
	}

	@Test
	void testMatchAllOlor() {
		Set<String> expected = new TreeSet<String>(Set.of("Desagradable", "Agradable"));
		Set<String> list = new TreeSet<String>(Function.matchAllOlor());
		assertArrayEquals(expected.toArray(), list.toArray());
	}

	@Test
	void testMatchAllVer() {
		Set<String> expected = new TreeSet<String>(Set.of("Forma Visual", "Tamaño", "Color"));
		Set<String> list = new TreeSet<String>(Function.matchAllVer());
		assertArrayEquals(expected.toArray(), list.toArray());
	}

	@Test
	void testMatchAllOir() {
		Set<String> expected = new TreeSet<String>(Set.of("Intensidad", "Distancia"));
		Set<String> list = new TreeSet<String>(Function.matchAllOir());
		assertArrayEquals(expected.toArray(), list.toArray());
	}

	@Test
	void testMatchAllTocar() {
		Set<String> expected = new TreeSet<String>(Set.of("Consistencia", "Temperatura", "Textura", "Forma Fisica"));
		Set<String> list = new TreeSet<String>(Function.matchAllTocar());
		assertArrayEquals(expected.toArray(), list.toArray());
	}

}

