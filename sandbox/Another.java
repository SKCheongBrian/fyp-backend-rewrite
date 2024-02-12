class Node {
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
        Node one = new Node(1);
        Node two = new Node(2);
        one.setNext(two);
    }
}