package org.fogbeam.example.opennlp.training;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

/**
 * @class DocumentClassifierTrainer
 * @brief Clase para entrenar un modelo de clasificación de documentos con OpenNLP.
 *
 * Utiliza datos de entrenamiento para generar un modelo que puede clasificar
 * texto en categorías predefinidas.
 */
public class DocumentClassifierTrainer {

	/** Logger para manejar mensajes de error y depuración. */
	private static final Logger LOGGER = Logger.getLogger(DocumentClassifierTrainer.class.getName());

	/**
	 * Método principal que realiza el entrenamiento del modelo de clasificación de documentos.
	 *
	 * Este método utiliza un archivo de datos de entrenamiento para crear un modelo
	 * preentrenado que puede ser utilizado para clasificar texto en categorías.
	 *
	 * @param args Argumentos de la línea de comandos (no se usan).
	 */
	public static void main(String[] args) {
		DoccatModel model = null;
		InputStream dataIn = null;
		OutputStream modelOut = null;
		String trainingDataPath = "training_data/en-doccat.train";
		String modelFilePath = "models/en-doccat.model";

		try {
			// Cargar datos de entrenamiento desde el archivo.
			LOGGER.info("Cargando datos de entrenamiento desde: " + trainingDataPath);
			dataIn = new FileInputStream(trainingDataPath);

			// Crear un flujo de muestras para el entrenamiento de clasificación de documentos.
			ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
			ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

			// Entrenar el modelo de clasificación de documentos.
			LOGGER.info("Entrenando el modelo de clasificación de documentos...");
			model = DocumentCategorizerME.train("en", sampleStream);

		} catch (IOException e) {
			/**
			 * Manejo de errores durante la carga de datos o el entrenamiento.
			 * @exception IOException Si ocurre un error al leer o procesar los datos de entrenamiento.
			 */
			LOGGER.log(Level.SEVERE, "Error al leer o procesar los datos de entrenamiento", e);
		} finally {
			// Cerrar el flujo de datos de entrenamiento.
			if (dataIn != null) {
				try {
					dataIn.close();
				} catch (IOException e) {
					/**
					 * Manejo de errores al cerrar el flujo de datos de entrenamiento.
					 * @exception IOException Si ocurre un error al cerrar el flujo.
					 */
					LOGGER.log(Level.WARNING, "Error al cerrar el flujo de datos de entrenamiento", e);
				}
			}
		}

		if (model != null) {
			try {
				// Guardar el modelo entrenado en un archivo.
				LOGGER.info("Guardando el modelo entrenado en: " + modelFilePath);
				modelOut = new BufferedOutputStream(new FileOutputStream(modelFilePath));
				model.serialize(modelOut);
				LOGGER.info("Modelo guardado exitosamente.");
			} catch (IOException e) {
				/**
				 * Manejo de errores al guardar el modelo entrenado.
				 * @exception IOException Si ocurre un error al guardar el modelo.
				 */
				LOGGER.log(Level.SEVERE, "Error al guardar el modelo entrenado", e);
			} finally {
				// Cerrar el flujo de salida del modelo.
				if (modelOut != null) {
					try {
						modelOut.close();
					} catch (IOException e) {
						/**
						 * Manejo de errores al cerrar el flujo de salida del modelo.
						 * @exception IOException Si ocurre un error al cerrar el flujo.
						 */
						LOGGER.log(Level.WARNING, "Error al cerrar el flujo de salida del modelo", e);
					}
				}
			}
		} else {
			// Advertencia si el modelo no fue entrenado.
			LOGGER.warning("El modelo no fue entrenado. Verifica los datos de entrada y el proceso de entrenamiento.");
		}

		// Indicar que el proceso ha finalizado.
		LOGGER.info("Proceso finalizado.");
	}
}
