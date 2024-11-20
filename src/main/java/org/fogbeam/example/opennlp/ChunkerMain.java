package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;

/**
 * @class ChunkerMain
 * @brief Clase principal para demostrar el uso del modelo de chunking de OpenNLP.
 *
 * Esta clase utiliza el modelo de chunking para analizar una oración
 * y asignar etiquetas de chunks (trozos) a las palabras basándose en las partes
 * del discurso (POS tags).
 */
public class ChunkerMain {

	/** Logger para registrar mensajes y errores. */
	private static final Logger LOGGER = Logger.getLogger(ChunkerMain.class.getName());

	/**
	 * Método principal que carga el modelo de chunking, analiza una oración y
	 * muestra los resultados.
	 *
	 * @param args Argumentos de la línea de comandos (no se usan).
	 * @throws Exception En caso de que ocurra un error durante la ejecución.
	 */
	public static void main(String[] args) throws Exception {
		InputStream modelIn = null;
		ChunkerModel model = null;

		try {
			// Cargar el modelo de chunking desde el archivo.
			modelIn = new FileInputStream("models/en-chunker.model");
			model = new ChunkerModel(modelIn);

			// Crear un objeto ChunkerME para realizar el chunking.
			ChunkerME chunker = new ChunkerME(model);

			// Tokens de la oración a analizar.
			String sent[] = new String[] { "Rockwell", "International", "Corp.", "'s",
					"Tulsa", "unit", "said", "it", "signed", "a", "tentative", "agreement",
					"extending", "its", "contract", "with", "Boeing", "Co.", "to",
					"provide", "structural", "parts", "for", "Boeing", "'s", "747",
					"jetliners", "." };

			// Etiquetas de partes del discurso (POS tags) correspondientes a los tokens.
			String pos[] = new String[] { "NNP", "NNP", "NNP", "POS", "NNP", "NN",
					"VBD", "PRP", "VBD", "DT", "JJ", "NN", "VBG", "PRP$", "NN", "IN",
					"NNP", "NNP", "TO", "VB", "JJ", "NNS", "IN", "NNP", "POS", "CD", "NNS",
					"." };

			// Realizar el chunking y obtener las etiquetas de chunks.
			String tag[] = chunker.chunk(sent, pos);

			// Obtener las probabilidades asociadas a cada chunk.
			double probs[] = chunker.probs();

			/**
			 * Las etiquetas de chunks incluyen tipos como:
			 * - I-NP: Palabras dentro de una frase nominal.
			 * - I-VP: Palabras dentro de una frase verbal.
			 * - B-CHUNK: Primera palabra de un chunk.
			 * - I-CHUNK: Palabras subsecuentes dentro de un chunk.
			 */

			// Imprimir los resultados.
			for (int i = 0; i < sent.length; i++) {
				System.out.println("Token [" + sent[i] + "] has chunk tag [" + tag[i] + "] with probability = "
						+ probs[i]);
			}

		} catch (IOException e) {
			// Registrar el error usando el logger.
			LOGGER.log(Level.SEVERE, "Error loading the model or processing chunks", e);
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
					// Registrar advertencia si no se puede cerrar el modelo.
					LOGGER.log(Level.WARNING, "Error closing the model input stream", e);
				}
			}
		}

		// Indicar que el proceso ha finalizado.
		System.out.println("done");
	}
}
