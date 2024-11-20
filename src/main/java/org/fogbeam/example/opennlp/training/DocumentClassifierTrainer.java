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

public class DocumentClassifierTrainer {

	// Logger para manejar mensajes y errores
	private static final Logger LOGGER = Logger.getLogger(DocumentClassifierTrainer.class.getName());

	public static void main(String[] args) {
		DoccatModel model = null;
		InputStream dataIn = null;
		OutputStream modelOut = null;
		String trainingDataPath = "training_data/en-doccat.train";
		String modelFilePath = "models/en-doccat.model";

		try {
			// Cargar datos de entrenamiento
			LOGGER.info("Cargando datos de entrenamiento desde: " + trainingDataPath);
			dataIn = new FileInputStream(trainingDataPath);

			ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
			ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

			// Entrenar el modelo
			LOGGER.info("Entrenando el modelo de clasificación de documentos...");
			model = DocumentCategorizerME.train("en", sampleStream);

		} catch (IOException e) {
			// Registrar el error durante la carga de datos o el entrenamiento
			LOGGER.log(Level.SEVERE, "Error al leer o procesar los datos de entrenamiento", e);
		} finally {
			if (dataIn != null) {
				try {
					dataIn.close();
				} catch (IOException e) {
					// Registrar advertencia si no se puede cerrar el recurso
					LOGGER.log(Level.WARNING, "Error al cerrar el flujo de datos de entrenamiento", e);
				}
			}
		}

		if (model != null) {
			try {
				// Guardar el modelo entrenado
				LOGGER.info("Guardando el modelo entrenado en: " + modelFilePath);
				modelOut = new BufferedOutputStream(new FileOutputStream(modelFilePath));
				model.serialize(modelOut);
				LOGGER.info("Modelo guardado exitosamente.");
			} catch (IOException e) {
				// Registrar el error al guardar el modelo
				LOGGER.log(Level.SEVERE, "Error al guardar el modelo entrenado", e);
			} finally {
				if (modelOut != null) {
					try {
						modelOut.close();
					} catch (IOException e) {
						// Registrar advertencia si no se puede cerrar el recurso
						LOGGER.log(Level.WARNING, "Error al cerrar el flujo de salida del modelo", e);
					}
				}
			}
		} else {
			LOGGER.warning("El modelo no fue entrenado. Verifica los datos de entrada y el proceso de entrenamiento.");
		}

		LOGGER.info("Proceso finalizado.");
	}
}
