package chpg.atlas.support;

import java.util.HashMap;
import java.util.Map;

import com.ensoftcorp.atlas.core.script.Common;
import com.ensoftcorp.atlas.core.xcsg.XCSG;

import chpg.graph.Edge;
import chpg.graph.GraphElement;
import chpg.graph.Node;
import chpg.graph.PropertyGraph;
import chpg.graph.schema.SchemaEdge;
import chpg.graph.schema.SchemaGraph;
import chpg.graph.schema.SchemaNode;

public class GraphConversion {

	public static PropertyGraph convert(com.ensoftcorp.atlas.core.db.graph.Graph atlasGraph) {
		return convert(atlasGraph, false);
	}
	
	public static PropertyGraph convert(com.ensoftcorp.atlas.core.db.graph.Graph atlasGraph, boolean extend) {
		
		// extend graph along contains edges
		if(extend) {
			atlasGraph = Common.universe().edges(XCSG.Contains).reverse(Common.toQ(atlasGraph)).eval();
		}
		
		// create a copy of the XCSG schema
		SchemaGraph schema = new SchemaGraph();
		
		// create XCSG tags
		Map<String,SchemaNode> tags = new HashMap<String,SchemaNode>();
		for(String tag : XCSG.HIERARCHY.registeredTags()) {
			SchemaNode schemaNode = new SchemaNode(tag);
			tags.put(tag, schemaNode);
			schema.add(schemaNode);
		}
		
		// create XCSG tag hierarchy
		for(String childTag : tags.keySet()) {
			SchemaNode childSchemaNode = tags.get(childTag);
			for(String ancestorTag : XCSG.HIERARCHY.ancestorsSet(childTag)) {
				SchemaNode ancestorSchemaNode = tags.get(ancestorTag);
				SchemaEdge schemaEdge = new SchemaEdge(ancestorSchemaNode, childSchemaNode);
				schema.add(schemaEdge);
			}
		}
		
		// XCSG.Contains is the root containment relationship which must extend the CHPG containment primitive tag
		
		SchemaNode chpgContains = schema.ContainsSchemaNode;
		SchemaNode xcsgContains = tags.get(XCSG.Contains);
		SchemaEdge schemaEdge = new SchemaEdge(chpgContains, xcsgContains);
		schema.add(schemaEdge);
		
		// construct a property graph with the XCSG schema
		PropertyGraph chpg = new PropertyGraph(schema);
		
		// create CHPG nodes for corresponding Atlas nodes
		Map<Integer,Integer> atlasToCHPGAddressMap = new HashMap<Integer,Integer>();
		for(com.ensoftcorp.atlas.core.db.graph.Node atlasNode : atlasGraph.nodes()) {
			Node chpgNode = new Node();
			extractAttributes(atlasNode, chpgNode);
			extractTags(atlasNode, chpgNode);
			atlasToCHPGAddressMap.put(atlasNode.address().getBits(), chpgNode.getAddress());
			chpg.add(chpgNode);
		}
		
		// create CHPG edges for corresponding Atlas edges
		for(com.ensoftcorp.atlas.core.db.graph.Edge atlasEdge : atlasGraph.edges()) {
			Node chpgFromNode = chpg.getNodeByAddress(atlasToCHPGAddressMap.get(atlasEdge.from().address().getBits()));
			Node chpgToNode = chpg.getNodeByAddress(atlasToCHPGAddressMap.get(atlasEdge.to().address().getBits()));
			Edge chpgEdge = new Edge(chpgFromNode, chpgToNode);
			extractAttributes(atlasEdge, chpgEdge);
			extractTags(atlasEdge, chpgEdge);
			chpg.add(chpgEdge);
		}
		
		return chpg;
	}

	private static void extractAttributes(com.ensoftcorp.atlas.core.db.graph.GraphElement atlasGraphElement, GraphElement chpgGraphElement) {
		if(atlasGraphElement.hasAttr(XCSG.name)) {
			chpgGraphElement.setName(atlasGraphElement.getAttr(XCSG.name).toString());
		}
		for(String attr : atlasGraphElement.attrI()) {
			if(!attr.equals(XCSG.name)) {
				Object value = atlasGraphElement.getAttr(attr);
				if(value instanceof Boolean) {
					chpgGraphElement.putAttr(attr, (Boolean) value);
				} else if(value instanceof Byte) {
					chpgGraphElement.putAttr(attr, (Byte) value);
				} else if(value instanceof Short) {
					chpgGraphElement.putAttr(attr, (Short) value);
				} else if(value instanceof Integer) {
					chpgGraphElement.putAttr(attr, (Integer) value);
				} else if(value instanceof Float) {
					chpgGraphElement.putAttr(attr, (Float) value);
				} else if(value instanceof Double) {
					chpgGraphElement.putAttr(attr, (Double) value);
				} else if(value instanceof Character) {
					chpgGraphElement.putAttr(attr, (Character) value);
				} else if(value instanceof String) {
					chpgGraphElement.putAttr(attr, (String) value);
				} else {
					chpgGraphElement.putAttr(attr, value.toString());
				}
			}
		}
	}
	
	private static void extractTags(com.ensoftcorp.atlas.core.db.graph.GraphElement atlasGraphElement, GraphElement chpgGraphElement) {
		for(String tag : atlasGraphElement.explicitTagsI()) {
			chpgGraphElement.tags().add(tag);
		}
	}
	
}
