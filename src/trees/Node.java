package trees;

public class Node {
	public Node left;
	public Node right;
	public int height;
	public int data;
	
	public Node(int n) {
		this.data = n;
		this.height = 1;
	}
}
