import java.util.Arrays;
import java.util.Random;

/**
 * Simple (skeleton) GA for the OneMax problem.
 * 
 * @author Fernando Otero
 * @version 1.1
 */
public class GA
{
	/*mutation or crossover*/
	private static final double operatorP=0.5;
	/*mutation rate*/
	private static final double mutationR=0.1;
    /**
     * Number of bits of the individual encoding.
     */
    private static final int BITS = 10;
    
    /**
     * The population size.
     */
    private static final int POPULATION_SIZE = 10;
    
    /**
     * The number of generations.
     */
    private static final int MAX_GENERATION = 5;
    
    /**
     * Random number generation.
     */
    private Random random = new Random();
        
    /**
     * The current population;
     */
    private boolean[][] population = new boolean[POPULATION_SIZE][BITS];
    
    /**
     * Fitness values of each individual of the population.
     */
    private int[] fitness = new int[POPULATION_SIZE];
    
    /**
     * Starts the execution of the GA.
     */
    public void run() {
        //--------------------------------------------------------------//
        // initialises the population                                   //
        //--------------------------------------------------------------//
        initialise();
        
        //--------------------------------------------------------------//
        // evaluates the propulation                                    //
        //--------------------------------------------------------------//
        evaluate();
        
        boolean[][] newGenGroup=new boolean[POPULATION_SIZE][BITS];
        for (int g = 0; g < MAX_GENERATION; g++) {
            //----------------------------------------------------------//
            // creates a new population                                 //
            //----------------------------------------------------------//
        	boolean[][] offsprings=new boolean[2][BITS];	double whichOperator;
        	boolean[] offspring=new boolean[BITS];
        	
        	for(int i=0;i<POPULATION_SIZE;i++) {
        		whichOperator=random.nextDouble();
        		if(operatorP<=whichOperator) {
        			offsprings=crossover(select(),select());	i+=1;
        			for(int j=0;j<2 && i+j<POPULATION_SIZE;j++) 
            			newGenGroup[i+j]=offsprings[j];
        		}else {
        			offspring=mutation(select());
        			newGenGroup[i]=offspring;
        		}
        	}
            
            //----------------------------------------------------------//
            // evaluates the new population                             //
            //----------------------------------------------------------//
        	System.out.println(g+1+"th"+" GEN\n");
        	for(int i=0;i<POPULATION_SIZE;i++)
        		System.out.println(i+1+"th :"+Arrays.toString(newGenGroup[i]));
        	population=newGenGroup;
            evaluate(); //update fitness -> update select()
        }
        
        // prints the value of the best individual
        int bestIndex=0;
        for(int i=1;i<POPULATION_SIZE;i++) {
        	if(fitness[bestIndex]<fitness[i])
        		bestIndex=i;
        }
        System.out.println("best fitness index: "+bestIndex+Arrays.toString(newGenGroup[bestIndex]));
    }
    
    /**
     * Retuns the index of the selected parent using a roulette wheel.
     * 
     * @return the index of the selected parent using a roulette wheel.
     */
    private int select() {
        // prepares for roulette wheel selection
        double[] roulette = new double[POPULATION_SIZE];
        double total = 0; //total fitness
            
        for (int i = 0; i < POPULATION_SIZE; i++) {
            total += fitness[i];
        }
            
        double cumulative = 0.0;
            
        for (int i = 0; i < POPULATION_SIZE; i++) {
            roulette[i] = cumulative + (fitness[i] / total);
            cumulative = roulette[i];
        }
            
        roulette[POPULATION_SIZE - 1] = 1.0; //float pt error
        
        int parent = -1;
        double probability = random.nextDouble();
        
        //selects a parent individual
        for (int i = 0; i < POPULATION_SIZE; i++) {
            if (probability <= roulette[i]) {
                parent = i;
                break;
            }
        }

        return parent; //parent index
    }
    
    /**
     * Initialises the population.
     */
    private void initialise() {
    	System.out.println("INITAIL GEN\n");
    	for(int i=0;i<POPULATION_SIZE;i++) {
    		for(int j=0;j<BITS;j++)
        		population[i][j]=random.nextBoolean();
    		System.out.println(i+1+"th :"+Arrays.toString(population[i]));
    	}
    }
    
    /**
     * Calculates the fitness of each individual.
     */
    private void evaluate() {
    	for(int i=0;i<POPULATION_SIZE;i++) {
    		fitness[i]=count(population[i]);
    		System.out.println(i+1+"th ->"+fitness[i]);
    	}
    }
    /*count trues of given array*/
    private int count(boolean[] arr) {
    	int sum=0;
    	for(int i=0;i<BITS;i++)
    		sum+=(arr[i])?1:0;
    	return sum;
    }
    
    /*first=index of parentA, second=index of parentB*/
    private boolean[][] crossover(int first, int second) {
    	boolean[][] offsprings=new boolean[2][BITS];
    	int crossoverPT;
    	for(int i=0;i<2;i++) {
    		crossoverPT=random.nextInt(BITS-1)+1; //inherit at least one Chromosome from parents
    		for(int j=0;j<BITS;j++) {
    			if(j>=crossoverPT) {
    				offsprings[i][j]=population[second][j];
    				continue;
    			}
    			offsprings[i][j]=population[first][j];
    		}
    	}
    	return offsprings;
    }
    
    /*parent=index of parent*/
    private boolean[] mutation(int parent) {
    	boolean[] offspring=population[parent];
    	boolean mutated=false; double prob; int index=0;
    	while(!mutated && index<BITS) {
    		if(mutated)
    			break;
    		prob=random.nextDouble();
    		if(prob<=mutationR) {
    			offspring[index]=!offspring[index];
    			mutated=true;
    		}
    		index++;
    	}
    	return offspring;
    }
}