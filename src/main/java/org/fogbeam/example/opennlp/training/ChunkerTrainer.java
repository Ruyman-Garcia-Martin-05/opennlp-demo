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

/**
 * @class ChunkerTrainer
 * @brief Clase para entrenar un modelo de chunking con OpenNLP.
 *
 * Utiliza datos de entrenamiento para generar un modelo que puede identificar
 * estructuras gramaticales (chunks) en un texto.
 */
public class ChunkerTrainer {

	/** Logger para manejar mensajes de error y depuración. */
	private static final Logger LOGGER = Logger.getLogger(ChunkerTrainer.class.getName());

	/**
	 * Método principal que realiza el entrenamiento del modelo de chunking.
	 *
	 * Este método utiliza un archivo de datos de entrenamiento para crear un modelo
	 * preentrenado que puede identificar chunks gramaticales en texto.
	 *
	 * @param args Argumentos de la línea de comandos (no se usan).
	 */
	public static void main(String[] args) {
		Charset charset = Charset.forName("UTF-8");
		String trainingDataPath = "training_data/conll2000-chunker.train";
		String modelFilePath = "models/en-chunker.model";

		ObjectStream<ChunkSample> sampleStream = null;
		OutputStream modelOut = null;

		try {
			// Cargar datos de entrenamiento desde el archivo.
			LOGGER.info("Cargando datos de entrenamiento desde: " + trainingDataPath);
			ObjectStream<String> lineStream = new PlainTextByLineStream(
					new FileInputStream(trainingDataPath), charset);

			sampleStream = new ChunkSampleStream(lineStream);

			// Entrenar el modelo de chunking.
			LOGGER.info("Entrenando el modelo...");
			ChunkerModel model = ChunkerME.train("en", sampleStream,
					new DefaultChunkerContextGenerator(),
					TrainingParameters.defaultParams());

			// Guardar el modelo entrenado en un archivo.
			LOGGER.info("Guardando el modelo en: " + modelFilePath);
			modelOut = new BufferedOutputStream(new FileOutputStream(modelFilePath));
			model.serialize(modelOut);

			LOGGER.info("Entrenamiento completado con éxito.");

		} catch (IOException e) {
			/**
			 * Manejo de errores durante la carga de datos, el entrenamiento o el guardado del modelo.
			 * @exception IOException Si ocurre un error al leer, entrenar o guardar el modelo.
			 */
			LOGGER.log(Level.SEVERE, "Error durante el entrenamiento o guardado del modelo de chunking", e);
		} finally {
			// Cerrar el flujo de datos de entrenamiento.
			if (sampleStream != null) {
				try {
					sampleStream.close();
				} catch (IOException e) {
					/**
					 * Manejo de errores al cerrar el flujo de datos de entrenamiento.
					 * @exception IOException Si ocurre un error al cerrar el flujo.
					 */
					LOGGER.log(Level.WARNING, "Error al cerrar el flujo de datos de entrenamiento", e);
				}
			}

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
	}
}
