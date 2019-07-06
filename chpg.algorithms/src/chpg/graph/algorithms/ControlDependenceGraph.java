package chpg.graph.algorithms;

import chpg.graph.Edge;
import chpg.graph.Graph;
import chpg.graph.PropertyGraph;

public class ControlDependenceGraph extends PropertyGraph {

	public static final String CONTROL_DEPENDENCE_EDGE = "control-dependence";
	
	private DominanceGraph dominanceGraph;
	
	/**
	 * Contructs a control dependence graph
	 * (https://en.wikipedia.org/wiki/Dependence_analysis#Control_dependencies) from
	 * a given control flow graph with unique entry and exit nodes. Control
	 * dependence is a type of program slicing
	 * https://en.wikipedia.org/wiki/Program_slicing
	 * 
	 * @param graph
	 */
	public ControlDependenceGraph(UniqueEntryExitGraph graph) {
		super(graph.getSchema(), graph.nodes());
		this.dominanceGraph = new DominanceGraph(graph, true);
		// create control dependence edges
		for(Edge postDomFrontierEdge : dominanceGraph.edges(DominanceGraph.POST_DOMINANCE_FRONTIER_EDGE)) {
			Edge controlDependenceEdge = new Edge(postDomFrontierEdge.to(), postDomFrontierEdge.from());
			controlDependenceEdge.tags().add(CONTROL_DEPENDENCE_EDGE);
//			Edge cfEdge = getControlFlowGraph().between(controlDependenceEdge.from(), controlDependenceEdge.to()).edges().one();
//			if(cfEdge.hasAttr(Schema.conditionValue)) {
//				controlDependenceEdge.putAttr(Schema.conditionValue, cfEdge.getAttr(Schema.conditionValue));
//			}
			this.edges().add(controlDependenceEdge);
		}
	}

	public Graph getControlFlowGraph() {
		return dominanceGraph.getUniqueEntryExitGraph();
	}
	
}
