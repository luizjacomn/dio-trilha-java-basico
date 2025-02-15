import java.util.Scanner;

public class Contador {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int floor = Integer.parseInt(args[0]);
        int ceil = Integer.parseInt(args[1]);

        try {
            contar(floor, ceil);
        } catch (ParametrosInvalidosException e) {
            System.err.println(e.getMessage());
        } finally {
            scanner.close();
        }
    }

    static void contar(int floor, int ceil) throws ParametrosInvalidosException {
        if (floor >= ceil) {
            throw new ParametrosInvalidosException("O segundo parâmetro deve ser maior que o primeiro!");
        }

        int contagem = ceil - floor;

        for (int i = 0; i < contagem; i++) {
            System.out.println(i);
        }
    }

}
