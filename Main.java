// clear && astyle --style=java --indent=tab *.java && javac Main.java && java Main

import java.util.Random;
import java.util.Arrays;

public class Main {
	private static final Random PRNG = new Random();

	private static final long GENERATIONS = 100;

	private static double max = 15D;

	private static boolean[] solution = {		false,		false,		false,		false,		false,		false,		false,		false,		false,		false,		false,		false,		false,		false,};

	private static double[] values = {		23,		33,		55,		85,		16,		92,		11,		24,		17,		85,		91,		45,		32,		23,	};

	private static double[] weights = {		3,		3,		5,		5,		6,		2,		1,		4,		7,		5,		1,		5,		2,		3,	};

	private static final int SIZE = 19;
	private static boolean[][] population = {{}};

	static {
		population = new boolean[SIZE][];
		for(int i=0; i<SIZE; i++) {
			population[i] = new boolean[ solution.length ];
		}
	}

	private static void mutate(boolean []individual) {
		int index = PRNG.nextInt(individual.length);
		individual[index] = !individual[index];
	}

	private static boolean[][] mutate(boolean [][]population) {
		for(boolean []individual : population) {
			mutate( individual );
		}

		return population;
	}

	private static void crossover(boolean []first, boolean []second) {
		int size = Math.min(first.length, second.length);
		for(int i=0; i<size; i++) {
			if(PRNG.nextInt(100) < 50) {
				boolean buffer = first[i];
				first[i] = second[i];
				second[i] = buffer;
			}
		}
	}

	private static boolean[][] crossover(boolean [][]population) {
		for(int i=0; i<population.length; i++) {
			int r = PRNG.nextInt(population.length);

			crossover(population[i], population[r]);
		}

		return population;
	}

	private static double[] evaluate(boolean []individual) {
		double []result = {0.0D, 0.0D};

		for(int i=0; i<individual.length && i<values.length && i<weights.length; i++) {
			if(individual[i] == false) {
				continue;
			}

			result[0] += values[i];
			result[1] += weights[i];
		}

		return result;
	}

	private static double[] evaluate(boolean [][]population) {
		double []result = new double[ population.length ];

		for(int i=0; i<population.length; i++) {
			double []buffer = evaluate( population[i] );

			if(buffer[1] <= max) {
				result[i] = buffer[0];
			} else {
				result[i] = 0;
			}
		}

		return result;
	}

	private static void initialize(boolean [][]population) {
		for(int i=0; i<population.length; i++) {
			for(int j=0; j<population[i].length; j++) {
				population[i][j] = (PRNG.nextDouble() < 0.5) ? true : false;
			}
		}
	}

	private static boolean[][] generate(boolean [][]population) {
		boolean [][]next =	Arrays.stream(population)
		                    .map(boolean[]::clone)
		                    .toArray(boolean[][]::new);;

		crossover( next );
		mutate( next );

		return next;
	}

	private static boolean[][] merge(boolean [][]previous, boolean [][]next) {
		double []fitnessPrevious = evaluate( previous );
		double []fitnessNext = evaluate( next );

		double []fitness = new double[fitnessPrevious.length + fitnessNext.length];
		System.arraycopy(fitnessPrevious, 0, fitness, 0, fitnessPrevious.length);
		System.arraycopy(fitnessNext, 0, fitness, fitnessPrevious.length, fitnessNext.length);

		boolean[][] population = new boolean[previous.length + next.length][];
		System.arraycopy(previous, 0, population, 0, previous.length);
		System.arraycopy(next, 0, population, previous.length, next.length);

		int size = Math.min(population.length, fitness.length);
		for(int i=0; i<size; i++) {
			for(int j=i; j<size-1; j++) {
				if(fitness[j] > fitness[j+1]) {
					continue;
				}

				double value = fitness[j];
				fitness[j] = fitness[j+1];
				fitness[j+1] = value;

				boolean individual[] = population[j];
				population[j] = population[j+1];
				population[j+1] = individual;
			}
		}

		boolean [][] result =  Arrays.copyOfRange(population, 0, previous.length);

		return result;
	}

	public static void main(String[] args) {
		initialize(population);
		for(long g=0L; g<GENERATIONS; g++) {
			population = merge(population, mutate(crossover(generate(population))));
		}
		System.out.println( Arrays.toString(population[0]) );
		System.out.println( Arrays.toString(evaluate(population[0])) );
	}
}
