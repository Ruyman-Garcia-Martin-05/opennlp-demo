package org.fogbeam.example.opennlp;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class TokenizerMain {

	public static void main(String[] args) throws Exception {
		// Ruta del modelo
		String modelPath = "models/en-token.model";
		// Directorio de entrada
		String inputDir = "input_texts";

		// Obtener archivos de entrada
		List<Path> inputFiles = getFilesFromDirectory(inputDir);

		// Inicializar el modelo de tokenización
		Tokenizer tokenizer = initializeTokenizer(modelPath);

		// Procesar y mostrar tokens de cada archivo
		for (Path file : inputFiles) {
			System.out.println("Procesando archivo: " + file.getFileName());

			// Leer el contenido del archivo
			String content = Files.readString(file);

			// Tokenizar el contenido
			String[] tokens = tokenizer.tokenize(content);

			// Mostrar tokens por consola
			for (String token : tokens) {
				System.out.println(token);
			}
		}

		System.out.println("Tokenización completada.");
	}

	/**
	 * Inicializa el tokenizador con el modelo especificado.
	 *
	 * @param modelPath Ruta del modelo.
	 * @return Instancia de Tokenizer.
	 * @throws IOException Si ocurre un error al cargar el modelo.
	 */
	private static Tokenizer initializeTokenizer(String modelPath) throws IOException {
		try (InputStream modelIn = new FileInputStream(modelPath)) {
			TokenizerModel model = new TokenizerModel(modelIn);
			return new TokenizerME(model);
		}
	}

	private static List<Path> getFilesFromDirectory(String directoryPath) throws IOException {
		List<Path> files = new ArrayList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directoryPath), "*.txt")) {
			for (Path entry : stream) {
				files.add(entry);
			}
		}
		return files;
	}
}
