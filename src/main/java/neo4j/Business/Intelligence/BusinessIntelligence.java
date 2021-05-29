package neo4j.Business.Intelligence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import neo4j.Cerebro.Function;
import neo4j.Cerebro.Intelligence;
import neo4j.Diccionario.Diccionario;

public class BusinessIntelligence {
	
	/**
	 * Método para Crear/Actualizar el Excel
	 */
	public static void crearExcel() {
		Workbook book = new HSSFWorkbook();
		Sheet sheetCerebro = book.createSheet("Cerebro");
		Sheet sheetDiccionario = book.createSheet("Diccionario");
		/**
		 * Creación de hoja de Excel con los datos del Grafo Cerebro
		 */
		createTableCerebro(sheetCerebro, book);
		createTableNumeroRelacionesOrgano(sheetCerebro, book);
		createTableNewRelaciones(sheetCerebro, book);
		
		/**
		 * Creación de la hoja de Excel con los datos del Grafo Diccionario
		 */
		createTableDiccionario(sheetDiccionario, book);
		try {
			FileOutputStream fileout = new FileOutputStream("Excel.xls");
			book.write(fileout);
			fileout.close();
			book.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void createTableDiccionario(Sheet sheet, Workbook book) {
		CellStyle titulo = getStyleTitulo(book);
		CellStyle datos = getStyleRelationship(book);
		
		Row row = sheet.createRow(4);
		
		row.createCell(4).setCellValue("Palabra");
		row.getCell(4).setCellStyle(titulo);
		row.createCell(5).setCellValue("Repeticiones");
		row.getCell(5).setCellStyle(titulo);
		
		int i = 0;
		
		for (Entry<String, Integer> entry : Diccionario.contarPalabras().entrySet()) {
			row = sheet.createRow(5 + i);
			row.createCell(4).setCellValue(entry.getKey());
			row.getCell(4).setCellStyle(datos);
			row.createCell(5).setCellValue(entry.getValue());
			row.getCell(5).setCellStyle(datos);
			i++;
		}
	}
	
	public static List<NodeRelationship> leerTablaDiccionario() {
		try {
			FileInputStream file = new FileInputStream("Excel.xls");
			HSSFWorkbook libro = new HSSFWorkbook(file);
			
			HSSFSheet sheet = libro.getSheetAt(1);
			
			Row row;
			Cell celda;
			String node = "";
			int relationship = 0;
			int num = 0;
			List<NodeRelationship> list = new ArrayList<NodeRelationship>();
			
			while (true) {
				try {
					
					row = sheet.getRow(5 + num);
					Iterator<Cell> celdas = row.iterator();
					while (celdas.hasNext()) {
						celda = celdas.next();
						
						switch (celda.getCellType().toString()) {
							case "NUMERIC":
								relationship = (int) celda.getNumericCellValue();
								break;
							case "STRING":
								node = celda.getStringCellValue();
								break;
								
						}
						
					}
					list.add(new NodeRelationship(node, relationship));
					num++;
					
				}catch (NullPointerException e) { break; }
			}
			
			libro.close();
			file.close();
			
			return list;
			
		} catch (FileNotFoundException e) {} catch (IOException e) {}
		
		return new ArrayList<NodeRelationship>();
	}
	
	/**
	 * Método que me lee la tabla New para posteriormente pintarme una gráfica circular con Java
	 * @return Lista con  (Nodo, Relación)
	 */
	public static List<NodeRelationship> leerTabla() {
		try {
			FileInputStream file = new FileInputStream("Excel.xls");
			HSSFWorkbook libro = new HSSFWorkbook(file);
			
			HSSFSheet sheet = libro.getSheetAt(0);
			
			Row row;
			Cell celda;
			String node = "";
			int relationship = 0;
			int num = 0;
			List<NodeRelationship> list = new ArrayList<NodeRelationship>();
			
			while (true) {
				try {
					
					row = sheet.getRow(20 + num);
					Iterator<Cell> celdas = row.iterator();
					while (celdas.hasNext()) {
						celda = celdas.next();
						
						switch (celda.getCellType().toString()) {
							case "NUMERIC":
								//System.out.println(celda.getNumericCellValue());
								relationship = (int) celda.getNumericCellValue();
								break;
							case "STRING":
								//System.out.println(celda.getStringCellValue());
								node = celda.getStringCellValue();
								break;
								
						}
						
					}
					list.add(new NodeRelationship(node, relationship));
					num++;
					
				}catch (NullPointerException e) { break; }
			}
			
			libro.close();
			file.close();
			
			return list;
			
		} catch (FileNotFoundException e) {} catch (IOException e) {}
		
		return new ArrayList<NodeRelationship>();
	}
	
	/**
	 * Me ejecuta un Runnable con un comando en la cmd para abrirme el Excel
	 */
	public static void mostrar() {
		try {
			Runtime.getRuntime().exec("cmd /c start C:\\Users\\marco\\eclipse-workspace\\Neo4jProyectoFinal\\Excel.xls");
		} catch (IOException e) {}
	}
	
	/**
	 * Método que crea las nuevas nodos con que Ralaciones estan conectados
	 * @param sheet
	 * @param book
	 */
	private static void createTableNewRelaciones(Sheet sheet, Workbook book) {
		Row row = sheet.createRow(19);
		
		Font font = book.createFont();
		font.setFontHeightInPoints((short)10);
		font.setColor(IndexedColors.BLACK.index);
		font.setBold(true);
		font.setFontName("Arial");
		
		CellStyle style = book.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFont(font);
		style.setWrapText(true);
		
		row.createCell(4).setCellValue("Nodos Nuevos");
		row.getCell(4).setCellStyle(style);
		row.createCell(5).setCellValue("Nº Relac.");
		row.getCell(5).setCellStyle(style);
		
		int i = 0;
		
		font = book.createFont();
		font.setColor(IndexedColors.BLACK.index);
		font.setFontName("Arial");
		
		style = book.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFillForegroundColor(IndexedColors.GREEN.index);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFont(font);
		
		for (String node : Intelligence.getNewNodes()) {
			row = sheet.createRow(20 + i);
			row.createCell(4).setCellValue(node);
			row.getCell(4).setCellStyle(style);
			row.createCell(5).setCellValue(Function.countRelationship(node));
			row.getCell(5).setCellStyle(style);
			i++;
		}
	}
	
	/**
	 * Tabla que me crea una tabla con los Organos y el Número de Relaciones.
	 * @param sheet
	 */
	private static void createTableNumeroRelacionesOrgano(Sheet sheet, Workbook book) {
		Row row = sheet.createRow(12);
		row.createCell(4).setCellValue("Órgano");
		row.getCell(4).setCellStyle(getStyleTitulo(book));
		row.createCell(5).setCellValue("Nº Relac.");
		row.getCell(5).setCellStyle(getStyleEncabezado(book));

		row = sheet.createRow(13);
		row.createCell(4).setCellValue("Lengua");
		row.getCell(4).setCellStyle(getStyleEncabezado(book));
		row.createCell(5).setCellValue(Function.countRelationship("Sabor"));
		row.getCell(5).setCellStyle(getStyleRelationship(book));
		
		row = sheet.createRow(14);
		row.createCell(4).setCellValue("Nariz");
		row.getCell(4).setCellStyle(getStyleEncabezado(book));
		row.createCell(5).setCellValue(Function.countRelationship("Olor"));
		row.getCell(5).setCellStyle(getStyleRelationship(book));
		
		row = sheet.createRow(15);
		row.createCell(4).setCellValue("Oído");
		row.getCell(4).setCellStyle(getStyleEncabezado(book));
		row.createCell(5).setCellValue(Function.countRelationship("Oir"));
		row.getCell(5).setCellStyle(getStyleRelationship(book));
		
		row = sheet.createRow(16);
		row.createCell(4).setCellValue("Piel");
		row.getCell(4).setCellStyle(getStyleEncabezado(book));
		row.createCell(5).setCellValue(Function.countRelationship("Tocar"));
		row.getCell(5).setCellStyle(getStyleRelationship(book));
		
		row = sheet.createRow(17);
		row.createCell(4).setCellValue("Ojo");
		row.getCell(4).setCellStyle(getStyleEncabezado(book));
		row.createCell(5).setCellValue(Function.countRelationship("Ver"));
		row.getCell(5).setCellStyle(getStyleRelationship(book));
	}
	
	private static CellStyle getStyleTitulo(Workbook book) {
		Font font = book.createFont();
		font.setFontHeightInPoints((short)10);
		font.setColor(IndexedColors.BLACK.index);
		font.setBold(true);
		font.setFontName("Arial");
		
		CellStyle style = book.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.index);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFont(font);
		//style.setWrapText(true);
		
		return style;
	}

	private static CellStyle getStyleRelationship(Workbook book) {
		Font font = book.createFont();
		font.setFontHeightInPoints((short)10);
		font.setColor(IndexedColors.BLACK.index);
		font.setBold(true);
		font.setFontName("Arial");
		
		CellStyle style = book.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFillForegroundColor(IndexedColors.AQUA.index);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFont(font);
		//style.setWrapText(true);
		
		return style;
	}

	private static CellStyle getStyleEncabezado(Workbook book) {
		Font fontEncabezado = book.createFont();
		fontEncabezado.setFontHeightInPoints((short)10);
		fontEncabezado.setColor(IndexedColors.BLACK.index);
		fontEncabezado.setBold(true);
		fontEncabezado.setFontName("Arial");
		
		CellStyle styleEncabezado = book.createCellStyle();
		styleEncabezado.setAlignment(HorizontalAlignment.CENTER);
		styleEncabezado.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
		styleEncabezado.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleEncabezado.setFont(fontEncabezado);
		//styleEncabezado.setWrapText(true);
		
		return styleEncabezado;
	}

	/**
	 * Primer método que me crea la primera tabla, Sentido con gusto, luego comprueba que en la base de datos haya 5-5
	 * @param sheet
	 */
	private static void createTableCerebro(Sheet sheet, Workbook book) {
		// Fila -> 5
		Row row = sheet.createRow(4);
		// Columna -> 5
		row.createCell(4).setCellValue("Cerebro");
		row.getCell(4).setCellStyle(getStyleTitulo(book));
		row.createCell(5).setCellValue("Organo");
		row.getCell(5).setCellStyle(getStyleEncabezado(book));
		row.createCell(6).setCellValue("Nº de Realac. Cerebro");
		row.getCell(6).setCellStyle(getStyleEncabezado(book));
		
		row = sheet.createRow(5);
		row.createCell(4).setCellValue("Gusto");
		row.getCell(4).setCellStyle(getStyleEncabezado(book));
		row.createCell(5).setCellValue("Lengua");
		row.getCell(5).setCellStyle(getStyleRelationship(book));
		
		row = sheet.createRow(6);
		row.createCell(4).setCellValue("Vista");
		row.getCell(4).setCellStyle(getStyleEncabezado(book));
		row.createCell(5).setCellValue("Ojo");
		row.getCell(5).setCellStyle(getStyleRelationship(book));
		
		row = sheet.createRow(7);
		row.createCell(4).setCellValue("Oido");
		row.getCell(4).setCellStyle(getStyleEncabezado(book));
		row.createCell(5).setCellValue("Oido");
		row.getCell(5).setCellStyle(getStyleRelationship(book));
		
		row = sheet.createRow(8);
		row.createCell(4).setCellValue("Olfato");
		row.getCell(4).setCellStyle(getStyleEncabezado(book));
		row.createCell(5).setCellValue("Nariz");
		row.getCell(5).setCellStyle(getStyleRelationship(book));
		
		row = sheet.createRow(9);
		row.createCell(4).setCellValue("Tacto");
		row.getCell(4).setCellStyle(getStyleEncabezado(book));
		row.createCell(5).setCellValue("Piel");
		row.getCell(5).setCellStyle(getStyleRelationship(book));
		
		row = sheet.getRow(5);
		/**
		 * Cuenta las relaciones para comprobar que son 5-5
		 */
		row.createCell(6).setCellValue(Function.countRelationship("Sentido"));
		row.getCell(6).setCellStyle(getStyleRelationship(book));
	}

}
