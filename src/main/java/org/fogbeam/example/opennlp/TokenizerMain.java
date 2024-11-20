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
		// Directorio de entrada y salida
		String inputDir = "input_texts";
		String outputDir = "output_texts";
		String outputFilePath = outputDir + "/output_tokens.txt";

		// Obtener archivos de entrada
		List<Path> inputFiles = getFilesFromDirectory(inputDir);

		// Inicializar el modelo de tokenización
		Tokenizer tokenizer = initializeTokenizer(modelPath);

		// Crear el directorio de salida si no existe
		Files.createDirectories(Paths.get(outputDir));

		// Abrir archivo de salida
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
			for (Path file : inputFiles) {
				System.out.println("Procesando archivo: " + file.getFileName());

				// Leer el contenido del archivo
				String content = readFileContent(file);

				// Tokenizar el contenido
				String[] tokens = tokenizer.tokenize(content);

				// Escribir tokens en el archivo de salida
				for (String token : tokens) {
					writer.write(token);
					writer.newLine();
				}
				writer.newLine(); // Separar los tokens de diferentes archivos
			}
		}

		System.out.println("Archivo de tokens generado en: " + outputFilePath);
	}

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

	/**
	 * Lee el contenido de un archivo de texto como un String.
	 *
	 * @param file Ruta del archivo.
	 * @return Contenido del archivo.
	 * @throws IOException Si ocurre un error al leer el archivo.
	 */
	private static String readFileContent(Path file) throws IOException {
		StringBuilder content = new StringBuilder();
		try (BufferedReader reader = Files.newBufferedReader(file)) {
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line).append(System.lineSeparator());
			}
		}
		return content.toString();
	}
}
