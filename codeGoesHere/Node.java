class Node {
	public Edge value;
	public Node next;
	
	public Node() {
		//do nothing
	}
	
	public Node(Edge value) {
		this.value = value;
	}
	
	public Node(Edge value, Node next) {
		this.value = value; this.next = next;
	}
	
	public String toString() {
		StringBuilder ret = new StringBuilder();
		Node curr = this;
		while(curr != null) {
			ret.append(curr.value);
			if(curr.next != null) ret.append(" ");
			curr = curr.next;
		}
		return ret.toString();
	}
}