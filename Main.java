// clear && astyle --style=java --indent=tab *.java && javac Main.java && java Main

import java.util.Random;
import java.util.Arrays;

public class Main {
	private static final Random PRNG = new Random();

	private static double max = 15D;

	private static boolean[] solution = {		false,		false,		false,		false,		false,		false,		false,		false,		false,		false,		false,		false,		false,		false,};

	private static double[] values = {		23,		33,		55,		85,		16,		92,		11,		24,		17,		85,		91,		45,		32,		23,	};

	private static double[] weights = {		3,		3,		5,		5,		6,		2,		1,		4,		7,		5,		1,		5,		2,		3,	};

	private static final int SIZE = 19;
	private static double[] fitness = {};
	private static boolean[][] population = {{}};

	static {
		fitness = new double[SIZE];
		population = new boolean[SIZE][];
		for(int i=0; i<SIZE; i++) {
			population[i] = new boolean[ solution.length ];
		}
	}

	private static void mutate(boolean []individual) {
		int index = PRNG.nextInt(individual.length);
		individual[index] = !individual[index];
	}

	private static void mutate(boolean [][]population) {
		for(boolean []individual : population) {
			mutate( individual );
		}
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

	private static void crossover(boolean [][]population) {
		for(int i=0; i<population.length; i++) {
			int r = PRNG.nextInt(population.length);

			crossover(population[i], population[r]);
		}
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

	private static double[] fitness(boolean [][]population) {
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

	public static void main(String[] args) {
		//System.out.println( Arrays.toString(values) );
		//System.out.println( Arrays.toString(weights) );
		//System.out.println( Arrays.toString(solution) );
		//System.out.println( Arrays.toString(fitness) );
		//mutate(solution);
		//System.out.println( Arrays.toString(solution) );
		//mutate(population);
		//System.out.println( Arrays.deepToString(population) );

		initialize(population);
		System.out.println( Arrays.deepToString(population) );
		fitness = fitness(population);
		System.out.println( Arrays.toString(fitness) );
	}
}
