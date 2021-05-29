package neo4j.Cerebro;

import java.util.List;

public class NodeLists {
	
	private String node;
	private List<String> list;
	
	public NodeLists(String node, List<String> list) {
		this.node = node;
		this.list = list;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}
	
	@Override
	public String toString() {
		return "Nodo: " + getNode() + ", List: " + getList().toString();
	}
	
}
