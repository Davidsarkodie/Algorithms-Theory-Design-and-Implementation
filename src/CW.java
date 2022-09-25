/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *
 * @author User
 */

public class CW {

    int Verticies;
    private int Vertex;
    private float[][] Adj;
    int Start;
    int Finish;
    int Weight;

    public CW(int Start, int Finish, int Weight) {
        //the ints are ran through the Edge method 
        this.Start = Start;
        this.Finish = Finish;
        this.Weight = Weight;
    }

    LinkedList<CW>[] AdjList;

    public CW(int Vertex) {
        this.Vertex = Vertex;
        Adj = new float[Vertex][Vertex];
        for (int f = 0; f < Vertex; f++) {
            for (int g = 0; g < Vertex; g++) {
                Adj[f][g] = 0;
            }
        }
        this.Verticies = Vertex;
        AdjList = new LinkedList[Vertex];
        //initialising adjacency lists for all the V
        for (int f = 0; f < Vertex; f++) {
            AdjList[f] = new LinkedList<>();
        }
    }
    
    public int GetVertex() {
            return Vertex;

        }

        public float[][] GetAdj() {
            return Adj;
        }
        
    public void AddEdge1(int f, int g, float weight) {
        Adj[f][g] = weight;

    }

    public void RemoveEdge(int f, int g) {
        Adj[f][g] = 0;
    }

    public boolean HoldEdge(int f, int g) {
        if (Adj[f][g] != 0) {
            return true;
        }
        return false;
    }

    public void AddEdge12(int Start, int Finish, int Weight) {
        //adds edge
        CW edge = new CW(Start, Finish, Weight);
        //Directed graph
        AdjList[Start].addFirst(edge);
    }

    public LinkedList<Integer> nextorder(int vertex) {
        LinkedList<Integer> Edges = new LinkedList<Integer>();
        for (int f = 0; f < Vertex; f++) {
            if (HoldEdge(vertex, f)) {
                Edges.add(f);
            }
        }
        return Edges;
    }

    public void printRGraph() {
        for (int s = 0; s < Verticies; s++) {
            LinkedList<CW> list = AdjList[s];
            //for loop running through the list size printing the vertex and the weight
            for (int d = 0; d < list.size(); d++) {
                 System.out.println("Node of " + s+ " is weighted by " + ""+list.get(d).Weight+"" + " to Node of " + list.get(d).Finish );
            }
        }
    }

    public void Readit() {
        try {
            /*creates a new object file reading the data from specified
                file passing through the scanner object*/
            File A_reads = new File("Bridge_1.txt");
            Scanner FileReader = new Scanner(A_reads);
            /*int a is set to 0 beginning at the start of the array
                FileReader.nextline() reads every line in the file 
                printing out all number of nodes in the file producing a graph of all
                the nodes in the file connecting to others and producing their weight */
            int f = 0;
            String deats = FileReader.next();
            System.out.println("Here are all the number of nodes, \nthese nodes are connected including their weight\n'V' are their representation");
            System.out.println("");
            System.out.println("      Amount of vertex: " + deats);
            System.out.println("");
            while (FileReader.hasNextLine()) {
                String data = FileReader.nextLine();
                String STRINGARRAY[] = data.split(" ");
                /*prints number of nodes in the file. Produces a graph of all
                nodes in the file connecting to others nodes whiles the weight
                    converts the strings in the arrays into ints*/
                if (f != 0) {
                    int Vertex;
                    int Node;
                    int weight;

                    Vertex = Integer.parseInt(STRINGARRAY[0]);
                    Node = Integer.parseInt(STRINGARRAY[1]);
                    weight = Integer.parseInt(STRINGARRAY[2]);
                    AddEdge1(Vertex, Node, weight);
                    AddEdge12(Vertex, Node, weight);
                }
                f++;
            }
           
            FileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist");
            e.printStackTrace();
        }
    }

    public static int FordFulkerson(CW g, int base, int point) {
       
        if (base == point) {
            return 0;
        }
        int N = g.GetVertex();

        // Residual graph created
        CW bn = new CW(N);
        for (int s = 0; s < N; s++) {
            for (int d = 0; d < N; d++) {
                bn.GetAdj()[s][d] = g.GetAdj()[s][d];
            }
        }

        // When ran the path will be stored which is accomplished by the BFS
        int author[] = new int[N];

        int maxflow = 0; // max flow value

        // while a path exists from base to point loop
        while (BFS(bn, base, point, author)) {
            // to save path flow
            float pathflow = Float.MAX_VALUE;
            
             ArrayList<String> paths = new ArrayList<>();
             ArrayList<String> weights = new ArrayList<>();

             


            // finds max flow of path accomplished by bfs
            for (int s = point; s != base; s = author[s]) {
                int d = author[s];
                pathflow = Math.min(pathflow, bn.GetAdj()[d][s]);
                paths.add(d + " -- " + s);
            }
            
            Collections.reverse(paths);
            Collections.reverse(weights);
            
            System.out.println(FArray(paths, true));

            // update residual graph volume
            // reverse edges
            for (int s = point; s != base; s = author[s]) {
                int d = author[s];
                bn.GetAdj()[d][s] -= pathflow;
                bn.GetAdj()[s][d] += pathflow;
            }
            
            System.out.println("maxFlow: " + Math.round(pathflow));

            // Adds path flow to max flow
            maxflow += pathflow;

        }

        return maxflow; //returns max flow
    }
    
    public static String FArray(ArrayList arr, boolean toggle) {
        String formattedArray;

        if (toggle) {
            formattedArray = arr.toString().replace(",", " --");
        } else {
            formattedArray = arr.toString().replace(",", " |");
        }

        formattedArray = formattedArray.replace("[", "");
        formattedArray = formattedArray.replace("]", "");

        return formattedArray;
    }
    

    public static boolean BFS(CW bn, int base, int point, int author[]) {
        // array to store visited nodes
        boolean[] bonda = new boolean[bn.GetVertex()];
        for (int s = 0; s < bn.GetVertex(); s++) {
            bonda[s] = false;
        }

        LinkedList<Integer> t = new LinkedList<Integer>(); // for queue

        // visits base
        t.add(base);
        bonda[base] = true;
        author[base] = -1;

        // loops through all nodes
        while (!t.isEmpty()) {
            int s = t.poll();
            // scans for the next nodes
            for (Integer d : bn.nextorder(s)) {
                // if not then visit
                if ((bonda[d] == false) && (bn.GetAdj()[s][d] > 0)) {
                    t.add(d);
                    bonda[d] = true;
                    author[d] = s;
                }
            }
        }

        // return boolean that tells us if we ended up at the checkpoint
        return bonda[point];
    }

    public static void main(String[] args) {
        CW b = new CW(9000);


        b.Readit();
        b.printRGraph();
        long start = System.currentTimeMillis();
        
        // Ford-Fulkerson Max Flow Algorithm
        System.out.println("");
        System.out.print("The max flow of the algorithm is: ");
        System.out.println("The Ford Fulkerson flow is " + FordFulkerson(b, 0, 5));
        
        long now = System.currentTimeMillis();
        double elapsed = (now - start) / 1000.0;
        System.out.println("Elapsed time = " + elapsed + " seconds");
    }
}
