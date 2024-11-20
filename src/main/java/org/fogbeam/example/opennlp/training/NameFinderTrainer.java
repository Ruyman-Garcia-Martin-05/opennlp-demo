package org.fogbeam.example.opennlp.training;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Collections;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

/**
 * @class NameFinderTrainer
 * @brief Clase para entrenar un modelo de reconocimiento de nombres con OpenNLP.
 *
 * Utiliza datos de entrenamiento para generar un modelo que puede reconocer nombres
 * propios (entidades nombradas) en texto.
 *
 * @note Los datos de entrenamiento deben contener al menos 15,000 oraciones para
 * generar un modelo con buen desempeño.
 */
public class NameFinderTrainer {

	/**
	 * Método principal que realiza el entrenamiento del modelo de reconocimiento de nombres.
	 *
	 * Este método utiliza un archivo de datos de entrenamiento para crear un modelo
	 * preentrenado que puede ser utilizado para reconocer entidades nombradas, como nombres de personas.
	 *
	 * @param args Argumentos de la línea de comandos (no se usan).
	 * @throws Exception Si ocurre algún error durante la ejecución del programa.
	 */
	public static void main(String[] args) throws Exception {
		// Definir el conjunto de caracteres para la codificación.
		Charset charset = Charset.forName("UTF-8");

		// Crear un flujo de entrada para leer las líneas del archivo de entrenamiento.
		ObjectStream<String> lineStream = new PlainTextByLineStream(
				new FileInputStream("training_data/en-ner-person.train"), charset);

		// Crear un flujo de muestras para el entrenamiento de reconocimiento de nombres.
		ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);

		TokenNameFinderModel model;

		try {
			// Entrenar el modelo de reconocimiento de nombres.
			model = NameFinderME.train("en", "person", sampleStream,
					TrainingParameters.defaultParams(), (byte[]) null,
					Collections.<String, Object>emptyMap());
		} finally {
			// Cerrar el flujo de muestras.
			sampleStream.close();
		}

		// Definir el flujo de salida para guardar el modelo entrenado.
		BufferedOutputStream modelOut = null;
		try {
			String modelFile = "models/en-ner-person.model";
			modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
			// Escribir el modelo entrenado en el archivo.
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
