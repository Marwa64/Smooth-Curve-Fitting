import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


// This will store the x and y coordinates of each point
class Pair {
	 private float x, y;
	
	 public Pair() {
		 x = 0;
		 y = 0;
	 }
	 public Pair(double _x, double _y) {
	     x   = (float) (_x);
	     y = (float)(_y);
	 }
	
	 public void setX(float _x) {  x = _x; }
	 public void setY(float _y) { y = _y; }
	 public float getX()   { return x; }
	 public float getY() { return y; }
}


public class CurveFitting {

	private ArrayList<ArrayList<Float>> People;
	ArrayList<Pair> Points;
	private int Degree;
	private static int Population;
	private static int MaximumGeneration;
	
	public CurveFitting(ArrayList<Pair> points, int degree, int population, int maximumGeneration) {
		Points = points;
		Degree = degree;
		Population = population;
		MaximumGeneration = maximumGeneration;

		People = new ArrayList<ArrayList<Float>>();
	}
	
	public void findBestIndividual() {
		generatePeople();
		
		//go forward till reach generation limit
		for (int i = 0; i < MaximumGeneration; i++) {
			ArrayList<ArrayList<Float>> parents = getParents();
			ArrayList<ArrayList<Float>> crossedover = crossover(parents.get(0), parents.get(1));
			mutate(crossedover, i);
		}
		getBestChild(People);
	}
	
	private void generatePeople() {
		ArrayList<Float> coefficients;
		
		for (int i = 0; i < Population; i++) {
			coefficients = new ArrayList<Float>();
			
			for (int j = 0; j < Degree + 1; j++) {
				Random r = new Random();
				float num =  -10 + r.nextFloat() * (10 - (-10));
				coefficients.add(num);
			}
			People.add(coefficients);
		}
	}
	
	private void getBestChild(ArrayList<ArrayList<Float>> Persons) {
		double bestFitness = getFitness(Persons.get(0));
		ArrayList<Float> bestChild = null;
		
		for (int i = 1; i < Persons.size(); i++) {
			double newFitness = getFitness(Persons.get(i));
			if (newFitness <= bestFitness) {
				bestChild = Persons.get(i);
				bestFitness = newFitness;
			}
		}
	    try {
	        FileWriter myWriter = new FileWriter("Solution.txt", true);
			for (int x = 0; x < bestChild.size(); x++) {
				System.out.print(bestChild.get(x) + ", ");
				myWriter.write(bestChild.get(x) + ", ");
			}
	        myWriter.write("Error = " + bestFitness);
	        System.out.println("Error = " + bestFitness);
	        myWriter.write("\n");
	        myWriter.close();
	      } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }

	}
	
	private double getFitness(ArrayList<Float> Chromosome) {
		double total, fitness = 0;

		for (int i = 0; i < Points.size(); i++) {
			total = 0;
			for (int j = 0; j < Chromosome.size(); j++) {
				total += Chromosome.get(j) * Math.pow(Points.get(i).getX(), j);
			}
			total -= Points.get(i).getY();
			fitness += Math.pow(total, 2);
		}

		return fitness / Points.size();
	}
	
	private ArrayList<ArrayList<Float>> getParents() {
		ArrayList<ArrayList<Float>> Parents = new ArrayList<ArrayList<Float>>();
		ArrayList<Pair> cumulativeFitness = new ArrayList<Pair>();
		float totalFitness = 0;
		Pair newPair;
		
		for (int i = 0; i < People.size(); i++) {
			newPair = new Pair();
			newPair.setX(totalFitness);
			totalFitness += getFitness(People.get(i));
			newPair.setY(totalFitness);
			cumulativeFitness.add(newPair);
		}
		
		Random rand = new Random();
		boolean parentFound = false;
		// Get Parent 1
		do {
			float r1 =  rand.nextFloat() * totalFitness;
			for (int i = 0; i < People.size(); i++) {
				if (r1 >= cumulativeFitness.get(i).getX() && r1 < cumulativeFitness.get(i).getY()) {
					Parents.add(People.get(i));
					totalFitness -= getFitness(People.get(i));
					parentFound = true;
					People.remove(i);
					break;
				}
			}
		} while (!parentFound);
		
		parentFound = false;
		// Get Parent 2
		do {
			float r2 =  rand.nextFloat() * totalFitness;
			for (int i = 0; i < People.size(); i++) {
				if (r2 >= cumulativeFitness.get(i).getX() && r2 < cumulativeFitness.get(i).getY()) {
					Parents.add(People.get(i));
					People.remove(i);
					parentFound = true;
					break;
				}
			
			}
		} while (!parentFound);
		
		return Parents;
	}
	
	private ArrayList<ArrayList<Float>> crossover(ArrayList<Float> individual1, ArrayList<Float> individual2) {
		ArrayList<ArrayList<Float>> crossedOver = new ArrayList<ArrayList<Float>>();
		ArrayList<Float> offspring1 = new ArrayList<Float>();
		ArrayList<Float> offspring2 = new ArrayList<Float>();
		
		double random = Math.random() * (individual1.size() - 1);
		double random2 = Math.random() * (individual1.size() - 1);
		
		if(random > random2)
		{
			int tmp = (int)random;
			random = random2;
			random2 = tmp;
			
		}
		for (int i = 0; i < individual1.size(); i++) {
			if (random < i && random2 >= i) {
				offspring1.add(individual1.get(i));
				offspring2.add(individual2.get(i));
			}
			else {
				offspring1.add(individual2.get(i));
				offspring2.add(individual1.get(i));
			}		
		}
		crossedOver.add(offspring1);
		crossedOver.add(offspring2);
		return crossedOver;
	}
	
	private void mutate(ArrayList<ArrayList<Float>> crossedOver, int generation) {
		ArrayList<Float> mutated;
		for (int i = 0; i < crossedOver.size(); i++) {
			mutated = new ArrayList<Float>();
			for (int j = 0; j < crossedOver.get(i).size(); j++) {
				double random = Math.random();
				if (random <= crossedOver.get(i).get(j)) {
					double lowerBound = crossedOver.get(i).get(j) -  (-10);
					double upperBound = 10 - crossedOver.get(i).get(j);
					double y;
					if (Math.random() <= 0.5) {
						y = lowerBound;
					} else {
						y = upperBound;
					}
					double power = 1 - (generation / MaximumGeneration);
					float result = (float) (y * (1 - Math.pow(random, power)));
					float newGene;
					if(y == lowerBound) newGene = crossedOver.get(i).get(j) - result;
					else  newGene = crossedOver.get(i).get(j) + result;
					mutated.add(newGene);
				} else {
					mutated.add(crossedOver.get(i).get(j));
				}
			}
			People.add(mutated);
		}
	}
}