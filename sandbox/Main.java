class A {
  int x;
  
  public A(int x) {
    this.x = x;
  }
  
  int getX() {
    return x;
  }
}

public class Main {
  public static void main(String[] args) {
    A a = new A(10);
    int z = a.getX();
  }
}