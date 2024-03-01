interface C {
  
}

class A {
  int x = 1;
  int y = 2;
  int z = 3;
  int a = 3;
  public void f()  {
    C c = new C(){};
  }

  

  
  public static void main(String[] args) {
    A a = new A();
    a.f();
    a.f();
  }
}
