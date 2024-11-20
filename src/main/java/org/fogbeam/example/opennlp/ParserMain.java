package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

public class ParserMain {

	// Logger para manejar mensajes y errores
	private static final Logger LOGGER = Logger.getLogger(ParserMain.class.getName());

	public static void main(String[] args) {
		InputStream modelIn = null;

		try {
			// Cargar el modelo del parser
			modelIn = new FileInputStream("models/en-parser-chunking.bin");
			ParserModel model = new ParserModel(modelIn);

			Parser parser = ParserFactory.create(model);

			// Texto a analizar
			String sentence = "The quick brown fox jumps over the lazy dog .";

			// Parsear la oración
			Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

			// Obtener y mostrar el primer parseo
			Parse parse = topParses[0];
			System.out.println(parse.toString());

			// Mostrar el árbol de código
			parse.showCodeTree();

		} catch (IOException e) {
			// Registrar el error con un nivel SEVERE
			LOGGER.log(Level.SEVERE, "Error while loading the parser model or processing the sentence", e);
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
					// Registrar advertencia si no se puede cerrar el recurso
					LOGGER.log(Level.WARNING, "Error closing the parser model input stream", e);
				}
			}
		}

		System.out.println("done");
	}
}
