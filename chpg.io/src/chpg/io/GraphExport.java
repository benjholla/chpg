package chpg.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

import chpg.graph.Edge;
import chpg.graph.Graph;
import chpg.graph.Node;
import chpg.graph.PropertyGraph;
import chpg.graph.schema.SchemaEdge;
import chpg.graph.schema.SchemaGraph;
import chpg.graph.schema.SchemaNode;
import chpg.io.support.GraphSerialization;

// TODO: consider alternate format for large files
// see https://developers.google.com/protocol-buffers/docs/techniques
public class GraphExport {
	
	public static void export(Graph graph, File output) throws IOException {
		if(graph instanceof PropertyGraph) {
			export((PropertyGraph) graph, output);
		} else {
			throw new IllegalArgumentException("Unsupported graph type - export only supports Property Graph types.");
		}
	}

	public static void export(PropertyGraph graph, File output) throws IOException {
		GraphSerialization.Graph.Builder graphBuilder = GraphSerialization.Graph.newBuilder();

		SchemaGraph schemaGraph = graph.getSchema();
		for(SchemaNode schemaNode : schemaGraph.getSchemaNodes()) {
			graphBuilder.addSchemaNode(
				GraphSerialization.Graph.SchemaNode.newBuilder()
					.setId(schemaNode.getAddress())
					.setTag(schemaNode.getTagName())
					.build()
			);
		}
		
		for(SchemaEdge schemaEdge : schemaGraph.getSchemaEdges()) {
			graphBuilder.addSchemaEdge(
				GraphSerialization.Graph.SchemaEdge.newBuilder()
					.setId(schemaEdge.getAddress())
					.setFrom(schemaEdge.from().getAddress())
					.setTo(schemaEdge.to().getAddress())
					.build()
			);
		}
		
		for(Node node : graph.nodes()) {
			GraphSerialization.Graph.Node.Builder nodeBuilder = GraphSerialization.Graph.Node.newBuilder();
			
			nodeBuilder.setId(node.getAddress());
			
			if(node.getName() != null) {
				nodeBuilder.setName(node.getName());
			}
			
			for(Entry<String,Object> attribute : node.attributes().entrySet()) {
				nodeBuilder.addAttribute(
					GraphSerialization.Attribute.newBuilder()
						.setName(attribute.getKey())
						.setValue(attribute.getValue().toString())
						.build()
				);
			}
			
			for(String tag : node.tags()) {
				nodeBuilder.addTag(
					GraphSerialization.Tag.newBuilder()
						.setName(tag)
						.build()
				);
			}
			
			graphBuilder.addNode(nodeBuilder.build());
		}
		
		for(Edge edge : graph.edges()) {
			GraphSerialization.Graph.Edge.Builder edgeBuilder = GraphSerialization.Graph.Edge.newBuilder();
			
			edgeBuilder.setId(edge.getAddress());
			
			if(edge.getName() != null) {
				edgeBuilder.setName(edge.getName());
			}
			
			edgeBuilder.setFrom(edge.from().getAddress());
			edgeBuilder.setTo(edge.to().getAddress());
			
			for(Entry<String,Object> attribute : edge.attributes().entrySet()) {
				edgeBuilder.addAttribute(
					GraphSerialization.Attribute.newBuilder()
						.setName(attribute.getKey())
						.setValue(attribute.getValue().toString())
						.build()
				);
			}
			
			for(String tag : edge.tags()) {
				edgeBuilder.addTag(
					GraphSerialization.Tag.newBuilder()
						.setName(tag)
						.build()
				);
			}
			
			graphBuilder.addEdge(edgeBuilder.build());
		}
		
		GraphSerialization.Graph serializedGraph = graphBuilder.build();
		output.createNewFile();
		FileOutputStream fos = new FileOutputStream(output);
		serializedGraph.writeTo(fos);
	}
	
}
