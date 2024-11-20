package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

/**
 * @class NameFinderMain
 * @brief Clase principal para demostrar el reconocimiento de nombres con OpenNLP.
 *
 * Esta clase utiliza un modelo preentrenado de OpenNLP para identificar nombres
 * propios en un conjunto de tokens.
 */
public class NameFinderMain {

	/** Logger para manejar mensajes de error y depuración. */
	private static final Logger LOGGER = Logger.getLogger(NameFinderMain.class.getName());

	/**
	 * Método principal que carga un modelo de reconocimiento de nombres,
	 * analiza un conjunto de tokens y muestra los nombres encontrados.
	 *
	 * @param args Argumentos de la línea de comandos (no se usan).
	 */
	public static void main(String[] args) {
		InputStream modelIn = null;

		try {
			// Cargar el modelo de reconocimiento de nombres.
			modelIn = new FileInputStream("models/en-ner-person.model");
			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);

			// Crear un objeto NameFinderME para realizar el reconocimiento de nombres.
			NameFinderME nameFinder = new NameFinderME(model);

			// Tokens para analizar.
			String[] tokens = { "Phillip", "Rhodes", "is", "presenting", "at", "some", "meeting", "." };

			// Encontrar nombres en los tokens.
			Span[] names = nameFinder.find(tokens);

			// Procesar los resultados y construir los nombres detectados.
			for (Span ns : names) {
				System.out.println("Found name: " + ns.toString());

				// Construir el nombre completo desde los tokens.
				StringBuilder name = new StringBuilder();
				for (int i = ns.getStart(); i < ns.getEnd(); i++) {
					name.append(tokens[i]).append(" ");
				}
				System.out.println("The name is: " + name.toString().trim());
			}

			// Limpiar los datos adaptativos del NameFinderME.
			nameFinder.clearAdaptiveData();

		} catch (IOException e) {
			/**
			 * Manejo de errores durante la carga del modelo o el análisis de los tokens.
			 * @exception IOException Si ocurre un error al cargar el modelo o procesar los tokens.
			 */
			LOGGER.log(Level.SEVERE, "Error while loading the name finder model or processing tokens", e);
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
					LOGGER.log(Level.WARNING, "Error closing the model input stream", e);
				}
			}
		}

		// Indicar que el proceso ha finalizado.
		System.out.println("done");
	}
}
