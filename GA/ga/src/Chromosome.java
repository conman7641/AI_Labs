import java.util.Random;

public class Chromosome {
    private String string;
    private int fitness;
    private int length;

    public Chromosome(int length) {
        this.length = length;
        this.string = methodUtils.generateRandomBinaryString(length);
        this.fitness = setFitness();
    }

    public Chromosome(String string) {
        this.string = string;
        this.length = string.length();
        this.fitness = setFitness();
    }

    public String getValue() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getFitness() {
        return fitness;
    }

    public int setFitness() {
        int fitnessScore = 0;
        for (int i = 0; i < length; i++) {
            if (string.charAt(i) == '1') {
                fitnessScore++;
            }
        }
        return fitnessScore;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
