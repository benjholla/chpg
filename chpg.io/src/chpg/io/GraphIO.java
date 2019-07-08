package chpg.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
public class GraphIO {
	
	public static void exportGraph(Graph graph, File output) throws IOException {
		if(graph instanceof PropertyGraph) {
			exportGraph((PropertyGraph) graph, output);
		} else {
			throw new IllegalArgumentException("Unsupported graph type - export only supports Property Graph types.");
		}
	}

	public static void exportGraph(PropertyGraph graph, File output) throws IOException {
		GraphSerialization.SerializedGraph.Builder graphBuilder = GraphSerialization.SerializedGraph.newBuilder();

		SchemaGraph schemaGraph = graph.getSchema();
		for(SchemaNode schemaNode : schemaGraph.getSchemaNodes()) {
			graphBuilder.addSchemaNode(
				GraphSerialization.SerializedGraph.SerializedSchemaNode.newBuilder()
					.setAddress(schemaNode.getAddress())
					.setTag(schemaNode.getTagName())
					.build()
			);
		}
		
		for(SchemaEdge schemaEdge : schemaGraph.getSchemaEdges()) {
			graphBuilder.addSchemaEdge(
				GraphSerialization.SerializedGraph.SerializedSchemaEdge.newBuilder()
					.setAddress(schemaEdge.getAddress())
					.setFrom(schemaEdge.from().getAddress())
					.setTo(schemaEdge.to().getAddress())
					.build()
			);
		}
		
		for(Node node : graph.nodes()) {
			GraphSerialization.SerializedGraph.SerializedNode.Builder nodeBuilder = GraphSerialization.SerializedGraph.SerializedNode.newBuilder();
			
			nodeBuilder.setAddress(node.getAddress());
			
			if(node.getName() != null) {
				nodeBuilder.setName(node.getName());
			}
			
			for(Entry<String,Object> attribute : node.attributes().entrySet()) {
				nodeBuilder.addAttribute(
					GraphSerialization.SerializedAttribute.newBuilder()
						.setName(attribute.getKey())
						.setValue(attribute.getValue().toString())
						.build()
				);
			}
			
			for(String tag : node.tags()) {
				nodeBuilder.addTag(
					GraphSerialization.SerializedTag.newBuilder()
						.setName(tag)
						.build()
				);
			}
			
			graphBuilder.addNode(nodeBuilder.build());
		}
		
		for(Edge edge : graph.edges()) {
			GraphSerialization.SerializedGraph.SerializedEdge.Builder edgeBuilder = GraphSerialization.SerializedGraph.SerializedEdge.newBuilder();
			
			edgeBuilder.setAddress(edge.getAddress());
			
			if(edge.getName() != null) {
				edgeBuilder.setName(edge.getName());
			}
			
			edgeBuilder.setFrom(edge.from().getAddress());
			edgeBuilder.setTo(edge.to().getAddress());
			
			for(Entry<String,Object> attribute : edge.attributes().entrySet()) {
				edgeBuilder.addAttribute(
					GraphSerialization.SerializedAttribute.newBuilder()
						.setName(attribute.getKey())
						.setValue(attribute.getValue().toString())
						.build()
				);
			}
			
			for(String tag : edge.tags()) {
				edgeBuilder.addTag(
					GraphSerialization.SerializedTag.newBuilder()
						.setName(tag)
						.build()
				);
			}
			
			graphBuilder.addEdge(edgeBuilder.build());
		}
		
		GraphSerialization.SerializedGraph serializedGraph = graphBuilder.build();
		output.createNewFile();
		FileOutputStream fos = new FileOutputStream(output);
		serializedGraph.writeTo(fos);
	}
	
	public static PropertyGraph importGraph(File input) throws FileNotFoundException, IOException {
		GraphSerialization.SerializedGraph deserializedGraph
		  = GraphSerialization.SerializedGraph.newBuilder()
		    .mergeFrom(new FileInputStream(input)).build();
		
		Map<Integer,Integer> oldToNewGraphElementAddressMap = new HashMap<Integer,Integer>();
		
		SchemaGraph schema = new SchemaGraph();
		
		for(GraphSerialization.SerializedGraph.SerializedSchemaNode deserializedSchemaNode : deserializedGraph.getSchemaNodeList()) {
			SchemaNode schemaNode = new SchemaNode(deserializedSchemaNode.getTag());
			if(schemaNode.equals(schema.ContainsSchemaNode)) {
				oldToNewGraphElementAddressMap.put(deserializedSchemaNode.getAddress(), schema.ContainsSchemaNode.getAddress());
			} else {
				oldToNewGraphElementAddressMap.put(deserializedSchemaNode.getAddress(), schemaNode.getAddress());
				schema.add(schemaNode);
			}
		}
		for(GraphSerialization.SerializedGraph.SerializedSchemaEdge deserializedSchemaEdge : deserializedGraph.getSchemaEdgeList()) {
			SchemaNode from = schema.getSchemaNodeByAddress(oldToNewGraphElementAddressMap.get(deserializedSchemaEdge.getFrom()));
			if(from != null) {
				SchemaNode to = schema.getSchemaNodeByAddress(oldToNewGraphElementAddressMap.get(deserializedSchemaEdge.getTo()));
				if(to != null) {
					SchemaEdge schemaEdge = new SchemaEdge(from, to);
					oldToNewGraphElementAddressMap.put(deserializedSchemaEdge.getAddress(), schemaEdge.getAddress());
					schema.add(schemaEdge);
				} else {
					throw new IOException("Schema edge to node not found for address " + deserializedSchemaEdge.getTo());
				}
			} else {
				throw new IOException("Schema edge from node not found for address " + deserializedSchemaEdge.getFrom());
			}
		}
		
		oldToNewGraphElementAddressMap.clear();
		
		PropertyGraph graph = new PropertyGraph(schema);
		for(GraphSerialization.SerializedGraph.SerializedNode deserializedNode : deserializedGraph.getNodeList()) {
			Node node = new Node();
			if(deserializedNode.hasName()) {
				node.setName(deserializedNode.getName());
			}
			for(GraphSerialization.SerializedAttribute attribute : deserializedNode.getAttributeList()) {
				node.attributes().put(attribute.getName(), attribute.getValue());
			}
			for(GraphSerialization.SerializedTag tag : deserializedNode.getTagList()) {
				node.tags().add(tag.getName());
			}
			oldToNewGraphElementAddressMap.put(deserializedNode.getAddress(), node.getAddress());
			graph.add(node);
		}
		for(GraphSerialization.SerializedGraph.SerializedEdge deserializedEdge : deserializedGraph.getEdgeList()) {
			Node from = graph.getNodeByAddress(oldToNewGraphElementAddressMap.get(deserializedEdge.getFrom()));
			if(from != null) {
				Node to = graph.getNodeByAddress(oldToNewGraphElementAddressMap.get(deserializedEdge.getTo()));
				if(to != null) {
					Edge edge = new Edge(from, to);
					if(deserializedEdge.hasName()) {
						edge.setName(deserializedEdge.getName());
					}
					for(GraphSerialization.SerializedAttribute attribute : deserializedEdge.getAttributeList()) {
						edge.attributes().put(attribute.getName(), attribute.getValue());
					}
					for(GraphSerialization.SerializedTag tag : deserializedEdge.getTagList()) {
						edge.tags().add(tag.getName());
					}
					oldToNewGraphElementAddressMap.put(deserializedEdge.getAddress(), edge.getAddress());
					graph.add(edge);
				} else {
					throw new IOException("Schema edge to node not found for address " + deserializedEdge.getTo());
				}
			} else {
				throw new IOException("Schema edge from node not found for address " + deserializedEdge.getFrom());
			}
		}
		
		return graph;
	}
	
}
