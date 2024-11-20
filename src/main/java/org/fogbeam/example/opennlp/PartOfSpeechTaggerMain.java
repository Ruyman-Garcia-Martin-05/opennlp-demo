package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class PartOfSpeechTaggerMain {

	// Logger para manejar mensajes y errores
	private static final Logger LOGGER = Logger.getLogger(PartOfSpeechTaggerMain.class.getName());

	public static void main(String[] args) {
		InputStream modelIn = null;

		try {
			// Cargar el modelo de etiquetado de partes del discurso
			modelIn = new FileInputStream("models/en-pos-maxent.bin");
			POSModel model = new POSModel(modelIn);

			POSTaggerME tagger = new POSTaggerME(model);

			// Tokens a analizar
			String sent[] = new String[]{"Most", "large", "cities", "in", "the", "US", "had",
					"morning", "and", "afternoon", "newspapers", "."};

			// Etiquetar los tokens
			String tags[] = tagger.tag(sent);
			double probs[] = tagger.probs();

			// Mostrar los resultados
			for (int i = 0; i < sent.length; i++) {
				System.out.println("Token [" + sent[i] + "] has POS [" + tags[i] + "] with probability = " + probs[i]);
			}

		} catch (IOException e) {
			// Registrar el error con un nivel SEVERE
			LOGGER.log(Level.SEVERE, "Error while loading the POS tagging model or processing tokens", e);
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
					// Registrar advertencia si no se puede cerrar el recurso
					LOGGER.log(Level.WARNING, "Error closing the POS tagging model input stream", e);
				}
			}
		}

		System.out.println("done");
	}
}
