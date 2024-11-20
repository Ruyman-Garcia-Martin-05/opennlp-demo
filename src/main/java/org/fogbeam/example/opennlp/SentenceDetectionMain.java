package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/**
 * @class SentenceDetectionMain
 * @brief Clase principal para detectar oraciones en un texto con OpenNLP.
 *
 * Esta clase utiliza un modelo preentrenado de detección de oraciones para
 * procesar un archivo de texto y extraer las oraciones contenidas en él.
 */
public class SentenceDetectionMain {

	/** Logger para manejar mensajes de error y depuración. */
	private static final Logger LOGGER = Logger.getLogger(SentenceDetectionMain.class.getName());

	/**
	 * Método principal que carga un modelo de detección de oraciones,
	 * procesa un archivo de texto de demo y muestra las oraciones detectadas.
	 *
	 * @param args Argumentos de la línea de comandos (no se usan).
	 */
	public static void main(String[] args) {
		InputStream modelIn = null;
		InputStream demoDataIn = null;

		try {
			// Cargar los archivos del modelo y los datos de demo.
			modelIn = new FileInputStream("models/en-sent.model");
			demoDataIn = new FileInputStream("demo_data/en-sent1.demo");

			// Crear un modelo de detección de oraciones.
			SentenceModel model = new SentenceModel(modelIn);
			SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);

			// Convertir el contenido del archivo de demo a un String.
			String demoData = convertStreamToString(demoDataIn);

			// Detectar oraciones en el texto de demo.
			String sentences[] = sentenceDetector.sentDetect(demoData);

			// Mostrar las oraciones detectadas.
			for (String sentence : sentences) {
				System.out.println(sentence + "\n");
			}

		} catch (IOException e) {
			/**
			 * Manejo de errores durante la carga del modelo o el procesamiento del archivo de demo.
			 * @exception IOException Si ocurre un error al cargar el modelo o procesar los datos de entrada.
			 */
			LOGGER.log(Level.SEVERE, "Error while loading the sentence detection model or processing the demo data", e);
		} finally {
			// Cerrar el flujo del modelo.
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
					/**
					 * Manejo de errores al cerrar el flujo de entrada del modelo.
					 * @exception IOException Si ocurre un error al cerrar el flujo del modelo.
					 */
					LOGGER.log(Level.WARNING, "Error closing the sentence detection model input stream", e);
				}
			}

			// Cerrar el flujo de datos de demo.
			if (demoDataIn != null) {
				try {
					demoDataIn.close();
				} catch (IOException e) {
					/**
					 * Manejo de errores al cerrar el flujo de datos de demo.
					 * @exception IOException Si ocurre un error al cerrar el flujo de demo.
					 */
					LOGGER.log(Level.WARNING, "Error closing the demo data input stream", e);
				}
			}
		}

		// Indicar que el proceso ha finalizado.
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
