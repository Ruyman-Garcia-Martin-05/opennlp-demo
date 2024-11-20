package org.fogbeam.example.opennlp.training;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.WordTagSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class PartOfSpeechTaggerTrainer {

	private static final Logger LOGGER = Logger.getLogger(PartOfSpeechTaggerTrainer.class.getName());

	public static void main(String[] args) {
		POSModel model = null;
		String trainingDataPath = "training_data/en-pos.train";
		String modelFilePath = "models/en-pos.model";

		ObjectStream<String> lineStream = null;
		ObjectStream<POSSample> sampleStream = null;
		try {
			lineStream = new PlainTextByLineStream(new InputStreamReader(new FileInputStream(trainingDataPath), Charset.forName("UTF-8")));
			sampleStream = new WordTagSampleStream(lineStream);

			LOGGER.info("Entrenando el modelo de etiquetado de partes del discurso...");
			model = POSTaggerME.train("en", sampleStream,
					TrainingParameters.defaultParams(), null, null);

			LOGGER.info("Modelo entrenado exitosamente.");

		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error al leer los datos de entrenamiento o entrenar el modelo", e);
		} finally {
			if (sampleStream != null) {
				try {
					sampleStream.close();
				} catch (IOException e) {
					LOGGER.log(Level.SEVERE, "Error closing sampleStream", e);
				}
			}
			if (lineStream != null) {
				try {
					lineStream.close();
				} catch (IOException e) {
					LOGGER.log(Level.SEVERE, "Error closing lineStream", e);
				}
			}
		}

		if (model != null) {
			try (BufferedOutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelFilePath))) {
				LOGGER.info("Guardando el modelo entrenado en: " + modelFilePath);
				model.serialize(modelOut);
				LOGGER.info("Modelo guardado exitosamente.");
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Error al guardar el modelo entrenado", e);
			}
		} else {
			LOGGER.warning("El modelo no fue entrenado. Verifica los datos de entrada y el proceso de entrenamiento.");
		}

		LOGGER.info("Proceso finalizado.");
	}
}