package com.me.mygdxgame;

import java.util.Random;

public class gamelogic {
	private int points;
	private int[] products;
	private int[] answers=new int[3];
	private int difficulty;
	//Constructor
	public gamelogic(int diff) {
		points = 0;
		difficulty = diff;
		//int[] answers = new int[3];
		this.next_question();
	}
	//public int getpoints();
	
	//public void next_question(int difficulty);
	public void next_question() {
		Random rand = new Random();
		int n1 = rand.nextInt(10)+1;
		int n2 = rand.nextInt(10)+1;
		products = new int[2];
		products[0] = n1;
		products[1] = n2;
		int result = n1*n2;
		//int[] answers = new int[3];
		generate_answers(n1,n2);
	}
	
	public void generate_answers(int n1, int n2) {
		int n_methods = 1;
		Random rand= new Random();
		int choice=rand.nextInt(n_methods);
		//int[] answers = new int[3];
		if (choice == 0) {
			answers[0] = n1*n2-1;
			answers[1] = n1*n2+1;
			answers[2] = n1*n2;
			//shuffleArray(answers);
		}
	}
	 //public string getquestion
	public String get_q() {
		String kysymys = String.format("%d * %d?", products[0], products[1]); 
		return kysymys;
	}
	
	public int[] get_answers() {
		return answers;
	}
	//public int[] get_answers
	public int get_right() {
		return products[0]*products[1];
	}
	//public int get_right_answer
	
	//ShuffleArray mb this isnt the place for this...
	 static void shuffleArray(int[] ar)
	 {
	   Random rnd = new Random();
	   for (int i = ar.length - 1; i > 0; i--)
	   {
	     int index = rnd.nextInt(i + 1);
	     // Simple swap
	     int a = ar[index];
	     ar[index] = ar[i];
	     ar[i] = a;
	   }
	 }
	
}
