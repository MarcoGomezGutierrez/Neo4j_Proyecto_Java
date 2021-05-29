package neo4j.Business.Intelligence;

public class NodeRelationship {
	
	private String node;
	private int relationship;
	
	public NodeRelationship(String node, int relationship) {
		this.node = node;
		this.relationship = relationship;
	}
	
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public int getRelationship() {
		return relationship;
	}
	public void setRelationship(int relationship) {
		this.relationship = relationship;
	}
	
	@Override
	public String toString() {
		return "Nodo: " + getNode() + ", Relationship: " + getRelationship();
	}
}
