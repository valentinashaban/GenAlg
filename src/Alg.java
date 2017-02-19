import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Random;

class Individual2 implements Comparable<Individual2> {   //особь
    int x1;
    int x2;
    private String bin_x1;
    private String bin_x2;

    public String getBin_x2() {
        return bin_x2;
    }

    public void setBin_x2(String bin_x2) {
        this.bin_x2 = bin_x2;
    }

    public String getBin_x1() {
        return bin_x1;
    }

    public void setBin_x1(String bin_x1) {
        this.bin_x1 = bin_x1;
    }

    String ToBin(int n) {
        return Integer.toBinaryString(n);
    }

    @Override
    public String toString() {
        return "Individual2{" +
                "x1=" + x1 +
                ", x2=" + x2 +
                ", bin_x1='" + bin_x1 + '\'' +
                ", bin_x2='" + bin_x2 + '\'' +
                ", fitness_function=" + fitness_function +
                '}';
    }

    double fitness_function; //целевая функция

    public Individual2(int x1, int x2) {
        this.x1 = x1;
        this.x2 = x2;
        this.bin_x1 = ToBin(this.x1);
        this.bin_x2 = ToBin(this.x2);
        this.fitness_function = this.x1 * Math.sin(4) * this.x1 + 1.1 * this.x2 * Math.sin(2) * this.x2;
    }

    public int compareTo(Individual2 o) {
        return (int) (fitness_function - o.fitness_function);
    }

}

public class Alg {
    public static final int SIZE_POPULATION = 100;  //количество особей в популяции
    public static final double E_RATE = 0.1;
    public static final double S_RATE = 0.5;
    public static final double M_RATE = 0.2;
    private static final int MAX_ITERATIONS = 100;

    //инициализация начальной популяции
    public void init(List<Individual2> population) {
        Random r = new Random();
        System.out.println("Start population:");
        for (int i = 0; i < SIZE_POPULATION; i++) {
            int x1 = (int) r.nextInt(255);
            int x2 = (int) r.nextInt(255);
            Individual2 individual = new Individual2(x1, x2);
            population.add(individual);
        }
        for (int i = 0; i < population.size(); i++) {
            System.out.println(population.get(i));
        }
    }

    //отбор для скрещивания
    private void select(List<Individual2> population, List<Individual2> children, int size) {
        for (int i = 0; i < size; i++) {
            children.add(population.get(i));
        }
    }

    //мутация
    int mutation(int x1) {
        String buf = Integer.toBinaryString(x1);
        int position = (int) (Math.random() * buf.length()); //номер бита  для мутации
        StringBuilder s = new StringBuilder(buf.subSequence(0, buf.length()));
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(position) == '0') s.setCharAt(position, '1');
            else if (s.charAt(position) == '1') s.setCharAt(position, '0');
        }
        return Integer.parseInt(s.toString(), 2);
    }

    //скрещивание
    List<Individual2> crossing(List<Individual2> population) {
        int size = (int) (SIZE_POPULATION * E_RATE);//количесвто пар для скрещивания
        List<Individual2> children = new ArrayList<>(); //список потомков
        select(population, children, size);//отбор
        for (int i = size; i < SIZE_POPULATION; i++) {
            int i1 = (int) (Math.random() * SIZE_POPULATION * S_RATE);
            int i2 = (int) (Math.random() * SIZE_POPULATION * S_RATE);
            int position = (int) (Math.random() * 2); //  точка кроссовера
            if (position > population.get(i).getBin_x1().length() | position > population.get(i).getBin_x2().length()) {
                position = (int) (Math.random() * 4); //  точка кроссовера
            }
            String str1 = population.get(i1).getBin_x1().substring(0, position) +
                    population.get(i2).getBin_x2().substring(position);
            String str2 = population.get(i1).getBin_x2().substring(0, position) +
                    population.get(i2).getBin_x1().substring(position);
            int new_child1 = Integer.parseInt(str1, 2);
            int new_child2 = Integer.parseInt(str2, 2);

            if (Math.random() < M_RATE) {   //вычисляется вероятность мутации
                new_child1 = mutation(new_child1);
                new_child2 = mutation(new_child2);
            }
            Individual2 child = new Individual2(new_child1, new_child2);
            children.add(child);
        }
        return children;
    }

    private void Start() {
        List<Individual2> population = new ArrayList<>();//особи
        init(population); //инициализация начальной популяции
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            Collections.sort(population);
            System.out.println(i + " > " + population.get(i));
            if (population.get(0).fitness_function == 0) {
                break;
            }
            population = crossing(population); //отбор и скрещивание
        }
    }

    public static void main(String[] args) {
        new Alg().Start();
    }
}