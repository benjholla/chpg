#  CHPG/QL
Compound Hierarchal Property Graph and Query Language

CHPG is a lightweight graph database for storing property graphs (attributed, multi-relational graphs) with hierarchical and compound relationships. Like [SQLite](https://www.sqlite.org/index.html), which serves as an embedded database that can be serialized to a single single cross-platform file format, CHPG is a queryable in memory graph database that serializes to a single cross-platform file format. The graph APIs and query language mimic much of the design and user experience of the [Atlas](http://www.ensoftcorp.com/atlas) program graph and query language, but is not specific to code property graphs.

## Nodes and Edges
Both nodes and edges in SPGQLite are `GraphElement` objects. `GraphElement` objects are uniquely addressed and equality is based on the equality of the addresses between the given graph elements. A `Node` is simply a `GraphElement`, but an `Edge` consists of two `Node`s that form a directed edge between the `from` node and the `to` node.

## Attributes and Tags
All `GraphElement` objects contain an attribute map that maps a `String` attribute key to an `Object` value. Conceptually, a tag is an attribute whose value is `true`, of which the presence of the tag denotes that the `GraphElement` is a member of a set. Nodes and edges can be queried in a graph by selecting by the presence of a tag or attribute or by filtering by `GraphElement` objects with the given attribute and attribute value.

## Tag Hierarchy
All tags denote a set membership, however some tags may be implicit depending defined the tag hierarchy relationships.

## Containment Edges
Containment edges are hierarchal edge tags that denote structural nesting of nodes and form compound nodes.

## Graph Schema
- TODO: document

## Query Language
- TODO: document

## Serialization
- TODO: document

## TODOs
See [TODO.md](TODO.md).
