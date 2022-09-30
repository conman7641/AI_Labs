import java.util.ArrayList;
import java.util.Random;

public class ga {

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: java ga <population size> <selection method> <crossover rate> <mutation rate>");
            System.exit(1);
        }
        //Create a genetic algorithm to get the most ones in a binary string
        Random rand = new Random();
        double Pc = Double.parseDouble(args[2]);
        double Pm = Double.parseDouble(args[3]);
        int popSize = Integer.parseInt(args[0]);
        int selectionType = Integer.parseInt(args[1]);
        ArrayList<Chromosome> currentPopulation = new ArrayList<>();
        ArrayList<Chromosome> newGeneration = new ArrayList<>();
        int generationCount = 0;

        //Create the initial currentPopulation
        for (int i = 0; i < popSize; i++) {
            currentPopulation.add(new Chromosome(20));
        }
        while(methodUtils.getMaxFitness(currentPopulation) != 20) {
            methodUtils.saveGenerationtoFile(currentPopulation, generationCount);
            while (newGeneration.size() != popSize) {
                Chromosome winner1 = null;
                Chromosome winner2 = null;
                if (selectionType == 2) {
                    winner1 = methodUtils.tournamentSelection(currentPopulation);
                    winner2 = methodUtils.tournamentSelection(currentPopulation);
                } else if (selectionType == 1) {
                    winner1 = methodUtils.weightProportionalSelection(currentPopulation);
                    winner2 = methodUtils.weightProportionalSelection(currentPopulation);
                }
                if (Math.random() < Pm) {                //Chance of mutation for parent 1
                    Chromosome child1 = methodUtils.mutation(winner1);
                    winner1 = child1;
                }
                if (Math.random() < Pm) {                //Chance of mutation for parent 2
                    Chromosome child2 = methodUtils.mutation(winner2);
                    winner2 = child2;
                }
                if (Math.random() < Pc) {          //chance of crossover
                    ArrayList<Chromosome> children = methodUtils.crossOver(winner1, winner2);
                    winner1 = children.get(0);
                    winner2 = children.get(1);
                }
                newGeneration.add(winner1);
                newGeneration.add(winner2);
            }
            currentPopulation.clear();
            currentPopulation.addAll(newGeneration);
            generationCount++;
            newGeneration.clear();
            System.out.println("Generation: " + generationCount + " Max Fitness: " + methodUtils.getMaxFitness(currentPopulation));
        }
        methodUtils.saveGenerationtoFile(currentPopulation, generationCount);
    }
}
