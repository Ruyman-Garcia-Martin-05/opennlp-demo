package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceDetectionMain {

	// Logger para manejar mensajes y errores
	private static final Logger LOGGER = Logger.getLogger(SentenceDetectionMain.class.getName());

	public static void main(String[] args) {
		InputStream modelIn = null;
		InputStream demoDataIn = null;

		try {
			// Cargar los archivos del modelo y los datos de demo
			modelIn = new FileInputStream("models/en-sent.model");
			demoDataIn = new FileInputStream("demo_data/en-sent1.demo");

			SentenceModel model = new SentenceModel(modelIn);
			SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);

			// Convertir el contenido del archivo de demo a String
			String demoData = convertStreamToString(demoDataIn);

			// Detectar oraciones en el texto de demo
			String sentences[] = sentenceDetector.sentDetect(demoData);

			// Mostrar las oraciones detectadas
			for (String sentence : sentences) {
				System.out.println(sentence + "\n");
			}

		} catch (IOException e) {
			// Registrar el error con un nivel SEVERE
			LOGGER.log(Level.SEVERE, "Error while loading the sentence detection model or processing the demo data", e);
		} finally {
			// Cerrar el flujo del modelo
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
					// Registrar advertencia si no se puede cerrar el flujo
					LOGGER.log(Level.WARNING, "Error closing the sentence detection model input stream", e);
				}
			}

			// Cerrar el flujo de datos de demo
			if (demoDataIn != null) {
				try {
					demoDataIn.close();
				} catch (IOException e) {
					// Registrar advertencia si no se puede cerrar el flujo
					LOGGER.log(Level.WARNING, "Error closing the demo data input stream", e);
				}
			}
		}

		System.out.println("done");
	}

	/**
	 * Convierte un InputStream en un String.
	 *
	 * @param is InputStream a convertir.
	 * @return El contenido del InputStream como String.
	 */
	static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
}
