class A {
  private int x;
  
  public A(int x) {
    this.x = x;
  }
  
  public int get() {
    return this.x;
  }
  
  public void set(int x) {
    this.x = x;
  }
}

class B extends A {
  private int y;
  
  public B(int x, int y) {
    super(x);
    this.y = y;
  }
  
  public B compute() {
    return this;
  }
}

public class Main {
  public static void main(String[] args) {
    B b = new B(1, 2);
    b.compute();
  }
}