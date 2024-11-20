package org.fogbeam.example.opennlp.training;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.chunker.ChunkSample;
import opennlp.tools.chunker.ChunkSampleStream;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.chunker.DefaultChunkerContextGenerator;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class ChunkerTrainer {

	// Logger para manejar mensajes y errores
	private static final Logger LOGGER = Logger.getLogger(ChunkerTrainer.class.getName());

	public static void main(String[] args) {
		Charset charset = Charset.forName("UTF-8");
		String trainingDataPath = "training_data/conll2000-chunker.train";
		String modelFilePath = "models/en-chunker.model";

		ObjectStream<ChunkSample> sampleStream = null;
		OutputStream modelOut = null;

		try {
			// Cargar datos de entrenamiento
			LOGGER.info("Cargando datos de entrenamiento desde: " + trainingDataPath);
			ObjectStream<String> lineStream = new PlainTextByLineStream(
					new FileInputStream(trainingDataPath), charset);

			sampleStream = new ChunkSampleStream(lineStream);

			// Entrenar el modelo
			LOGGER.info("Entrenando el modelo...");
			ChunkerModel model = ChunkerME.train("en", sampleStream,
					new DefaultChunkerContextGenerator(),
					TrainingParameters.defaultParams());

			// Guardar el modelo entrenado
			LOGGER.info("Guardando el modelo en: " + modelFilePath);
			modelOut = new BufferedOutputStream(new FileOutputStream(modelFilePath));
			model.serialize(modelOut);

			LOGGER.info("Entrenamiento completado con éxito.");

		} catch (IOException e) {
			// Registrar errores críticos durante la carga, entrenamiento o guardado del modelo
			LOGGER.log(Level.SEVERE, "Error durante el entrenamiento o guardado del modelo de chunking", e);
		} finally {
			// Cerrar el flujo de datos de entrenamiento
			if (sampleStream != null) {
				try {
					sampleStream.close();
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "Error al cerrar el flujo de datos de entrenamiento", e);
				}
			}

			// Cerrar el flujo de salida del modelo
			if (modelOut != null) {
				try {
					modelOut.close();
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "Error al cerrar el flujo de salida del modelo", e);
				}
			}
		}
	}
}
