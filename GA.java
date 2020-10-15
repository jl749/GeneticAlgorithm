import java.util.*; 

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
	/*tournament size*/
	private static final int Tsize=10;
    /**
     * Number of bits of the individual encoding.
     */
    private static final int BITS = 5;
    
    /**
     * The population size.
     */
    private static final int POPULATION_SIZE = 10;
    
    /**
     * The number of generations.
     */
    private static final int MAX_GENERATION = 10;
    
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
        evaluate(); System.out.println();
        
        boolean[][] newGenGroup=new boolean[POPULATION_SIZE][BITS];
        for (int g = 0; g < MAX_GENERATION; g++) {
            //----------------------------------------------------------//
            // creates a new population                                 //
            //----------------------------------------------------------//
        	boolean[][] offsprings=new boolean[2][BITS];	double whichOperator;
        	boolean[] offspring=new boolean[BITS];
        	
        	int bestIndex=0;
            for(int i=1;i<POPULATION_SIZE;i++) {
            	if(fitness[bestIndex]<fitness[i])
            		bestIndex=i;
            }
        	newGenGroup[0]=population[bestIndex];
        	for(int i=1;i<POPULATION_SIZE;i++) {
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
        	System.out.println("<GEN "+g+">");
        	for(int i=0;i<POPULATION_SIZE;i++) {
        		System.out.println(i+1+"th :"+Arrays.toString(newGenGroup[i]));
        	}
        	population=newGenGroup;
            evaluate(); //update fitness -> update select()
            
            // prints the value of the best individual
            for(int i=1;i<POPULATION_SIZE;i++) {
            	if(fitness[bestIndex]<fitness[i])
            		bestIndex=i;
            }
            System.out.println("best fitness index: "+bestIndex+Arrays.toString(newGenGroup[bestIndex])+" "+fitness[bestIndex]+"\n");
        }
    }
    
    /**
     * Retuns the index of the selected parent using a tournament selection
     * 
     * @return the index of the selected parent using a tournament selection.
     */
    private int select() {
        Set<Integer> arena=new HashSet<Integer>();
        
    	while(arena.size()<Tsize) {
    		arena.add(random.nextInt(POPULATION_SIZE));
    	}
    	
    	int winner=arena.iterator().next();
    	for(int a:arena) {
    		if(winner<a)
    			winner=a;
    	}
    	
    	return winner;
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
    /*uniform crossover*/
    private boolean[][] crossover(int first, int second) {
    	boolean[][] offsprings=new boolean[2][BITS];
    	for(int j=0;j<BITS;j++) {
    		double which=random.nextDouble();
    		if(which>=0.5) {
    			offsprings[0][j]=population[second][j];
    			offsprings[1][j]=population[first][j];
    			continue;
    		}
    		offsprings[0][j]=population[first][j];
    		offsprings[1][j]=population[second][j];
    	}
    	
    	return offsprings;
    }
    
    /*parent=index of parent*/
    /*Bit string mutation*/
    private boolean[] mutation(int parent) {
    	boolean[] offspring=population[parent];
    	double prob; int index=0;
    	while(index<BITS) {
    		prob=random.nextDouble();
    		if(prob<=mutationR) {
    			offspring[index]=!offspring[index];
    		}
    		index++;
    	}
    	return offspring;
    }
}