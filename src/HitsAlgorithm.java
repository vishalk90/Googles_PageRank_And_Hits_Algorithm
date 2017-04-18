/*
 * Created by vishal kulkarni on 4/14/17
 */

import java.io.*;
import java.util.Scanner;

public class HitsAlgorithm {

    private static int AdjacencyMatrix[][];
    private static String initialVal;
    private static int iteration;
    private static int size;
    private static double authorityVector[];
    private static double previous_authorityVector[];
    private static double hubVector[];
    private static double previous_hubVector[];
    private static double errorRate;
    private static boolean smallGraph = true;
    //private int length;

    // method to create a Adjacency matrix
    private static void createAdjacencyMatrix(int i, int j) {

        AdjacencyMatrix[i][j] = 1;
    }

    // method to get transpose of given matrix
    private static int[][] getTransposeMatrix(int matrix[][]) {
        int temp[][] = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                temp[i][j] = matrix[j][i];

            }
        }
        return temp;
    }

    // method for dotproduct of given 2 matrices
    private static double[] dotProduct(int Adj[][], double X[]) {
        double temp[] = new double[size];
        double sum = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (Adj[i][j] == 1) {
                    sum += (double) Adj[i][j] * (X[j]);
                }
            }
            temp[i] = sum;
            sum = 0;
        }

        return temp;
    }

    // method to do scaling of given matrix
    private static double[] scaling(double X[]) {
        Double sum = 0.0;
        double temp[] = new double[size];
        for (int i = 0; i < size; i++) {
            //sum += Math.pow(Double.parseDouble(X[i]),2);
            sum += Math.pow(X[i], 2);
        }
        sum = Math.sqrt(sum);
        for (int i = 0; i < size; i++) {
            temp[i] = (X[i] / sum);
        }
        return temp;
    }

    // method to print given matrix
    private static void printMatrix(String A[][]) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(A[i][j] + " ");
            }
            System.out.println();
        }
    }


    public static void main(String[] args) {
        //long startTime = System.currentTimeMillis();
        if (args.length != 3) {
            System.out.println("Please enter: HitsAlgorithm iterations initialvalue filename");
            return;
        }
        try {
            iteration = Integer.parseInt(args[0]);

            initialVal = args[1];

            String fileName = args[2];

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
                //initialVal = new DecimalFormat("#.#######").format((1 / ((float) size)));
                initialVal = String.valueOf((1 / ((float) size)));
                //System.out.println(initialVal);
            } else if (initialVal.equals("-2")) {
                //initialVal = new DecimalFormat("#.#######").format((1 / ((float) Math.sqrt(size))));
                initialVal = String.valueOf((1 / ((float) Math.sqrt(size))));
                //initialVal = (1/Math.sqrt(size));
            }


            //creating authority vector matrix of size
            authorityVector = new double[size];
            previous_authorityVector = new double[size];

            //creating hub vector matrix of size
            hubVector = new double[size];
            previous_hubVector = new double[size];

            // initializing vectors with initialVal if its initial value is not 0
            if (!(initialVal.equals("0"))) {
                //initializing authority matrix with initial value
                for (int i = 0; i < size; i++) {
                    authorityVector[i] = Double.parseDouble(initialVal);
                }

                //initializing hub matrix with initial value
                for (int i = 0; i < size; i++) {
                    hubVector[i] = Double.parseDouble(initialVal);
                }
            }

            //reading data from input file and creating a adjacency matrix
            while (sc.hasNext()) {
                int i = sc.nextInt();
                int j = sc.nextInt();
                createAdjacencyMatrix(i, j);
            }

            //getting the transpose matrix
            int AdjacencyMatrix_T[][] = getTransposeMatrix(AdjacencyMatrix);


            ////////////////////////////////////////////////////////////////

//            printing a and h vector matrix
//            System.out.println("A ---> H");
//            for (int i = 0; i < size; i++) {
//                System.out.print(authorityVector[i] + " ");
//                System.out.print(hubVector[i] + " ");
//                System.out.println();
//            }

            ////////////////////////////////////////////////////////////////
            if (smallGraph) {
                System.out.print("Base : 0 : ");
                for (int i = 0; i < size; i++) {
                    System.out.printf("A/H[" + i + "]=" + "%.7f" + "/" + "%.7f ", authorityVector[i], hubVector[i]);
                }
                System.out.println();
            }

            // adding condition of iteration and errorRate depending of value
            if (iteration < 0) {
                errorRate = Math.pow(10, (iteration));
            } else if (iteration == 0) {
                errorRate = Math.pow(10, -5);
            }

            boolean breakingCondition = true; // breaking condition for following while loop
            int count = 1;  // initializing count for iterations

            // assigning values to previous authority and hub vectors
            for (int i = 0; i < size; i++) {
                previous_authorityVector[i] = authorityVector[i];
                previous_hubVector[i] = hubVector[i];
            }

            while (breakingCondition) {

                for (int i = 0; i < size; i++) {
                    authorityVector[i] = 0.0;
                }

                // finding the dot product of Adjacency matrix and hub vector
                authorityVector = dotProduct(AdjacencyMatrix_T, hubVector);


                for (int i = 0; i < size; i++) {
                    hubVector[i] = 0.0;
                }
                // finding the dot product of Adjacency matrix and Authority vector
                hubVector = dotProduct(AdjacencyMatrix, authorityVector);

                //performing scaling operation on both the vectors
                authorityVector = scaling(authorityVector);
                hubVector = scaling(hubVector);

                if (smallGraph) {
                    System.out.print("Iter : " + (count) + " : ");
                    for (int i = 0; i < size; i++) {
                        System.out.printf("A/H[" + i + "]=" + "%.7f" + "/" + "%.7f ", authorityVector[i], hubVector[i]);
                    }

                    System.out.println();
                }

                if (iteration > 0) {

                    if (count == iteration) {
                        breakingCondition = false;
                    }
                } else {
                    int aCount = 0;
                    int hCount = 0;
                    for (int i = 0; i < size; i++) {
                        if ((Math.abs(previous_authorityVector[i] - authorityVector[i]) < errorRate) || String.valueOf(authorityVector[i]).equals("NaN") ) {
                            aCount++;
                        }
                        if ((Math.abs(previous_hubVector[i] - hubVector[i]) < errorRate) || String.valueOf(hubVector[i]).equals("NaN")) {
                            hCount++;
                        }
                        previous_authorityVector[i] = authorityVector[i];
                        previous_hubVector[i] = hubVector[i];
                    }
                    if (aCount == size && hCount == size) {
                        breakingCondition = false;
                        if (!smallGraph) {
                            //printing only last Iteration value if N > 10
                            System.out.println("Iter : " + (count));
                            for (int i = 0; i < 4; i++) {
                                System.out.printf("A/H[" + i + "]=" + "%.7f" + "/" + "%.7f ", authorityVector[i], hubVector[i]);
                                System.out.println();
                            }
                            System.out.println("...other vertices omitted");
                        }

                    }
                }
                count++;
            }

            //System.out.println("total Time : " + (System.currentTimeMillis() - startTime));

        } catch (
                FileNotFoundException e)

        {
            e.printStackTrace();
        }


    }
}
