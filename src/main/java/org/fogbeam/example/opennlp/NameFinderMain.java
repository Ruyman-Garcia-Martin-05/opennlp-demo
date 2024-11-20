package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

public class NameFinderMain {

	// Logger para manejar mensajes y errores
	private static final Logger LOGGER = Logger.getLogger(NameFinderMain.class.getName());

	public static void main(String[] args) {
		InputStream modelIn = null;

		try {
			// Cargar el modelo de reconocimiento de nombres
			modelIn = new FileInputStream("models/en-ner-person.model");
			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);

			NameFinderME nameFinder = new NameFinderME(model);

			// Tokens para analizar
			String[] tokens = { "Phillip", "Rhodes", "is", "presenting", "at", "some", "meeting", "." };

			// Encontrar nombres en los tokens
			Span[] names = nameFinder.find(tokens);

			for (Span ns : names) {
				System.out.println("Found name: " + ns.toString());

				// Si necesitas construir el nombre real a partir de los tokens
				StringBuilder name = new StringBuilder();
				for (int i = ns.getStart(); i < ns.getEnd(); i++) {
					name.append(tokens[i]).append(" ");
				}
				System.out.println("The name is: " + name.toString().trim());
			}

			// Limpiar los datos adaptativos
			nameFinder.clearAdaptiveData();

		} catch (IOException e) {
			// Registrar el error con un nivel SEVERE
			LOGGER.log(Level.SEVERE, "Error while loading the name finder model or processing tokens", e);
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
					// Registrar advertencia si no se puede cerrar el recurso
					LOGGER.log(Level.WARNING, "Error closing the model input stream", e);
				}
			}
		}

		System.out.println("done");
	}
}
