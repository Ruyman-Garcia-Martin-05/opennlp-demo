package org.fogbeam.example.opennlp;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TokenizerMain {

	// Logger para manejar mensajes y errores
	private static final Logger LOGGER = Logger.getLogger(TokenizerMain.class.getName());

	public static void main(String[] args) {
		// Ruta del modelo
		String modelPath = "models/en-token.model";
		// Directorio de entrada y salida
		String inputDir = "input_texts";
		String outputDir = "output_texts";
		String outputFilePath = outputDir + "/output_tokens.txt";

		try {
			// Obtener archivos de entrada
			List<Path> inputFiles = getFilesFromDirectory(inputDir);

			// Inicializar el modelo de tokenización
			Tokenizer tokenizer = initializeTokenizer(modelPath);

			// Crear el directorio de salida si no existe
			Files.createDirectories(Paths.get(outputDir));

			// Abrir archivo de salida
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
				for (Path file : inputFiles) {
					LOGGER.info("Procesando archivo: " + file.getFileName());

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

			LOGGER.info("Archivo de tokens generado en: " + outputFilePath);

		} catch (IOException e) {
			// Registrar el error con un nivel SEVERE
			LOGGER.log(Level.SEVERE, "Error durante el procesamiento de tokenización", e);
		}
	}

	private static Tokenizer initializeTokenizer(String modelPath) throws IOException {
		try (InputStream modelIn = new FileInputStream(modelPath)) {
			TokenizerModel model = new TokenizerModel(modelIn);
			return new TokenizerME(model);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error al cargar el modelo de tokenización", e);
			throw e;
		}
	}

	private static List<Path> getFilesFromDirectory(String directoryPath) throws IOException {
		List<Path> files = new ArrayList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directoryPath), "*.txt")) {
			for (Path entry : stream) {
				files.add(entry);
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error al leer el directorio de entrada: " + directoryPath, e);
			throw e;
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
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error al leer el archivo: " + file.getFileName(), e);
			throw e;
		}
		return content.toString();
	}
}
