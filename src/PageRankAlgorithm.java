import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by vishal kulkarni on 4/15/17
 */
public class PageRankAlgorithm {
    private static int AdjacencyMatrix[][];
    private static String initialVal;
    private static int iteration;
    private static int size;
    private static double DVector[];
    private static double Src[];
    private static double errorRate;
    private static boolean smallGraph = true;
    private static double d = 0.85;
    private static int C[];


    // method to create a Adjacency matrix
    private static void createAdjacencyMatrix(int i, int j) {

        AdjacencyMatrix[i][j] = 1;
    }


    public static void main(String[] args) {
        //long startTime = System.currentTimeMillis();
        if (args.length != 3) {
            System.out.println("Please enter: PageRankAlgorithm iterations initialvalue filename");
            return;
        }
        try {
            iteration = Integer.parseInt(args[0]);

            initialVal = args[1];

            String fileName = args[2];

            if (!(Integer.parseInt(initialVal) >= -2 && Integer.parseInt(initialVal) <= 1)) {
                System.out.println("Please enter initialvalue as 0, 1, -1 or -2");
                return;
            }
            // setting the input file path and creating an object of file input stream

            Scanner sc = new Scanner(new File(fileName));

            size = sc.nextInt(); // getting the size of adjacency matrix
            int length = sc.nextInt();

            // creating the adjacency
            AdjacencyMatrix = new int[size][size];


            // if N is greater than 10 then set iteration to 0 and initial value to -1
            if (size > 10) {
                iteration = 0;
                initialVal = "-1";
                smallGraph = false;
            }

            // checking conditions for initial value
            if (initialVal.equals("-1")) {

                initialVal = String.valueOf((1 / ((float) size)));
                //System.out.println(initialVal);
            } else if (initialVal.equals("-2")) {

                initialVal = String.valueOf((1 / ((float) Math.sqrt(size))));
                //initialVal = (1/Math.sqrt(size));
            }

            //creating authority vector matrix of size
            DVector = new double[size];
            Src = new double[size];

            for (int i = 0; i < size; i++) {
                DVector[i] = 0.0;
                Src[i] = 0.0;
            }


            // initializing vectors with initialVal if its initial value is not 0
            if (!(initialVal.equals("0"))) {
                //initializing authority matrix with initial value
                for (int i = 0; i < size; i++) {
                    Src[i] = Double.parseDouble(initialVal);
                }

            }

            //reading data from input file and creating a adjacency matrix
            while (sc.hasNext()) {
                int i = sc.nextInt();
                int j = sc.nextInt();
                createAdjacencyMatrix(i, j);
            }


            // creating C i.e. out degree of perticular node
            C = new int[size];
            for (int i = 0; i < size; i++) {
                C[i] = 0;
                for (int j = 0; j < size; j++) {
                    C[i] += AdjacencyMatrix[i][j];
                }
            }

            if (smallGraph) {
                System.out.print("Base : 0 : ");
                for (int i = 0; i < size; i++) {
                    System.out.printf("P[" + i + "]=" + "%.7f" + " ", Float.parseFloat(String.valueOf(Src[i])));
                }
                System.out.println();
            }

            // adding condition of iteration and errorRate depending of value
            if (iteration < 0) {
                errorRate = Math.pow(10, iteration);
            } else if (iteration == 0) {
                errorRate = Math.pow(10, -5);
            }

            boolean breakingCondition = true; // breaking condition for following while loop
            int count = 1;  // initializing count for iterations


            while (breakingCondition) {

                // Initializing DVector with 0
                for (int i = 0; i < size; i++) {
                    DVector[i] = 0.0;
                }


                // finding the D vector based on Src and C i.e. out degree
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (AdjacencyMatrix[j][i] == 1) {
                            DVector[i] = (DVector[i]) + (Src[j] / C[j]);
                        }
                    }
                }


                //finding page rank for all the nodes
                for (int i = 0; i < size; i++) {
                    DVector[i] = (d * (DVector[i]) + ((1 - d) / (float) size));
                }


                if (smallGraph) {
                    System.out.print("Iter : " + (count) + " : ");
                    for (int i = 0; i < size; i++) {
                        System.out.printf("P[" + i + "]=" + "%.7f" + " ", Float.parseFloat(String.valueOf(DVector[i])));
                    }

                    System.out.println();
                }


                if (iteration > 0) {
                    if (count == iteration) {
                        breakingCondition = false;
                    }
                } else {
                    int D_Count = 0;
                    for (int i = 0; i < size; i++) {
                        if (Math.abs((Src[i]) - DVector[i]) < errorRate) {
                            D_Count++;
                        }

                    }
                    if (D_Count == size) {
                        breakingCondition = false;
                        if (!smallGraph) {
                            //printing only last Iteration value if N > 10
                            System.out.println("Iter : " + (count));
                            for (int i = 0; i < 4; i++) {
                                System.out.printf("P[" + i + "]=" + "%.7f" + " ", Float.parseFloat(String.valueOf(DVector[i]) + " "));
                                System.out.println();
                            }
                            System.out.println("...other vertices omitted");
                        }
                    }
                }
                count++;
                for (int i = 0; i < size; i++) {
                    Src[i] = DVector[i];
                }
            }
            //System.out.println("total Time : " + (System.currentTimeMillis() - startTime));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
