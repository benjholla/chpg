package chpg.visualizations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import chpg.graph.Edge;
import chpg.graph.Graph;
import chpg.graph.GraphElementSet;
import chpg.graph.Node;
import chpg.graph.schema.SchemaGraph;
import io.github.spencerpark.ijava.runtime.Display;

public class GraphView {

	private static boolean debug = false;

	public static void setDebug(boolean enabled) {
		debug = enabled;
	}

	public static final int DEFAULT_VERTICAL_SIZE = 600;
	public static final int DEFAULT_NAVIGATOR_NODES_SIZE = 20;
	public static final boolean DEFAULT_PANZOOM = true;

	public enum Layout {
		DAGRE, KLAY
	}

	public enum Menu {
		NONE, TEXT, WHEEL
	}

	public enum PanZoom {
		ENABLED, DISABLED
	}

	public enum Navigator {
		DEFAULT, ENABLED, DISABLED
	}

	public static Path createHTMLDocument(Graph graph) throws IOException {
		Path tempDirectory = Files.createTempDirectory("graph-viewer");
		return createHTMLDocument(graph, tempDirectory, "", DEFAULT_VERTICAL_SIZE, true, Layout.DAGRE, Menu.NONE,
				PanZoom.ENABLED, Navigator.DEFAULT);
	}

	public static Path createHTMLDocument(Graph graph, Path directoryPath) throws IOException {
		return createHTMLDocument(graph, directoryPath, "", DEFAULT_VERTICAL_SIZE, true, Layout.DAGRE, Menu.NONE,
				PanZoom.ENABLED, Navigator.DEFAULT);
	}

	public static Path createHTMLDocument(Graph graph, Path directoryPath, String name) throws IOException {
		return createHTMLDocument(graph, directoryPath, name, DEFAULT_VERTICAL_SIZE, true, Layout.DAGRE, Menu.NONE,
				PanZoom.ENABLED, Navigator.DEFAULT);
	}

	public static Path createHTMLDocument(Graph graph, Path directoryPath, String name, boolean extend)
			throws IOException {
		return createHTMLDocument(graph, directoryPath, name, DEFAULT_VERTICAL_SIZE, extend, Layout.DAGRE, Menu.NONE,
				PanZoom.ENABLED, Navigator.DEFAULT);
	}

	public static Path createHTMLDocument(Graph graph, Path directoryPath, String name, boolean extend,
			int verticalSize) throws IOException {
		return createHTMLDocument(graph, directoryPath, name, verticalSize, extend, Layout.DAGRE, Menu.NONE,
				PanZoom.ENABLED, Navigator.DEFAULT);
	}

	public static Path createHTMLDocument(Graph graph, Path directoryPath, String name, int verticalSize,
			boolean extend, Layout layout, Menu menu, PanZoom panzoom, Navigator navigator) throws IOException {
		// TODO handle indexPath not getting set
		Path indexPath = null;

		// Set name to empty if it is null
		if (name == null) {
			name = "";
		}

		// Open the directory as a file
		File graphViewerDirectory = directoryPath.toFile();

		// If in debug mode, print path to directory of HTML graph
		if (debug)
			System.out.println("DEBUG: " + graphViewerDirectory.getAbsolutePath());

		// Handle each file needed to create the HTML graph
		for (String resource : Resources.getResources()) {

			// Create File object for the copy of this resource file in graphViewerDirectory
			File resourceFile = new File(graphViewerDirectory.getAbsolutePath() + File.separator
					+ resource.replaceFirst("templates/", "").replace("/", File.separator));

			// Make the parent directory for the HTML graph if it doesn't exist
			if (!resourceFile.getParentFile().exists())
				resourceFile.getParentFile().mkdirs();

			if (resource.equals("templates/index.html")) {
				// This is the main index.html file, handle creating of actual HTML file from
				// template
				// Store the path to the index.html file
				indexPath = resourceFile.toPath();

				// Open the HTML template file and read its contents
				FileWriter fw = new FileWriter(resourceFile);
				String htmlContents = readResource(resource + ".template");

				// Format the HTML file
				htmlContents = formatHTML(htmlContents, graph, name, verticalSize, extend, layout, menu, panzoom,
						navigator);

				// Write the formatted HTML to HTML document being created
				fw.write(htmlContents);
				fw.close();
			} else {
				// This is a resource file, simply copy over to the directory of the HTML
				// document being created
				InputStream inputStream = GraphView.class.getResourceAsStream("/" + resource);
				if (inputStream == null) {
					throw new IOException("Unable to access resource at path: " + resource);
				}
				Files.copy(inputStream, resourceFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		}

		return indexPath;
	}

	public static String formatHTML(String htmlContents, Graph graph, String name, int verticalSize, boolean extend,
			Layout layout, Menu menu, PanZoom panzoom, Navigator navigator) {
		// TODO Unsure what this does
		Graph containsGraph = graph.toGraph(graph.edges(SchemaGraph.Contains));
		containsGraph = graph.toGraph(graph.nodes()).induce(containsGraph);

		// Set the graph name
		htmlContents = htmlContents.replace("TEMPLATE_GRAPH_NAME", name);

		// Create the nodes and edges JSON arrays, add them to a JSON array of elements
		JsonArray elementsJsonArray = new JsonArray();
		elementsJsonArray.addAll(createNodeJson(graph.nodes(), containsGraph, extend));
		elementsJsonArray.addAll(createEdgesJson(graph.edges(), containsGraph));
		htmlContents = htmlContents.replace("TEMPLATE_ELEMENTS", elementsJsonArray.toString());

		// Add layout options JSON
		JsonObject layoutOptionsJsonObject = createLayoutOptions(layout);
		htmlContents = htmlContents.replace("TEMPLATE_LAYOUT_OPTIONS", layoutOptionsJsonObject.toString());

		// Add JavaScript sources to HTML depending on which layout is being used
		if (layout.equals(Layout.DAGRE)) {
			htmlContents = htmlContents.replace("TEMPLATE_LAYOUT",
					"<script src=\"js/dagre.min.js\"></script><script src=\"js/cytoscape-dagre.js\"></script>");
		} else if (layout.equals(Layout.KLAY)) {
			htmlContents = htmlContents.replace("TEMPLATE_LAYOUT",
					"<script src=\"js/klay.min.js\"></script><script src=\"js/cytoscape-klay.js\"></script>");
		}

		// Add JavaScript sources to HTML for text menu
		if (menu.equals(Menu.TEXT)) {
			htmlContents = htmlContents.replace("TEMPLATE_STYLE_TEXT_CONTEXT",
					"<link href=\"css/cytoscape-context-menus.css\" rel=\"stylesheet\" type=\"text/css\" />");
			htmlContents = htmlContents.replace("TEMPLATE_JQUERY", "<script src=\"js/jquery-2.0.3.min.js\"></script>");
			htmlContents = htmlContents.replace("TEMPLATE_JS_TEXT_CONTEXT",
					"<script src=\"js/cytoscape-context-menus.js\"></script>");
			htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_CONTEXT_TEXT", "// TODO: TEXT MENU OPTIONS");
		} else {
			htmlContents = htmlContents.replace("TEMPLATE_STYLE_TEXT_CONTEXT", "");
			htmlContents = htmlContents.replace("TEMPLATE_JS_TEXT_CONTEXT", "");
			htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_CONTEXT_TEXT", "");
		}

		// Add JavaScript sources to HTML for wheel menu
		if (menu.equals(Menu.WHEEL)) {
			htmlContents = htmlContents.replace("TEMPLATE_STYLE_FONT_AWESOME",
					"<link href=\"font-awesome-4.0.3/css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\" />");
			htmlContents = htmlContents.replace("TEMPLATE_JQUERY", "<script src=\"js/jquery-2.0.3.min.js\"></script>");
			htmlContents = htmlContents.replace("TEMPLATE_JS_WHEEL_CONTEXT",
					"<script src=\"js/cytoscape-cxtmenu.js\"></script>");
			htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_CONTEXT_WHEEL", "// TODO: WHEEL MENU OPTIONS");
		} else {
			htmlContents = htmlContents.replace("TEMPLATE_JS_WHEEL_CONTEXT", "");
			htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_CONTEXT_WHEEL", "");
		}

		// Add panzoom if enabled
		if (panzoom.equals(PanZoom.ENABLED)) {
			htmlContents = htmlContents.replace("TEMPLATE_STYLE_FONT_AWESOME",
					"<link href=\"font-awesome-4.0.3/css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\" />");
			htmlContents = htmlContents.replace("TEMPLATE_STYLE_PANZOOM",
					"<link href=\"css/cytoscape.js-panzoom.css\" rel=\"stylesheet\" type=\"text/css\" />");
			htmlContents = htmlContents.replace("TEMPLATE_JQUERY", "<script src=\"js/jquery-2.0.3.min.js\"></script>");
			htmlContents = htmlContents.replace("TEMPLATE_JS_PANZOOM_CONTEXT",
					"<script src=\"js/cytoscape-panzoom.js\"></script>");
			// TODO add panzoom options
			htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_PANZOOM", "cy.panzoom({})");
		} else {
			htmlContents = htmlContents.replace("TEMPLATE_STYLE_PANZOOM", "");
			htmlContents = htmlContents.replace("TEMPLATE_JS_PANZOOM_CONTEXT", "");
			htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_PANZOOM", "");
		}

		// Add navigator if enabled
		if (navigator.equals(Navigator.ENABLED)
				|| (navigator.equals(Navigator.DEFAULT) && graph.nodes().size() >= DEFAULT_NAVIGATOR_NODES_SIZE)) {
			htmlContents = htmlContents.replace("TEMPLATE_STYLE_FONT_AWESOME",
					"<link href=\"font-awesome-4.0.3/css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\" />");
			htmlContents = htmlContents.replace("TEMPLATE_STYLE_NAVIGATOR",
					"<link href=\"css/cytoscape.js-navigator.css\" rel=\"stylesheet\" type=\"text/css\" />");
			htmlContents = htmlContents.replace("TEMPLATE_JQUERY", "<script src=\"js/jquery-2.0.3.min.js\"></script>");
			htmlContents = htmlContents.replace("TEMPLATE_JS_NAVIGATOR_CONTEXT",
					"<script src=\"js/cytoscape-navigator.js\"></script>");
			// TODO add navigator options
			htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_NAVIGATOR", "cy.navigator({})");
		} else {
			htmlContents = htmlContents.replace("TEMPLATE_STYLE_NAVIGATOR", "");
			htmlContents = htmlContents.replace("TEMPLATE_JS_NAVIGATOR_CONTEXT", "");
			htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_NAVIGATOR", "");
		}

		// Remove unused dependencies
		htmlContents = htmlContents.replace("TEMPLATE_STYLE_FONT_AWESOME", "");
		htmlContents = htmlContents.replace("TEMPLATE_JQUERY", "");

		// Replace functions and undefined values with proper text for JavaScript
		htmlContents = toJavaScriptObjectString(htmlContents);

		return htmlContents;
	}

	public static JsonArray createNodeJson(GraphElementSet<Node> nodes, Graph containsGraph, boolean extend) {
		// Create a list of JSON representations of the graph nodes
		JsonArray nodeJsonArray = new JsonArray();

		for (Node node : nodes) {
			// Get the escaped name of the nodes if it has a name
			String nodeName = node.hasName() ? escapeSchemaChars(node.getName()) : "";

			// Get the parent of the node if it exists
			Node parentNode = containsGraph.predecessors(node).one();

			// Create the JSON for the node
			JsonObject nodeJson = new JsonObject();

			// Add the basic data attribute
			JsonObject dataJson = new JsonObject();
			dataJson.addProperty("id", "n" + node.getAddress());
			dataJson.addProperty("name", nodeName);

			// Set shape of node TODO fix padding around diamond shape
			dataJson.addProperty("shape", "round-rectangle");

			if (node.tags().contains("XCSG.ControlFlowLoopCondition")
					|| node.tags().contains("XCSG.ControlFlowIfCondition")) {
				dataJson.addProperty("backgroundcolor", "#e8ae58");
				// dataJson.addProperty("shape", "diamond");
			} else {
				dataJson.addProperty("backgroundcolor", "#34c2db");
				// dataJson.addProperty("shape", "round-rectangle");
			}

			// If extend is set to true and this node has, add parent attribute to data and
			// add classes attribute
			if (extend && parentNode == null) {
				dataJson.addProperty("parent", "n" + node.getAddress());

				JsonArray classesJson = new JsonArray();
				classesJson.add("container");
				nodeJson.add("classes", classesJson);
			}
			nodeJson.add("data", dataJson);

			// Add the node to the array of nodes
			nodeJsonArray.add(nodeJson);
		}

		return nodeJsonArray;
	}

	public static JsonArray createEdgesJson(GraphElementSet<Edge> edges, Graph containsGraph) {
		// Create a list of JSON representations of the graph edges
		JsonArray edgesJsonArray = new JsonArray();

		for (Edge edge : edges) {
			// TODO not sure what this does
			if (!containsGraph.edges().contains(edge)) {

				// Get the escaped name of the edge if it has a name
				String edgeName = edge.hasName() ? escapeSchemaChars(edge.getName()) : "";

				// Create the JSON for the edge
				JsonObject edgeJson = new JsonObject();

				// Create the data attribute for the edge and add it to edgeJson
				JsonObject dataJson = new JsonObject();
				dataJson.addProperty("id", edge.getAddress());
				dataJson.addProperty("name", edgeName);
				dataJson.addProperty("source", "n" + edge.from().getAddress());
				dataJson.addProperty("target", "n" + edge.to().getAddress());
				edgeJson.add("data", dataJson);

				// Add the edge to the array of edges
				edgesJsonArray.add(edgeJson);
			}
		}

		return edgesJsonArray;
	}

	public static JsonObject createLayoutOptions(Layout layout) {
		// Create a JSON object of graph options for the given layout type
		JsonObject graphOptionsJsonObject = new JsonObject();

		// Add layout specific options
		if (layout.equals(Layout.DAGRE)) {
			graphOptionsJsonObject.addProperty("name", "dagre");
			graphOptionsJsonObject.addProperty("nodeSep", "undefined");
			graphOptionsJsonObject.addProperty("edgeSep", "undefined");
			graphOptionsJsonObject.addProperty("rankSep", "undefined");
			graphOptionsJsonObject.addProperty("rankDir", "TB");
			graphOptionsJsonObject.addProperty("rankSep", "undefined");
			graphOptionsJsonObject.addProperty("minLen", "function( edge ){ return 1; }");
			graphOptionsJsonObject.addProperty("edgeWeight", "function( edge ){ return 1; }");

		} else if (layout.equals(Layout.KLAY)) {
			graphOptionsJsonObject.addProperty("name", "klay");
			graphOptionsJsonObject.addProperty("priority", "function( edge ){ return null; }");

			JsonObject klayJsonObject = new JsonObject();
			klayJsonObject.addProperty("addUnnecessaryBendpoints", false);
			klayJsonObject.addProperty("aspectRatio", 1.6);
			klayJsonObject.addProperty("borderSpacing", 20);
			klayJsonObject.addProperty("compactComponents", false);
			klayJsonObject.addProperty("crossingMinimization", "LAYER_SWEEP");
			klayJsonObject.addProperty("cycleBreaking", "GREEDY");
			klayJsonObject.addProperty("direction", "DOWN");
			klayJsonObject.addProperty("edgeRouting", "ORTHOGONAL");
			klayJsonObject.addProperty("edgeSpacingFactor", 0.5);
			klayJsonObject.addProperty("feedbackEdges", false);
			klayJsonObject.addProperty("fixedAlignment", "NONE");
			klayJsonObject.addProperty("inLayerSpacingFactor", 1.0);
			klayJsonObject.addProperty("layoutHierarchy", true);
			klayJsonObject.addProperty("linearSegmentsDeflectionDampening", 0.3);
			klayJsonObject.addProperty("mergeEdges", false);
			klayJsonObject.addProperty("mergeHierarchyCrossingEdges", true);
			klayJsonObject.addProperty("nodeLayering", "NETWORK_SIMPLEX");
			klayJsonObject.addProperty("nodePlacement", "BRANDES_KOEPF");
			klayJsonObject.addProperty("randomizationSeed", 1);
			klayJsonObject.addProperty("routeSelfLoopInside", false);
			klayJsonObject.addProperty("separateConnectedComponents", true);
			klayJsonObject.addProperty("spacing", 20);
			klayJsonObject.addProperty("thoroughness", 7);
			graphOptionsJsonObject.add("klay", klayJsonObject);
		}

		// Add general layout options
		graphOptionsJsonObject.addProperty("fit", true);
		graphOptionsJsonObject.addProperty("padding", 30);
		// find better way to handle undefined
		graphOptionsJsonObject.addProperty("spacingFactor", "undefined");
		graphOptionsJsonObject.addProperty("nodeDimensionsIncludeLabels", false);
		graphOptionsJsonObject.addProperty("animate", false);
		graphOptionsJsonObject.addProperty("animateFilter", "function( node, i ){ return true; }");
		graphOptionsJsonObject.addProperty("animationDuration", 500);
		graphOptionsJsonObject.addProperty("animationEasing", "undefined");
		graphOptionsJsonObject.addProperty("boundingBox", "undefined");
		graphOptionsJsonObject.addProperty("transform", "function( node, pos ){ return pos; }");
		graphOptionsJsonObject.addProperty("ready", "function(){}");
		graphOptionsJsonObject.addProperty("stop", "function(){}");

		return graphOptionsJsonObject;
	}

	public static void show(Path indexPath) {
		// TODO set appropriate default height
		show(indexPath, 500);
	}

	public static void show(Path indexPath, int verticalSize) {
		Display.display("<html><iframe src='" + indexPath + "' width=\"100%\", height=\"" + verticalSize
				+ "px\" frameBorder=\"0\"></iframe></html>", "text/html");
	}

	private static String readResource(String path) throws IOException {
		InputStream inputStream = GraphView.class.getResourceAsStream("/" + path);
		if (inputStream == null) {
			throw new IOException("Unable to access resource at path: /" + path);
		}
		return convertStreamToString(inputStream);
	}

	private static String convertStreamToString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder(2048);
		char[] read = new char[128];
		try (InputStreamReader ir = new InputStreamReader(is, StandardCharsets.UTF_8)) {
			for (int i; -1 != (i = ir.read(read)); sb.append(read, 0, i))
				;
		}
		return sb.toString();
	}

	public static String escapeSchemaChars(String s) {
		s = s.replace("\\", "\\\\");
		s = s.replace("\"", "\\\"");
		s = s.replace("\b", "\\b");
		s = s.replace("\f", "\\f");
		s = s.replace("\n", "\\n");
		s = s.replace("\r", "\\r");
		s = s.replace("\t", "\\t");
		return s;
	}

	public static String toJavaScriptObjectString(String jsonString) {
		// Remove surrounding quotes from all JavaScript functions
		String patternString = "\"function\\s*([A-z0-9]+)?\\s*\\((?:[^)(]+|\\((?:[^)(]+|\\([^)(]*\\))*\\))*\\)\\s*\\{(?:[^}{]+|\\{(?:[^}{]+|\\{[^}{]*\\})*\\})*\\}\"";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(jsonString);
		StringBuilder sb = new StringBuilder();

		while (matcher.find()) {
			String toReplace = matcher.group();
			String replacement = toReplace.substring(1, toReplace.length() - 1);
			matcher.appendReplacement(sb, replacement);
		}
		matcher.appendTail(sb);

		// Remove surrounding quotes from all undefined values
		String result = sb.toString().replace("\"undefined\"", "undefined");

		return result;
	}
}
