package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;

public class DocumentClassifierMain {

	// Logger para manejar mensajes de error y depuración
	private static final Logger LOGGER = Logger.getLogger(DocumentClassifierMain.class.getName());

	public static void main(String[] args) {
		InputStream is = null;

		try {
			// Cargar el modelo desde el archivo
			is = new FileInputStream("models/en-doccat.model");
			DoccatModel m = new DoccatModel(is);

			// Texto a clasificar
			String inputText = "What happens if we have declining bottom-line revenue?";
			DocumentCategorizerME myCategorizer = new DocumentCategorizerME(m);

			// Clasificar el texto
			double[] outcomes = myCategorizer.categorize(inputText);
			String category = myCategorizer.getBestCategory(outcomes);

			// Imprimir la categoría clasificada
			System.out.println("Input classified as: " + category);

		} catch (IOException e) {
			// Registrar el error de entrada/salida con un nivel SEVERE
			LOGGER.log(Level.SEVERE, "Error while loading the model or classifying the input text", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// Registrar advertencia si no se puede cerrar el recurso
					LOGGER.log(Level.WARNING, "Error closing the model input stream", e);
				}
			}
		}

		System.out.println("done");
	}
}
