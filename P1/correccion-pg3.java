public class Triangulo {

    private double base, altura;

    public Triangulo(double base, double altura) {
        this.base = base;
        this.altura = altura;
    }

    public double calcularArea() {
        return (base * altura) / 2;
    }

    public double calcularPerimetro() {
        return base + altura + Math.sqrt(base * base + altura * altura);
    }

    public static void main(String[] args) {
        System.out.println(new Triangulo(3, 4).calcularArea());
        System.out.println(new Triangulo(3, 4).calcularPerimetro());
        System.out.println(new Triangulo(5, 12).calcularArea());
        System.out.println(new Triangulo(5, 12).calcularPerimetro());
    }
}

