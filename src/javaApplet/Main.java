package javaApplet;

import java.io.*;
import java.util.*;

public class Main {
	public static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	public static Random r = new Random();
	
	public static Scanner reader = new Scanner(System.in);
	
	public static void main(String[] args) throws InterruptedException, IOException{
		
		Board board = new Board(30, 30);

		Hero hero = new Hero("hi");
		board.setHero(hero);
//		Hero hero1 = new Hero("hi1");
//		board.setHero(hero1);
//		Hero hero2 = new Hero("hi2");
//		board.setHero(hero2);
		while (true) {
			
			System.out.print(board.printBoardHeroView(hero));
			System.out.print("[" + board.printHeroes() + "]");
			System.out.print(board.getHero("hi").printStat() + '\n');
			System.out.print(board.getHero("hi").printSlots());
			
//			char c = reader.next().charAt(0);
//			handleHeroMoveCommand(board, c);
//			
//			if (c == '1' || c == '2' || c == '3' || c == '4' || c == '5') {
//				int index = Character.getNumericValue(c);
//				board.getHero("hi").useItem(index-1);
//			}
			
			try{
				  //do what you want to do before sleeping
				  Thread.currentThread().sleep(1000);//sleep for 1000 ms
				  //do what you want to do after sleeptig
				}
				catch(InterruptedException ie){
				//If this thread was intrrupted by nother thread 
				}
			board.moveMons();
		}
	}
	
	public  static void handleHeroMoveCommand(Board board, char c) throws InterruptedException, IOException {
		int dir = -1;
		if (c == 'w') {
			dir = Unit.DIR_N;
		}
		else if (c == 'd'){
			dir = Unit.DIR_E;
		}
		else if (c == 's') {
			dir = Unit.DIR_S;
		}
		else if (c == 'a'){
			dir = Unit.DIR_W;
		}
		if (dir != -1) {
			board.getHero("hi").move(dir);	
		}
	}
}
