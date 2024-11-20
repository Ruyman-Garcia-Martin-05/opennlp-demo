package org.fogbeam.example.opennlp;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class TokenizerMain {

	public static void main(String[] args) throws Exception {
		// Directorio de entrada
		String inputDir = "input_texts";

		// Obtener archivos de entrada
		List<Path> inputFiles = getFilesFromDirectory(inputDir);

		// Imprimir los nombres de los archivos leídos
		for (Path file : inputFiles) {
			System.out.println("Archivo encontrado: " + file.getFileName());
		}

		System.out.println("Lectura de archivos completada.");
	}

	/**
	 * Obtiene todos los archivos de texto desde un directorio específico.
	 *
	 * @param directoryPath Ruta del directorio.
	 * @return Lista de rutas de archivos.
	 * @throws IOException Si ocurre un error al acceder al directorio.
	 */
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
