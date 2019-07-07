package chpg.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import chpg.graph.Edge;
import chpg.graph.Node;
import chpg.graph.PropertyGraph;
import chpg.graph.schema.SchemaEdge;
import chpg.graph.schema.SchemaGraph;
import chpg.graph.schema.SchemaNode;
import chpg.io.support.GraphSerialization;

public class GraphImport {

	public static PropertyGraph importGraph(File input) throws FileNotFoundException, IOException {
		GraphSerialization.Graph deserializedGraph
		  = GraphSerialization.Graph.newBuilder()
		    .mergeFrom(new FileInputStream(input)).build();
		
		Map<Integer,Integer> oldToNewGraphElementAddressMap = new HashMap<Integer,Integer>();
		
		SchemaGraph schema = new SchemaGraph();
		
		for(GraphSerialization.Graph.SchemaNode deserializedSchemaNode : deserializedGraph.getSchemaNodeList()) {
			SchemaNode schemaNode = new SchemaNode(deserializedSchemaNode.getTag());
			oldToNewGraphElementAddressMap.put(deserializedSchemaNode.getAddress(), schemaNode.getAddress());
			schema.add(schemaNode);
		}
		for(GraphSerialization.Graph.SchemaEdge deserializedSchemaEdge : deserializedGraph.getSchemaEdgeList()) {
			SchemaNode from = schema.getSchemaNodeByAddress(oldToNewGraphElementAddressMap.get(deserializedSchemaEdge.getFrom()));
			SchemaNode to = schema.getSchemaNodeByAddress(oldToNewGraphElementAddressMap.get(deserializedSchemaEdge.getTo()));
			SchemaEdge schemaEdge = new SchemaEdge(from, to);
			oldToNewGraphElementAddressMap.put(deserializedSchemaEdge.getAddress(), schemaEdge.getAddress());
			schema.add(schemaEdge);
		}
		
		oldToNewGraphElementAddressMap.clear();
		
		PropertyGraph graph = new PropertyGraph(schema);
		for(GraphSerialization.Graph.Node deserializedNode : deserializedGraph.getNodeList()) {
			Node node = new Node();
			if(deserializedNode.hasName()) {
				node.setName(deserializedNode.getName());
			}
			for(GraphSerialization.Attribute attribute : deserializedNode.getAttributeList()) {
				node.attributes().put(attribute.getName(), attribute.getValue());
			}
			for(GraphSerialization.Tag tag : deserializedNode.getTagList()) {
				node.tags().add(tag.getName());
			}
			oldToNewGraphElementAddressMap.put(deserializedNode.getAddress(), node.getAddress());
			graph.add(node);
		}
		for(GraphSerialization.Graph.Edge deserializedEdge : deserializedGraph.getEdgeList()) {
			Node from = graph.getNodeByAddress(oldToNewGraphElementAddressMap.get(deserializedEdge.getFrom()));
			Node to = graph.getNodeByAddress(oldToNewGraphElementAddressMap.get(deserializedEdge.getTo()));
			Edge edge = new Edge(from, to);
			if(deserializedEdge.hasName()) {
				edge.setName(deserializedEdge.getName());
			}
			for(GraphSerialization.Attribute attribute : deserializedEdge.getAttributeList()) {
				edge.attributes().put(attribute.getName(), attribute.getValue());
			}
			for(GraphSerialization.Tag tag : deserializedEdge.getTagList()) {
				edge.tags().add(tag.getName());
			}
			oldToNewGraphElementAddressMap.put(deserializedEdge.getAddress(), edge.getAddress());
			graph.add(edge);
		}
		
		return graph;
	}
	
}
