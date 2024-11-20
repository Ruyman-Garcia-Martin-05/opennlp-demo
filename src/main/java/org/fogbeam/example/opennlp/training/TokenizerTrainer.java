package org.fogbeam.example.opennlp.training;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import opennlp.tools.tokenize.TokenSample;
import opennlp.tools.tokenize.TokenSampleStream;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

/**
 * @class TokenizerTrainer
 * @brief Clase para entrenar un modelo de tokenización con OpenNLP.
 *
 * Utiliza datos de entrenamiento para generar un modelo de tokenización que puede dividir texto en palabras o tokens.
 */
public class TokenizerTrainer {

	/**
	 * Método principal que realiza el entrenamiento del modelo de tokenización.
	 *
	 * Este método utiliza un archivo de datos de entrenamiento para crear un modelo
	 * preentrenado que puede ser utilizado para dividir texto en tokens.
	 *
	 * @param args Argumentos de la línea de comandos (no se usan).
	 * @throws Exception Si ocurre algún error durante la ejecución del programa.
	 */
	public static void main(String[] args) throws Exception {
		// Configurar el conjunto de caracteres para el archivo de entrenamiento.
		Charset charset = Charset.forName("UTF-8");

		// Crear un flujo de entrada para leer las líneas del archivo de entrenamiento.
		ObjectStream<String> lineStream = new PlainTextByLineStream(
				new FileInputStream("training_data/en-token.train"), charset);

		// Crear un flujo de muestras para el entrenamiento de tokenización.
		ObjectStream<TokenSample> sampleStream = new TokenSampleStream(lineStream);

		TokenizerModel model;

		try {
			// Entrenar el modelo de tokenización.
			model = TokenizerME.train("en", sampleStream, true, TrainingParameters.defaultParams());
		} finally {
			// Cerrar el flujo de muestras.
			sampleStream.close();
		}

		// Definir el flujo de salida para guardar el modelo entrenado.
		OutputStream modelOut = null;
		try {
			// Escribir el modelo entrenado en el archivo.
			modelOut = new BufferedOutputStream(new FileOutputStream("models/en-token.model"));
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
