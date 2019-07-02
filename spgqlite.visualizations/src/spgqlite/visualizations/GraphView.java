package spgqlite.visualizations;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import io.github.spencerpark.ijava.runtime.Display;
import spgqlite.graph.Graph;

public class GraphView {

	public static void show(Graph graph) {
		try {
			// work around for unresolved security policies: https://stackoverflow.com/a/52338192/475329
			// https://stackoverflow.com/questions/8117761/how-can-i-make-an-iframe-resizable
			Display.display("<html><iframe src='index.html' width=\"100%\", height=\"600\" frameBorder=\"0\"></iframe></html>", "text/html");
//			Display.display(readResource("/templates/index.html"), "text/html");
		} catch (Throwable t) {
			Display.display("<html><body><h1>Error could not display graph</h1><h2>" + t.getMessage() + "</h2></body></html>", "text/html");
		}
	}
	
	private static String readResource(String path) throws IOException {
		InputStream inputStream = GraphView.class.getResourceAsStream(path); 
		return convertStreamToString(inputStream);
	}
	
	private static String convertStreamToString(InputStream is) throws IOException {
	   StringBuilder sb = new StringBuilder(2048);
	   char[] read = new char[128];
	   try (InputStreamReader ir = new InputStreamReader(is, StandardCharsets.UTF_8)) {
	     for (int i; -1 != (i = ir.read(read)); sb.append(read, 0, i));
	   }
	   return sb.toString();
	}
	
}
