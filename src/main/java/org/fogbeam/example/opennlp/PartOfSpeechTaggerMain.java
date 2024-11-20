package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

/**
 * @class PartOfSpeechTaggerMain
 * @brief Clase principal para demostrar el etiquetado de partes del discurso (POS) con OpenNLP.
 *
 * Esta clase utiliza un modelo preentrenado para etiquetar las partes del discurso
 * de un conjunto de tokens y mostrar las probabilidades asociadas a cada etiqueta.
 */
public class PartOfSpeechTaggerMain {

	/** Logger para manejar mensajes de error y depuración. */
	private static final Logger LOGGER = Logger.getLogger(PartOfSpeechTaggerMain.class.getName());

	/**
	 * Método principal que carga un modelo de etiquetado de partes del discurso (POS),
	 * etiqueta un conjunto de tokens y muestra los resultados.
	 *
	 * @param args Argumentos de la línea de comandos (no se usan).
	 */
	public static void main(String[] args) {
		InputStream modelIn = null;

		try {
			// Cargar el modelo de etiquetado de partes del discurso.
			modelIn = new FileInputStream("models/en-pos-maxent.bin");
			POSModel model = new POSModel(modelIn);

			// Crear un objeto POSTaggerME para realizar el etiquetado.
			POSTaggerME tagger = new POSTaggerME(model);

			// Tokens a analizar.
			String sent[] = new String[]{"Most", "large", "cities", "in", "the", "US", "had",
					"morning", "and", "afternoon", "newspapers", "."};

			// Etiquetar los tokens y obtener las probabilidades.
			String tags[] = tagger.tag(sent);
			double probs[] = tagger.probs();

			// Mostrar los resultados.
			for (int i = 0; i < sent.length; i++) {
				System.out.println("Token [" + sent[i] + "] has POS [" + tags[i] + "] with probability = " + probs[i]);
			}

		} catch (IOException e) {
			/**
			 * Manejo de errores durante la carga del modelo o el etiquetado de los tokens.
			 * @exception IOException Si ocurre un error al cargar el modelo o procesar los tokens.
			 */
			LOGGER.log(Level.SEVERE, "Error while loading the POS tagging model or processing tokens", e);
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
					LOGGER.log(Level.WARNING, "Error closing the POS tagging model input stream", e);
				}
			}
		}

		// Indicar que el proceso ha finalizado.
		System.out.println("done");
	}
}
