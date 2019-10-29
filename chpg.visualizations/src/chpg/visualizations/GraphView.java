package chpg.visualizations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import chpg.graph.Edge;
import chpg.graph.Graph;
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
	
	public static Path createHTMLDocument(Graph graph)  throws IOException {
		Path tempDirectory = Files.createTempDirectory("graph-viewer");
		return createHTMLDocument(graph, tempDirectory, "", DEFAULT_VERTICAL_SIZE, true, Layout.DAGRE, Menu.NONE, PanZoom.ENABLED, Navigator.DEFAULT);
	}

	public static Path createHTMLDocument(Graph graph, Path directoryPath)  throws IOException {
		return createHTMLDocument(graph, directoryPath, "", DEFAULT_VERTICAL_SIZE, true, Layout.DAGRE, Menu.NONE, PanZoom.ENABLED, Navigator.DEFAULT);
	}
	
	
	public static Path createHTMLDocument(Graph graph, Path directoryPath, String name)  throws IOException {
		return createHTMLDocument(graph, directoryPath, name, DEFAULT_VERTICAL_SIZE, true, Layout.DAGRE, Menu.NONE, PanZoom.ENABLED, Navigator.DEFAULT);
	}
	
	public static Path createHTMLDocument(Graph graph, Path directoryPath, String name, boolean extend)  throws IOException {
		return createHTMLDocument(graph, directoryPath, name, DEFAULT_VERTICAL_SIZE, extend, Layout.DAGRE, Menu.NONE, PanZoom.ENABLED, Navigator.DEFAULT);
	}
	
	public static Path createHTMLDocument(Graph graph, Path directoryPath, String name, boolean extend, int verticalSize)  throws IOException {
		return createHTMLDocument(graph, directoryPath, name, verticalSize, extend, Layout.DAGRE, Menu.NONE, PanZoom.ENABLED, Navigator.DEFAULT);
	}
	
	public static Path createHTMLDocument(Graph graph, Path directoryPath, String name, int verticalSize, boolean extend, Layout layout, Menu menu, PanZoom panzoom, Navigator navigator) throws IOException {
		// TODO handle indexPath not getting set
		Path indexPath = null;
		
		// Set name to empty if it is null
		if(name == null) {
			name = "";
		}
				
		// Open the directory as a file
		File graphViewerDirectory = directoryPath.toFile();
		
		// If in debug mode, print path to directory of HTML graph
		if(debug) System.out.println("DEBUG: " + graphViewerDirectory.getAbsolutePath());
		
		// Handle each file needed to create the HTML graph
		for(String resource : Resources.getResources()) {
						
			// Create File object for the copy of this resource file in graphViewerDirectory
			File resourceFile = new File(graphViewerDirectory.getAbsolutePath() + File.separator + resource.replaceFirst("templates/", "").replace("/", File.separator));
			
			// Make the parent directory for the HTML graph if it doesn't exist
			if (!resourceFile.getParentFile().exists()) resourceFile.getParentFile().mkdirs();

			if(resource.equals("templates/index.html")) {
				// This is the main index.html file, handle creating of actual HTML file from template
				// Store the path to the index.html file
				indexPath = resourceFile.toPath();
				
				// Open the HTML template file and read its contents
				FileWriter fw = new FileWriter(resourceFile);
				String htmlContents = readResource(resource + ".template");
				
				// Format the HTML file
				htmlContents = formatHTML(htmlContents, graph, name, verticalSize, extend, layout, menu, panzoom, navigator);
				
				// Write the formatted HTML to HTML document being created
				fw.write(htmlContents);
				fw.close();
			} else {
				// This is a resource file, simply copy over to the directory of the HTML document being created
				InputStream inputStream = GraphView.class.getResourceAsStream("/" + resource); 
				if(inputStream == null) {
					throw new IOException("Unable to access resource at path: " + resource);
				}
				Files.copy(inputStream, resourceFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		}
		
		return indexPath;
	}
	
	public static String formatHTML(String htmlContents, Graph graph, String name, int verticalSize, boolean extend, Layout layout, Menu menu, PanZoom panzoom, Navigator navigator) {
		// Unsure what this does
		// TODO fix comment
		Graph containsGraph = graph.toGraph(graph.edges(SchemaGraph.Contains));
		containsGraph = graph.toGraph(graph.nodes()).induce(containsGraph);
		
		// graph name
		htmlContents = htmlContents.replace("TEMPLATE_GRAPH_NAME", name);
		
		// nodes
		StringBuilder nodeList = new StringBuilder();
		for(Node node : graph.nodes()) {
			if(nodeList.length() != 0) {
				nodeList.append(",");
			}
			String nodeName = node.hasName() ? node.getName() : "";
			// TODO: better escaping (probably use a json library)
			nodeName = nodeName.replace("\\", "\\\\");
			nodeName = nodeName.replace("\"", "\\\"");
			nodeName = nodeName.replace("\b", "\\b");
		    nodeName = nodeName.replace("\f", "\\f");
		    nodeName = nodeName.replace("\n", "\\n");
		    nodeName = nodeName.replace("\r", "\\r");
		    nodeName = nodeName.replace("\t", "\\t");
			Node parentNode = containsGraph.predecessors(node).one();
			if(!extend || parentNode == null) {
				nodeList.append("{ data: { id: \"" + "n" + node.getAddress() + "\", name: \"" + nodeName + "\" } }");
			} else {
				nodeList.append("{ data: { id: \"" + "n" + node.getAddress() + "\", name: \"" + nodeName + "\", parent: \"" + "n" + parentNode.getAddress() + "\" }, classes: ['container'] }");
			}
		}
		htmlContents = htmlContents.replace("TEMPLATE_NODES", nodeList.toString());
		
		// edges
		StringBuilder edgeList = new StringBuilder();
		for(Edge edge : graph.edges()) {
			if(!containsGraph.edges().contains(edge)) {
				String edgeName = edge.hasName() ? edge.getName() : "";
				// TODO: better escaping (probably use a json library)
				edgeName = edgeName.replace("\\", "\\\\");
				edgeName = edgeName.replace("\"", "\\\"");
				edgeName = edgeName.replace("\b", "\\b");
				edgeName = edgeName.replace("\f", "\\f");
				edgeName = edgeName.replace("\n", "\\n");
				edgeName = edgeName.replace("\r", "\\r");
				edgeName = edgeName.replace("\t", "\\t");
				edgeList.append(",{");
			    edgeList.append("data: {");
			    edgeList.append("id: \"" + edge.getAddress() + "\",");
			    edgeList.append("name: \"" + edgeName + "\",");
			    edgeList.append("source: \"" + "n" + edge.from().getAddress() + "\",");
			    edgeList.append("target: \"" + "n" + edge.to().getAddress() + "\"");
			    edgeList.append("}");
			    edgeList.append("}");
			}
		}
		htmlContents = htmlContents.replace("TEMPLATE_EDGES", edgeList.toString());
		
		// dagre layout
		if(layout.equals(Layout.DAGRE)) {
			// requires style, jquery, and options
			htmlContents = htmlContents.replace("TEMPLATE_LAYOUT_DAGRE", "<script src=\"js/dagre.min.js\"></script><script src=\"js/cytoscape-dagre.js\"></script>");
			
			StringBuilder dagreOptions = new StringBuilder();
			dagreOptions.append("name: \"dagre\",");

			dagreOptions.append("nodeSep: undefined,");
			dagreOptions.append("edgeSep: undefined,");
			dagreOptions.append("rankSep: undefined,");
			dagreOptions.append("rankDir: 'TB',");
			dagreOptions.append("ranker: undefined,");
			dagreOptions.append("minLen: function( edge ){ return 1; },");
			dagreOptions.append("edgeWeight: function( edge ){ return 1; },");
			
			htmlContents = htmlContents.replace("TEMPLATE_LAYOUT_OPTIONS_DAGRE", dagreOptions.toString());
		} else {
			htmlContents = htmlContents.replace("TEMPLATE_LAYOUT_DAGRE", "");
			htmlContents = htmlContents.replace("TEMPLATE_LAYOUT_OPTIONS_DAGRE", "");
		}
		
		// klay layout
		if(layout.equals(Layout.KLAY)) {
			// requires style, jquery, and options
			htmlContents = htmlContents.replace("TEMPLATE_LAYOUT_KLAY", "<script src=\"js/klay.min.js\"></script><script src=\"js/cytoscape-klay.js\"></script>");
			
			StringBuilder klayOptions = new StringBuilder();
			klayOptions.append("name: \"klay\",");
			
			klayOptions.append("klay: {");
			klayOptions.append("addUnnecessaryBendpoints: false,");
			klayOptions.append("aspectRatio: 1.6, ");
			klayOptions.append("borderSpacing: 20, ");
			klayOptions.append("compactComponents: false, ");
			klayOptions.append("crossingMinimization: 'LAYER_SWEEP',");
			klayOptions.append("cycleBreaking: 'GREEDY', ");
			klayOptions.append("direction: 'DOWN',");
			klayOptions.append("edgeRouting: 'ORTHOGONAL', ");
			klayOptions.append("edgeSpacingFactor: 0.5,");
			klayOptions.append("feedbackEdges: false, ");
			klayOptions.append("fixedAlignment: 'NONE', ");
			klayOptions.append("inLayerSpacingFactor: 1.0, ");
			klayOptions.append("layoutHierarchy: true,");
			klayOptions.append("linearSegmentsDeflectionDampening: 0.3, ");
			klayOptions.append("mergeEdges: false, ");
			klayOptions.append("mergeHierarchyCrossingEdges: true, ");
			klayOptions.append("nodeLayering:'NETWORK_SIMPLEX', ");
			klayOptions.append("nodePlacement:'BRANDES_KOEPF', ");
			klayOptions.append("randomizationSeed: 1, ");
			klayOptions.append("routeSelfLoopInside: false,");
			klayOptions.append("separateConnectedComponents: true,");
			klayOptions.append("spacing: 20, ");
			klayOptions.append("thoroughness: 7 ");
			klayOptions.append("},");
			klayOptions.append("priority: function( edge ){ return null; },");
			htmlContents = htmlContents.replace("TEMPLATE_LAYOUT_OPTIONS_KLAY", klayOptions.toString());
		} else {
			htmlContents = htmlContents.replace("TEMPLATE_LAYOUT_KLAY", "");
			htmlContents = htmlContents.replace("TEMPLATE_LAYOUT_OPTIONS_KLAY", "");
		}
		
		// general layout options
		StringBuilder generalLayoutOptions = new StringBuilder();
		generalLayoutOptions.append("fit: true,");
		generalLayoutOptions.append("padding: 30,");
		generalLayoutOptions.append("spacingFactor: undefined,");
		generalLayoutOptions.append("nodeDimensionsIncludeLabels: false,");
		generalLayoutOptions.append("animate: false,");
		generalLayoutOptions.append("animateFilter: function( node, i ){ return true; },");
		generalLayoutOptions.append("animationDuration: 500,");
		generalLayoutOptions.append("animationEasing: undefined,");
		generalLayoutOptions.append("boundingBox: undefined,");
		generalLayoutOptions.append("transform: function( node, pos ){ return pos; },");
		generalLayoutOptions.append("ready: function(){},");
		generalLayoutOptions.append("stop: function(){}");
		htmlContents = htmlContents.replace("TEMPLATE_LAYOUT_OPTIONS_GENERAL", generalLayoutOptions.toString());
		
		// text menu
		if(menu.equals(Menu.TEXT)) {
			// requires style, jquery, and options
			htmlContents = htmlContents.replace("TEMPLATE_STYLE_TEXT_CONTEXT", "<link href=\"css/cytoscape-context-menus.css\" rel=\"stylesheet\" type=\"text/css\" />");
			htmlContents = htmlContents.replace("TEMPLATE_JQUERY", "<script src=\"js/jquery-2.0.3.min.js\"></script>");
			htmlContents = htmlContents.replace("TEMPLATE_JS_TEXT_CONTEXT", "<script src=\"js/cytoscape-context-menus.js\"></script>");
			htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_CONTEXT_TEXT", "// TODO: TEXT MENU OPTIONS");
		} else {
			htmlContents = htmlContents.replace("TEMPLATE_STYLE_TEXT_CONTEXT", "");
			htmlContents = htmlContents.replace("TEMPLATE_JS_TEXT_CONTEXT", "");
			htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_CONTEXT_TEXT", "");
		}
		
		// wheel menu
		if(menu.equals(Menu.WHEEL)) {
			// requires font awesome, jquery, and options
			htmlContents = htmlContents.replace("TEMPLATE_STYLE_FONT_AWESOME", "<link href=\"font-awesome-4.0.3/css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\" />");
			htmlContents = htmlContents.replace("TEMPLATE_JQUERY", "<script src=\"js/jquery-2.0.3.min.js\"></script>");
			htmlContents = htmlContents.replace("TEMPLATE_JS_WHEEL_CONTEXT", "<script src=\"js/cytoscape-cxtmenu.js\"></script>");
			htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_CONTEXT_WHEEL", "// TODO: WHEEL MENU OPTIONS");
		} else {
			htmlContents = htmlContents.replace("TEMPLATE_JS_WHEEL_CONTEXT", "");
			htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_CONTEXT_WHEEL", "");
		}
		
		// panzoom
		if(panzoom.equals(PanZoom.ENABLED)) {
			// requires font awesome, style, jquery, and options
			htmlContents = htmlContents.replace("TEMPLATE_STYLE_FONT_AWESOME", "<link href=\"font-awesome-4.0.3/css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\" />");
			htmlContents = htmlContents.replace("TEMPLATE_STYLE_PANZOOM", "<link href=\"css/cytoscape.js-panzoom.css\" rel=\"stylesheet\" type=\"text/css\" />");
			htmlContents = htmlContents.replace("TEMPLATE_JQUERY", "<script src=\"js/jquery-2.0.3.min.js\"></script>");
			htmlContents = htmlContents.replace("TEMPLATE_JS_PANZOOM_CONTEXT", "<script src=\"js/cytoscape-panzoom.js\"></script>");
			StringBuilder panzoomOptions = new StringBuilder();
			panzoomOptions.append("cy.panzoom({");
			panzoomOptions.append("});");
			htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_PANZOOM", panzoomOptions.toString());
		} else {
			htmlContents = htmlContents.replace("TEMPLATE_STYLE_PANZOOM", "");
			htmlContents = htmlContents.replace("TEMPLATE_JS_PANZOOM_CONTEXT", "");
			htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_PANZOOM", "");
		}
		
		// navigator
		if(navigator.equals(Navigator.ENABLED) || (navigator.equals(Navigator.DEFAULT) && graph.nodes().size() >= DEFAULT_NAVIGATOR_NODES_SIZE)) {
			// requires font awesome, style, jquery, and options
			htmlContents = htmlContents.replace("TEMPLATE_STYLE_FONT_AWESOME", "<link href=\"font-awesome-4.0.3/css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\" />");
			htmlContents = htmlContents.replace("TEMPLATE_STYLE_NAVIGATOR", "<link href=\"css/cytoscape.js-navigator.css\" rel=\"stylesheet\" type=\"text/css\" />");
			htmlContents = htmlContents.replace("TEMPLATE_JQUERY", "<script src=\"js/jquery-2.0.3.min.js\"></script>");
			htmlContents = htmlContents.replace("TEMPLATE_JS_NAVIGATOR_CONTEXT", "<script src=\"js/cytoscape-navigator.js\"></script>");
			StringBuilder navigatorOptions = new StringBuilder();
			navigatorOptions.append("cy.navigator({");
			navigatorOptions.append("});");
			htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_NAVIGATOR", navigatorOptions.toString());
		} else {
			htmlContents = htmlContents.replace("TEMPLATE_STYLE_NAVIGATOR", "");
			htmlContents = htmlContents.replace("TEMPLATE_JS_NAVIGATOR_CONTEXT", "");
			htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_NAVIGATOR", "");
		}
		
		// remove unused dependencies
		htmlContents = htmlContents.replace("TEMPLATE_STYLE_FONT_AWESOME", "");
		htmlContents = htmlContents.replace("TEMPLATE_JQUERY", "");
		
		return htmlContents;
	}
	
	public static Path createHTMLDocumentold(Graph graph, Path directoryPath, String name, int verticalSize, boolean extend, Layout layout, Menu menu, PanZoom panzoom, Navigator navigator) throws IOException {
		// TODO handle indexPath not getting set
		Path indexPath = null;
		
		// Set name to empty if it is null
		if(name == null) {
			name = "";
		}
		
		// Open the directory as a file
		File graphViewerDirectory = directoryPath.toFile();
		
		// If in debug mode, print path to directory of HTML graph
		if(debug) System.out.println("DEBUG: " + graphViewerDirectory.getAbsolutePath());
		
		// Handle each file needed to create the HTML graph
		for(String resource : Resources.getResources()) {
						
			// Create File object for the copy of this resource file in graphViewerDirectory
			File resourceFile = new File(graphViewerDirectory.getAbsolutePath() + File.separator + resource.replaceFirst("templates/", "").replace("/", File.separator));
			
			// Make the parent directory for the HTML graph if it doesn't exist
			if (!resourceFile.getParentFile().exists()) resourceFile.getParentFile().mkdirs();

			// Handle creating of actual HTML file from template
			if(resource.equals("templates/index.html")) {
				// Store the path to the index.html file
				indexPath = resourceFile.toPath();
				
				FileWriter fw = new FileWriter(resourceFile);
				String htmlContents = readResource(resource + ".template");
				
				Graph containsGraph = graph.toGraph(graph.edges(SchemaGraph.Contains));
				containsGraph = graph.toGraph(graph.nodes()).induce(containsGraph);
				
				// graph name
				htmlContents = htmlContents.replace("TEMPLATE_GRAPH_NAME", name);
				
				// nodes
				StringBuilder nodeList = new StringBuilder();
				for(Node node : graph.nodes()) {
					if(nodeList.length() != 0) {
						nodeList.append(",");
					}
					String nodeName = node.hasName() ? node.getName() : "";
					// TODO: better escaping (probably use a json library)
					nodeName = nodeName.replace("\\", "\\\\");
					nodeName = nodeName.replace("\"", "\\\"");
					nodeName = nodeName.replace("\b", "\\b");
				    nodeName = nodeName.replace("\f", "\\f");
				    nodeName = nodeName.replace("\n", "\\n");
				    nodeName = nodeName.replace("\r", "\\r");
				    nodeName = nodeName.replace("\t", "\\t");
					Node parentNode = containsGraph.predecessors(node).one();
					if(!extend || parentNode == null) {
						nodeList.append("{ data: { id: \"" + "n" + node.getAddress() + "\", name: \"" + nodeName + "\" } }");
					} else {
						nodeList.append("{ data: { id: \"" + "n" + node.getAddress() + "\", name: \"" + nodeName + "\", parent: \"" + "n" + parentNode.getAddress() + "\" }, classes: ['container'] }");
					}
				}
				htmlContents = htmlContents.replace("TEMPLATE_NODES", nodeList.toString());
				
				// edges
				StringBuilder edgeList = new StringBuilder();
				for(Edge edge : graph.edges()) {
					if(!containsGraph.edges().contains(edge)) {
						String edgeName = edge.hasName() ? edge.getName() : "";
						// TODO: better escaping (probably use a json library)
						edgeName = edgeName.replace("\\", "\\\\");
						edgeName = edgeName.replace("\"", "\\\"");
						edgeName = edgeName.replace("\b", "\\b");
						edgeName = edgeName.replace("\f", "\\f");
						edgeName = edgeName.replace("\n", "\\n");
						edgeName = edgeName.replace("\r", "\\r");
						edgeName = edgeName.replace("\t", "\\t");
						edgeList.append(",{");
					    edgeList.append("data: {");
					    edgeList.append("id: \"" + edge.getAddress() + "\",");
					    edgeList.append("name: \"" + edgeName + "\",");
					    edgeList.append("source: \"" + "n" + edge.from().getAddress() + "\",");
					    edgeList.append("target: \"" + "n" + edge.to().getAddress() + "\"");
					    edgeList.append("}");
					    edgeList.append("}");
					}
				}
				htmlContents = htmlContents.replace("TEMPLATE_EDGES", edgeList.toString());
				
				// dagre layout
				if(layout.equals(Layout.DAGRE)) {
					// requires style, jquery, and options
					htmlContents = htmlContents.replace("TEMPLATE_LAYOUT_DAGRE", "<script src=\"js/dagre.min.js\"></script><script src=\"js/cytoscape-dagre.js\"></script>");
					
					StringBuilder dagreOptions = new StringBuilder();
					dagreOptions.append("name: \"dagre\",");

					dagreOptions.append("nodeSep: undefined,");
					dagreOptions.append("edgeSep: undefined,");
					dagreOptions.append("rankSep: undefined,");
					dagreOptions.append("rankDir: 'TB',");
					dagreOptions.append("ranker: undefined,");
					dagreOptions.append("minLen: function( edge ){ return 1; },");
					dagreOptions.append("edgeWeight: function( edge ){ return 1; },");
					
					htmlContents = htmlContents.replace("TEMPLATE_LAYOUT_OPTIONS_DAGRE", dagreOptions.toString());
				} else {
					htmlContents = htmlContents.replace("TEMPLATE_LAYOUT_DAGRE", "");
					htmlContents = htmlContents.replace("TEMPLATE_LAYOUT_OPTIONS_DAGRE", "");
				}
				
				// klay layout
				if(layout.equals(Layout.KLAY)) {
					// requires style, jquery, and options
					htmlContents = htmlContents.replace("TEMPLATE_LAYOUT_KLAY", "<script src=\"js/klay.min.js\"></script><script src=\"js/cytoscape-klay.js\"></script>");
					
					StringBuilder klayOptions = new StringBuilder();
					klayOptions.append("name: \"klay\",");
					
					klayOptions.append("klay: {");
					klayOptions.append("addUnnecessaryBendpoints: false,");
					klayOptions.append("aspectRatio: 1.6, ");
					klayOptions.append("borderSpacing: 20, ");
					klayOptions.append("compactComponents: false, ");
					klayOptions.append("crossingMinimization: 'LAYER_SWEEP',");
					klayOptions.append("cycleBreaking: 'GREEDY', ");
					klayOptions.append("direction: 'DOWN',");
					klayOptions.append("edgeRouting: 'ORTHOGONAL', ");
					klayOptions.append("edgeSpacingFactor: 0.5,");
					klayOptions.append("feedbackEdges: false, ");
					klayOptions.append("fixedAlignment: 'NONE', ");
					klayOptions.append("inLayerSpacingFactor: 1.0, ");
					klayOptions.append("layoutHierarchy: true,");
					klayOptions.append("linearSegmentsDeflectionDampening: 0.3, ");
					klayOptions.append("mergeEdges: false, ");
					klayOptions.append("mergeHierarchyCrossingEdges: true, ");
					klayOptions.append("nodeLayering:'NETWORK_SIMPLEX', ");
					klayOptions.append("nodePlacement:'BRANDES_KOEPF', ");
					klayOptions.append("randomizationSeed: 1, ");
					klayOptions.append("routeSelfLoopInside: false,");
					klayOptions.append("separateConnectedComponents: true,");
					klayOptions.append("spacing: 20, ");
					klayOptions.append("thoroughness: 7 ");
					klayOptions.append("},");
					klayOptions.append("priority: function( edge ){ return null; },");
					htmlContents = htmlContents.replace("TEMPLATE_LAYOUT_OPTIONS_KLAY", klayOptions.toString());
				} else {
					htmlContents = htmlContents.replace("TEMPLATE_LAYOUT_KLAY", "");
					htmlContents = htmlContents.replace("TEMPLATE_LAYOUT_OPTIONS_KLAY", "");
				}
				
				// general layout options
				StringBuilder generalLayoutOptions = new StringBuilder();
				generalLayoutOptions.append("fit: true,");
				generalLayoutOptions.append("padding: 30,");
				generalLayoutOptions.append("spacingFactor: undefined,");
				generalLayoutOptions.append("nodeDimensionsIncludeLabels: false,");
				generalLayoutOptions.append("animate: false,");
				generalLayoutOptions.append("animateFilter: function( node, i ){ return true; },");
				generalLayoutOptions.append("animationDuration: 500,");
				generalLayoutOptions.append("animationEasing: undefined,");
				generalLayoutOptions.append("boundingBox: undefined,");
				generalLayoutOptions.append("transform: function( node, pos ){ return pos; },");
				generalLayoutOptions.append("ready: function(){},");
				generalLayoutOptions.append("stop: function(){}");
				htmlContents = htmlContents.replace("TEMPLATE_LAYOUT_OPTIONS_GENERAL", generalLayoutOptions.toString());
				
				// text menu
				if(menu.equals(Menu.TEXT)) {
					// requires style, jquery, and options
					htmlContents = htmlContents.replace("TEMPLATE_STYLE_TEXT_CONTEXT", "<link href=\"css/cytoscape-context-menus.css\" rel=\"stylesheet\" type=\"text/css\" />");
					htmlContents = htmlContents.replace("TEMPLATE_JQUERY", "<script src=\"js/jquery-2.0.3.min.js\"></script>");
					htmlContents = htmlContents.replace("TEMPLATE_JS_TEXT_CONTEXT", "<script src=\"js/cytoscape-context-menus.js\"></script>");
					htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_CONTEXT_TEXT", "// TODO: TEXT MENU OPTIONS");
				} else {
					htmlContents = htmlContents.replace("TEMPLATE_STYLE_TEXT_CONTEXT", "");
					htmlContents = htmlContents.replace("TEMPLATE_JS_TEXT_CONTEXT", "");
					htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_CONTEXT_TEXT", "");
				}
				
				// wheel menu
				if(menu.equals(Menu.WHEEL)) {
					// requires font awesome, jquery, and options
					htmlContents = htmlContents.replace("TEMPLATE_STYLE_FONT_AWESOME", "<link href=\"font-awesome-4.0.3/css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\" />");
					htmlContents = htmlContents.replace("TEMPLATE_JQUERY", "<script src=\"js/jquery-2.0.3.min.js\"></script>");
					htmlContents = htmlContents.replace("TEMPLATE_JS_WHEEL_CONTEXT", "<script src=\"js/cytoscape-cxtmenu.js\"></script>");
					htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_CONTEXT_WHEEL", "// TODO: WHEEL MENU OPTIONS");
				} else {
					htmlContents = htmlContents.replace("TEMPLATE_JS_WHEEL_CONTEXT", "");
					htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_CONTEXT_WHEEL", "");
				}
				
				// panzoom
				if(panzoom.equals(PanZoom.ENABLED)) {
					// requires font awesome, style, jquery, and options
					htmlContents = htmlContents.replace("TEMPLATE_STYLE_FONT_AWESOME", "<link href=\"font-awesome-4.0.3/css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\" />");
					htmlContents = htmlContents.replace("TEMPLATE_STYLE_PANZOOM", "<link href=\"css/cytoscape.js-panzoom.css\" rel=\"stylesheet\" type=\"text/css\" />");
					htmlContents = htmlContents.replace("TEMPLATE_JQUERY", "<script src=\"js/jquery-2.0.3.min.js\"></script>");
					htmlContents = htmlContents.replace("TEMPLATE_JS_PANZOOM_CONTEXT", "<script src=\"js/cytoscape-panzoom.js\"></script>");
					StringBuilder panzoomOptions = new StringBuilder();
					panzoomOptions.append("cy.panzoom({");
					panzoomOptions.append("});");
					htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_PANZOOM", panzoomOptions.toString());
				} else {
					htmlContents = htmlContents.replace("TEMPLATE_STYLE_PANZOOM", "");
					htmlContents = htmlContents.replace("TEMPLATE_JS_PANZOOM_CONTEXT", "");
					htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_PANZOOM", "");
				}
				
				// navigator
				if(navigator.equals(Navigator.ENABLED) || (navigator.equals(Navigator.DEFAULT) && graph.nodes().size() >= DEFAULT_NAVIGATOR_NODES_SIZE)) {
					// requires font awesome, style, jquery, and options
					htmlContents = htmlContents.replace("TEMPLATE_STYLE_FONT_AWESOME", "<link href=\"font-awesome-4.0.3/css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\" />");
					htmlContents = htmlContents.replace("TEMPLATE_STYLE_NAVIGATOR", "<link href=\"css/cytoscape.js-navigator.css\" rel=\"stylesheet\" type=\"text/css\" />");
					htmlContents = htmlContents.replace("TEMPLATE_JQUERY", "<script src=\"js/jquery-2.0.3.min.js\"></script>");
					htmlContents = htmlContents.replace("TEMPLATE_JS_NAVIGATOR_CONTEXT", "<script src=\"js/cytoscape-navigator.js\"></script>");
					StringBuilder navigatorOptions = new StringBuilder();
					navigatorOptions.append("cy.navigator({");
					navigatorOptions.append("});");
					htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_NAVIGATOR", navigatorOptions.toString());
				} else {
					htmlContents = htmlContents.replace("TEMPLATE_STYLE_NAVIGATOR", "");
					htmlContents = htmlContents.replace("TEMPLATE_JS_NAVIGATOR_CONTEXT", "");
					htmlContents = htmlContents.replace("TEMPLATE_OPTIONS_NAVIGATOR", "");
				}
				
				// remove unused dependencies
				htmlContents = htmlContents.replace("TEMPLATE_STYLE_FONT_AWESOME", "");
				htmlContents = htmlContents.replace("TEMPLATE_JQUERY", "");
				
				fw.write(htmlContents);
				fw.close();
			} else {
				// Simply copy over to directory
				InputStream inputStream = GraphView.class.getResourceAsStream("/" + resource); 
				if(inputStream == null) {
					throw new IOException("Unable to access resource at path: " + resource);
				}
				Files.copy(inputStream, resourceFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		}
		
		return indexPath;
	}
	
	public static void show(Path indexPath) {
		// TODO set appropriate default height
		show(indexPath, 500);
	}
	
	public static void show(Path indexPath, int verticalSize) {
		Display.display("<html><iframe src='" + indexPath +"' width=\"100%\", height=\"" + verticalSize + "px\" frameBorder=\"0\"></iframe></html>", "text/html");
	}
	
	private static String readResource(String path) throws IOException {
		URL a = GraphView.class.getResource("/" + path); 
		InputStream inputStream = GraphView.class.getResourceAsStream("/" + path); 
		if(inputStream == null) {
			throw new IOException("Unable to access resource at path: /" + path);
		}
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
