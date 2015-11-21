import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NQueen
{
	private static final int SIZE = 8;							// Board Size/No. of Queens.
	private static final int MAX_POP = 16;						// Maximum population.
	private static final int MAX_GEN = 5000;					// Maximum generations.

	private static ArrayList<Gene> population = new ArrayList<Gene>();
	private static int crossover_count = 0;

	private static class Gene
	{
		private int state[] = new int[SIZE];
		private int fitness = 0;

		public void gene()
		{
			for(int i=0; i<SIZE; i++)
				this.state[i] = i;
			calcFitness();
			return;
		}
		public void setState(int[] g)
		{
			calcFitness();
			state = g;
			return;
		}
		public int[] getState()
		{
			return state;
		}
		public int getFitness()
		{
			return fitness;
		}
		public void setFitness(int f)
		{
			fitness = f;
			return;
		}
		public void randomInitialize()
		{
			for(int i=0; i<SIZE; i++)
			{
				state[i] = getRandomNum(0,8);
			}
			calcFitness();
			return;
		}
		public void calcFitness()
		{
			fitness = 0;
			for(int i=0; i<SIZE; i++)
			{
				for(int j=i; j<SIZE; j++)
				{
					if(i==j)	continue;
					if(state[i]==state[j])	continue;
					if(Math.abs(state[i]-state[j])==Math.abs(i-j))	continue;
					fitness++;
				}
			}
		}
		public void mutate()
		{
			state[getRandomNum(0,8)] = getRandomNum(0,8);
			calcFitness();
		}
		public void print()
		{
			System.out.print("[");
			for(int i=0; i<SIZE; i++)
			{
				System.out.print(state[i]+" ");
			}
			System.out.print("] Fitness: "+fitness+"\n");
			return;
		}
	}

	private static int getRandomNum(int st, int en)
	{
		return (int)Math.floor((en - st) * Math.random() + st);
	}

	public static void generatePopulation()
	{
		Gene temp = null;
		for(int i=0; i<MAX_POP; i++)
		{
			temp = new Gene();
			temp.randomInitialize();
			temp.calcFitness();
			population.add(temp);
		}
		return;
	}

	private static void sort()
	{
		Collections.sort(population, new fitnessComp());
	}

	public static int minimum()
	{
		return population.indexOf(Collections.max(population, new fitnessComp()));
	}

	public static int maximum()
	{
		return population.indexOf(Collections.min(population, new fitnessComp()));
	}

	static class fitnessComp implements Comparator<Gene>
	{
	    public int compare(Gene g1, Gene g2) {
	        if(g1.getFitness() < g2.getFitness()){
	            return 1;
	        } else {
	            return -1;
	        }
	    }
	}

	public static void printPopulation()
	{
		for(Gene temp: population)
			temp.print();
		return;
	}
	public static void printBoard(Gene s)
	{
		// Display the board.
		int[] sol = s.getState();
		System.out.println("Board:");
		for(int y = 0; y < SIZE; y++)
		{
		    for(int x = 0; x < SIZE; x++)
		    {
				if(y==sol[x])
				{
					System.out.print("o ");
		        }
		        else
		        {
		        	System.out.print("- ");
		        }
		  	}
		    System.out.print("\n");
        }
        return;
	}

	private static void crossoverAndMutate(Gene parent1, Gene parent2)
	{
		crossover_count++;
		Gene child1 = new Gene();
		Gene child2 = new Gene();
		int crossover_point = getRandomNum(0,8);
		int[] p1 = parent1.getState();
		int[] p2 = parent2.getState();
		int[] c1 = new int[SIZE];
		int[] c2 = new int[SIZE];
		for(int i=0; i<crossover_point; i++)
		{
			c1[i]=p1[i];
			c2[i]=p2[i];
		}
		for(int i=crossover_point; i<SIZE; i++)
		{
			c1[i]=p2[i];
			c2[i]=p1[i];
		}
		child1.setState(c1);
		child1.mutate();
		child2.setState(c2);
		child2.mutate();

		int min = population.size();
		min = minimum();
		if(child1.getFitness() >= population.get(min).getFitness())
		{
			population.set(min, child1);
		}

		min = minimum();
		if(child2.getFitness() >= population.get(min).getFitness())
		{
			population.set(min, child2);
		}

		sort();
		return;
	}

	private static void solve()
	{
		boolean done = false;
		int maxFitness = 0;
		int generation = 0;

		generatePopulation();
		printPopulation();

		while(!done && generation<MAX_GEN)
		{
			System.out.println("\nGeneration "+generation);
			sort();
			//printPopulation();
			for(int i=0; i<MAX_POP-1; i+=1)
			{
				//System.out.println("Crossover and Mutate "+i+" and "+(i+1)+"..");
				crossoverAndMutate(population.get(i),population.get(i+1));
				//population.get(i).print();population.get(i+1).print();
			}

			printPopulation();
			maxFitness = maximum();
			//population.get(maxFitness).print();
			if(population.get(maxFitness).getFitness() == SIZE*(SIZE-1)/2)
			{
				done = true;
				System.out.println("Solution Found..");
			}
			generation++;
		}

		System.out.println("\n\nTotal No. of generations: "+generation);
		population.get(maxFitness).print();
		printBoard(population.get(maxFitness));
		return;
	}

	public static void main(String args[])
	{
		int i=0;
		NQueen n = new NQueen();
		n.solve();
		return;
	}
}