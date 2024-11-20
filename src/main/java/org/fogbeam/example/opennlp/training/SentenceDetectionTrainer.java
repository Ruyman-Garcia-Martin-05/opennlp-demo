package org.fogbeam.example.opennlp.training;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

/**
 * @class SentenceDetectionTrainer
 * @brief Clase para entrenar un modelo de detección de oraciones con OpenNLP.
 *
 * Utiliza datos de entrenamiento para generar un modelo de detección de oraciones,
 * que identifica los límites entre las oraciones en un texto.
 */
public class SentenceDetectionTrainer {

	/**
	 * Método principal que realiza el entrenamiento del modelo de detección de oraciones.
	 *
	 * Este método utiliza un archivo de datos de entrenamiento para crear un modelo
	 * preentrenado que puede ser utilizado para detectar los límites de oraciones en textos.
	 *
	 * @param args Argumentos de la línea de comandos (no se usan).
	 * @throws Exception Si ocurre algún error durante la ejecución del programa.
	 */
	public static void main(String[] args) throws Exception {
		// Definir el conjunto de caracteres para la codificación.
		Charset charset = Charset.forName("UTF-8");

		// Crear un flujo de entrada para leer las líneas del archivo de entrenamiento.
		ObjectStream<String> lineStream = new PlainTextByLineStream(
				new FileInputStream("training_data/en-sent.train"), charset);

		// Crear un flujo de muestras para el entrenamiento de detección de oraciones.
		ObjectStream<SentenceSample> sampleStream = new SentenceSampleStream(lineStream);

		SentenceModel model;

		try {
			// Entrenar el modelo de detección de oraciones.
			model = SentenceDetectorME.train("en", sampleStream, true, null, TrainingParameters.defaultParams());
		} finally {
			// Cerrar el flujo de muestras.
			sampleStream.close();
		}

		// Definir el flujo de salida para guardar el modelo entrenado.
		OutputStream modelOut = null;
		File modelFile = new File("models/en-sent.model");

		try {
			// Escribir el modelo entrenado en el archivo.
			modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
			model.serialize(modelOut);
		} finally {
			// Cerrar el flujo de salida si está abierto.
			if (modelOut != null) {
				modelOut.close();
			}
		}

		// Indicar que el proceso ha finalizado.
		System.out.println("done");
	}
}
