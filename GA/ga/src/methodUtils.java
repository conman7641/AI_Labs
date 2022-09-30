import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class methodUtils {

    public static String generateRandomBinaryString(int length) {
        String binaryString = "";
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            binaryString = binaryString.concat(Integer.toString(rand.nextInt(2)));
        }
        return binaryString;
    }

    public static double getAverageFitness(ArrayList<Chromosome> population) {
        double totalFitness = 0;
        for (Chromosome stringPopulation : population) {
            totalFitness += stringPopulation.getFitness();
        }
        return totalFitness / population.size();
    }

    public static int getMaxFitness(ArrayList<Chromosome> population) {
        int maxFitness = 0;
        for (Chromosome stringPopulation : population) {
            if (stringPopulation.getFitness() > maxFitness) {
                maxFitness = stringPopulation.getFitness();
            }
        }
        return maxFitness;
    }

    public static int getMinFitness(ArrayList<Chromosome> population) {
        int minFitness = population.get(0).getFitness();
        for (Chromosome stringPopulation : population) {
            if (stringPopulation.getFitness() < minFitness) {
                minFitness = stringPopulation.getFitness();
            }
        }
        return minFitness;
    }



    public static void saveGenerationtoFile(ArrayList<Chromosome> population, int generationCount) {
        try {
            FileWriter fw = new FileWriter("fitness_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH-mm-ss")) + ".txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.print(generationCount + ", " + getMinFitness(population) + ", " + getAverageFitness(population) + ", " + getMaxFitness(population) + "");
            out.println();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Tournament selection
    public static Chromosome tournamentSelection(ArrayList<Chromosome> population) {
        Random rand = new Random();
        ArrayList<Chromosome> tournament = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tournament.add(population.get(rand.nextInt(population.size())));
        }

        Chromosome fittest = tournament.get(0);
        for (Chromosome stringTournament : tournament) {
            if (stringTournament.getFitness() > fittest.getFitness()) {
                fittest = stringTournament;
            }
        }
        return fittest;
    }

    //weight proportional selection
    public static Chromosome weightProportionalSelection(ArrayList<Chromosome> population) {
        Random rand = new Random();
        int totalFitness = 0;
        for (Chromosome stringPopulation : population) {
            totalFitness += stringPopulation.getFitness();
        }
        int randomFitness = rand.nextInt(totalFitness);
        int currentFitness = 0;
        for (Chromosome stringPopulation : population) {
            currentFitness += stringPopulation.getFitness();
            if (currentFitness >= randomFitness) {
                return stringPopulation;
            }
        }
        return population.get(0);
    }

    //cross over
    public static ArrayList<Chromosome> crossOver(Chromosome parent1, Chromosome parent2) {
        ArrayList<Chromosome> children = new ArrayList<>();
        Random rand = new Random();
        int crossOverPoint = rand.nextInt(1, parent1.getLength() - 1);
        String childValue1 = parent1.getValue().substring(0, crossOverPoint) + parent2.getValue().substring(crossOverPoint);
        String childValue2 = parent2.getValue().substring(0, crossOverPoint) + parent1.getValue().substring(crossOverPoint);
        children.add(new Chromosome(childValue1));
        children.add(new Chromosome(childValue2));
        return children;
    }

    //mutation
    public static Chromosome mutation(Chromosome child) {
        Random rand = new Random();
        int mutationPoint = rand.nextInt(1, child.getLength() - 1);
        String childValue = child.getValue();
        if (childValue.charAt(mutationPoint) == '0') {
            childValue = childValue.substring(0, mutationPoint) + "1" + childValue.substring(mutationPoint + 1);
        } else {
            childValue = childValue.substring(0, mutationPoint) + "0" + childValue.substring(mutationPoint + 1);
        }
        child.setString(childValue);
        return child;
    }

}
