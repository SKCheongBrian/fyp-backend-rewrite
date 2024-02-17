class Node {
  static int x = 2;
  static Node test = new Node(3);
  private int value;
  private Node next;
  public Node(int value) {
    this.value = value;
    this.next = null;
  }
  
  public void setNext(Node next) {
    this.next = next;
  }
}

public class Another {
  static int x = 3;
    public static void main(String[] args) {
        int x = 1;
        int y = 2;
        Node one = new Node(1);
        Node two = new Node(2);
        one.setNext(two);
    }
}