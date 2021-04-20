package thecursedgame.domain;

import java.util.ArrayList;

public class Levels {
	ArrayList<String[]> levels;
	String[] level1 = {"0000000000000010000000010000000100000100",
	 		 "0000000000000000000000100000001000001000",
	 		 "0000000000000000000000010000000100000100",
	 		 "0000000000000000000000010000000100000100",
	 		 "0000000000000000000000010000000100000100",
	 		 "0000000000000000000000010000000100000100",
	 		 "0000000000000000000000010000000100000100",
	 		 "0000000000000000000000010000000100000100",
	 		 "0000000000000000000000010000000100000100",
	 		 "0000000000000000000000010000000100000100",
	 		 "0000000000000011111100010000000000000100",
	 		 "0000000000000000000000000000000000000100",
	 		 "0000000000000000000000000000000000000100",
	 		 "0000000000000000000000000000000000000100",
	 		 "0000000000000000000000000000000000000100",
	 		 "0000000000000000000000000000000000000100",
	 		 "0000000000000000000000000000000000000100",
	 		 "0000000000000000000000000000000000000100",
	 		 "0000000000000010000000000000000000000100",
	 		 "0000000000000000000000000000000000000100",
	 		 "0000000000011111000000000000000000000100",
	 		 "0000000000100010000000001100000111100100",
	 		 "0000000010000010000000000000000100002100",
	 		 "0000000010000010000033000000000000010011",
	 		 "1111111111111111111111111111111111111111"};
	String[] level2  = {"0000000000000010000000010000000100000100",
	 		 "0000000000000000000000100000001000001000",
	 		 "0000000000000000000000010000000100000100",
	 		 "0000000000000000000000010000000100000100",
	 		 "0000000000000000000000010000000100000100",
	 		 "0000000000000000000000010000000100000100",
	 		 "0000000000000000000000010000000100000100",
	 		 "0000000000000000000000010000000100000100",
	 		 "0000000000000000000000010000000100000100",
	 		 "0000000000000000000000010000000100000100",
	 		 "0000000000000011111100010000000000000100",
	 		 "0000000000000000000000000000000000000100",
	 		 "0000000000000000000000000011110000000100",
	 		 "0000000000000000000000000010000000000100",
	 		 "0000000000000000010100000010000000000100",
	 		 "0000000000000000010100000000000000000100",
	 		 "0000000000000000010100000000000000000100",
	 		 "0000000000001111111111111110000000000100",
	 		 "0000000000000010000000000000000000000100",
	 		 "0000000000000000000000000000000000000100",
	 		 "0000000011111110000000000000000000000100",
	 		 "0000000000000000000000000000000000000100",
	 		 "0000000010000010000000000000000100002100",
	 		 "0000000010000010000000000000000000010011",
	 		 "1111111111111111111111111111111111111111"};
	String[] level3 = {"0000000000000000000000000000000000000000",
	 		 "0000000000000000000000000000001000001000",
	 		 "0000000000000000000000000000000000000100",
	 		 "0000000000000000000000000000000000000100",
	 		 "0000000000000000000000000000000000000100",
	 		 "0000000000000111011111000000000000000100",
	 		 "0000001100100100000100000000000000000100",
	 		 "0000010010100111000100000000000000000100",
	 		 "0000001100110100000100000000000000000100",
	 		 "0000000000000111000100000000000000000100",
	 		 "0000000000000011111100000000000000000100",
	 		 "0000000000000000000000000000000000000100",
	 		 "0000000000000000000000000011110000000100",
	 		 "0000000000000000000000000010000000000100",
	 		 "0000000000000000010100000010000000000100",
	 		 "0000000000000000010100000000000000000100",
	 		 "0000000000000000010100000000000000000100",
	 		 "0000000000001111111111111110000000000100",
	 		 "0000000000000010000000000000000000000100",
	 		 "0000000000000000000000000000000000000100",
	 		 "0000000011111110000000000000000000000100",
	 		 "0000000000000000000000000000000000000100",
	 		 "0000000010000010000000000000000100002100",
	 		 "0000000010000010000000000000000000010011",
	 		 "0000000000000000000000000000000000000000"};
	public Levels() {
		levels = new ArrayList<>();
		levels.add(level1);
		levels.add(level2);
		levels.add(level3);
	}
	public String[] getLevel(int i) {
		return this.levels.get(i);
	}
	public int getSize() {
		return this.levels.size();
	}
}
