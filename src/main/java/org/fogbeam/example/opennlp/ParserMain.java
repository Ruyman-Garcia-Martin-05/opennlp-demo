package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

/**
 * @class ParserMain
 * @brief Clase principal para demostrar el uso del modelo de parser de OpenNLP.
 *
 * Esta clase utiliza un modelo de parser preentrenado para analizar
 * sintácticamente una oración de entrada y mostrar el árbol de parseo.
 */
public class ParserMain {

	/** Logger para manejar mensajes y errores. */
	private static final Logger LOGGER = Logger.getLogger(ParserMain.class.getName());

	/**
	 * Método principal que carga un modelo de parser, analiza una oración
	 * y muestra el árbol de parseo.
	 *
	 * @param args Argumentos de la línea de comandos (no se usan).
	 */
	public static void main(String[] args) {
		InputStream modelIn = null;

		try {
			// Cargar el modelo del parser desde un archivo.
			modelIn = new FileInputStream("models/en-parser-chunking.bin");
			ParserModel model = new ParserModel(modelIn);

			// Crear un objeto Parser para analizar oraciones.
			Parser parser = ParserFactory.create(model);

			// Texto a analizar.
			String sentence = "The quick brown fox jumps over the lazy dog .";

			// Parsear la oración y obtener los resultados.
			Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

			// Obtener y mostrar el primer parseo.
			Parse parse = topParses[0];
			System.out.println(parse.toString());

			// Mostrar el árbol de parseo.
			parse.showCodeTree();

		} catch (IOException e) {
			/**
			 * Manejo de errores durante la carga del modelo o el análisis de la oración.
			 * @exception IOException Si ocurre un error al cargar el modelo o procesar el texto.
			 */
			LOGGER.log(Level.SEVERE, "Error while loading the parser model or processing the sentence", e);
		} finally {
			if (modelIn != null) {
				try {
					// Cerrar el flujo del modelo.
					modelIn.close();
				} catch (IOException e) {
					/**
					 * Manejo de errores al cerrar el flujo de entrada del modelo.
					 * @exception IOException Si ocurre un error al cerrar el flujo.
					 */
					LOGGER.log(Level.WARNING, "Error closing the parser model input stream", e);
				}
			}
		}

		// Indicar que el proceso ha finalizado.
		System.out.println("done");
	}
}
