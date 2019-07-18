# TODOs 

A list of TODOs in no particular order.

- Typed Nodes/Edges
  - Make `Graph` generic with `Graph<N extends Node, E extends Edge<? extends Node, ? extends Node>>` and enforce tag hierarchy and attributes with explicit types
  - `BasicGraph extends Graph<Node,Edge>`
  - Typed queries (`Graph` subtypes)
- IBinder examples
  - Hello World
  - Buffer overflow
- Documentation
  - Philosophy
  - Basic usage examples
- Algorithms
  - Standard graph algorithms
- Visualization
  - Compound Dagre support - [https://github.com/cytoscape/cytoscape.js/issues/844](https://github.com/cytoscape/cytoscape.js/issues/844)
  - Autosize nodes for title
  - Node icons
  - Collapse/expand support
  - Smart views JavaScript example
  - Selected properties details view
  - Export jpeg/png
  - Resizable vertical view
  - Jupyter Notebook security issues ([Resource 1](https://www.stefaanlippens.net/jupyter-custom-d3-visualization.html), [Resource 2](https://jupyter-notebook.readthedocs.io/en/stable/examples/Notebook/JavaScript%20Notebook%20Extensions.html)
)
