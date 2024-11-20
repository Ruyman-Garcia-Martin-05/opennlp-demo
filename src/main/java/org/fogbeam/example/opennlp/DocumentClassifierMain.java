package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;

/**
 * @class DocumentClassifierMain
 * @brief Clase principal para demostrar la clasificación de documentos con OpenNLP.
 *
 * Esta clase carga un modelo preentrenado para clasificar un texto de entrada
 * en una categoría específica.
 */
public class DocumentClassifierMain {

	/** Logger para manejar mensajes de error y depuración. */
	private static final Logger LOGGER = Logger.getLogger(DocumentClassifierMain.class.getName());

	/**
	 * Método principal que carga un modelo de clasificación de documentos,
	 * clasifica un texto de entrada y muestra la categoría asignada.
	 *
	 * @param args Argumentos de la línea de comandos (no se usan).
	 */
	public static void main(String[] args) {
		InputStream is = null;

		try {
			// Cargar el modelo desde el archivo.
			is = new FileInputStream("models/en-doccat.model");
			DoccatModel m = new DoccatModel(is);

			// Texto a clasificar.
			String inputText = "What happens if we have declining bottom-line revenue?";
			DocumentCategorizerME myCategorizer = new DocumentCategorizerME(m);

			// Clasificar el texto y obtener los resultados.
			double[] outcomes = myCategorizer.categorize(inputText);
			String category = myCategorizer.getBestCategory(outcomes);

			// Imprimir la categoría clasificada.
			System.out.println("Input classified as: " + category);

		} catch (IOException e) {
			/**
			 * Manejo de errores durante la carga del modelo o la clasificación.
			 * @exception IOException Si ocurre un error al cargar el modelo o procesar el texto.
			 */
			LOGGER.log(Level.SEVERE, "Error while loading the model or classifying the input text", e);
		} finally {
			if (is != null) {
				try {
					// Cerrar el flujo del modelo.
					is.close();
				} catch (IOException e) {
					/**
					 * Manejo de errores al cerrar el flujo de entrada del modelo.
					 * @exception IOException Si ocurre un error al cerrar el flujo.
					 */
					LOGGER.log(Level.WARNING, "Error closing the model input stream", e);
				}
			}
		}

		// Indicar que el proceso ha finalizado.
		System.out.println("done");
	}
}
