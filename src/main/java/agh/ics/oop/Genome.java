package agh.ics.oop;

import java.util.*;

public class Genome {
    public ArrayList<Integer> genes = new ArrayList<>();
    private final int length = 32;


    //Constructor for Adam & Ewa type Genes
    public Genome() {
        Random random = new Random();

        for( int i = 0; i < length; i++) {
            int newGene = random.nextInt((int) 8);
            genes.add(newGene);
        }
        genes.sort(Integer::compare);
    }

    //Constructor for next generations Genes
    public Genome(ArrayList<Integer> parent1, ArrayList<Integer> parent2, int energy1, int energy2) {
        Random random = new Random();
        int side = random.nextInt((int) 2);
        float proportion = ((float)energy1/(energy1 + energy2)) * length;
        if(side == 0) {
            genes = create_child(parent1, parent2, (int) proportion);
//            System.out.println((int) proportion);
//            System.out.println(side);
        } else {
            genes = create_child(parent2, parent1, length - (int) proportion);
//            System.out.println(length - (int) proportion);
        }

    }

    private ArrayList<Integer> create_child(ArrayList<Integer> parent1, ArrayList<Integer> parent2, int proportion) {
        ArrayList<Integer> child_gene = new ArrayList<>();
        for(int i = 0; i < length; i++) {
            if(i <= proportion) {
                child_gene.add(parent1.get(i));
            } else {
                child_gene.add(parent2.get(i));
            }
        }
        return child_gene;
    }
}
