class B { }

class A {
  int x;
  B b;

  public A(int x, B b) {
    this.x = x;
    this.b = b;
  }

  public A multiply(A b) {
    A a = new A(this.x * b.x, new B());
    // Line X
    return a;
  }

  public static void main(String[] args) {
    B b = new B();
    A a1 = new A(2, b);
    A a2 = new A(3, b);
    A c = a2.multiply(a1);
  }
}