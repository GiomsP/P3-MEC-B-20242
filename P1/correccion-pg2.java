public class Triangulo {

    private double base, altura;

    public Triangulo(double base, double altura) {
        this.base = base;
        this.altura = altura;
    }

    public Triangulo() {
        this(1.0, 1.0);
    }

    public Triangulo(double lado) {
        this(lado, lado);
    }

    public double calcularArea() {
        return (base * altura) / 2;
    }

    public static void main(String[] args) {
        System.out.println(new Triangulo(3.0, 4.0).calcularArea());
        System.out.println(new Triangulo(5.0, 6.0).calcularArea());
        System.out.println(new Triangulo().calcularArea());
        System.out.println(new Triangulo().calcularArea());
        System.out.println(new Triangulo(4.0).calcularArea());
        System.out.println(new Triangulo(7.0).calcularArea());
    }
}
