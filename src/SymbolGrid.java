/*

Daniel Moncada
Course Number: CS 2013
Section 05
Description:
The purpose of this lab is to understand recursion and implement backtracking to solve these recursive problems.
We must find specific path/s of a sequence of symbols throughout a grid of many symbols. We must be able to create
a Path through the grid, and if we find we followed the wrong Path, be able to backtrack until we continue back onto
the Path using recursion.
Other Comments
An issue we found in our code is that the same Path may be displayed more than once in the console.We also
found that the new Cell created can sometimes be located next to a past Cell on the Path and not the current Cell.
 */
import java.util.LinkedList;

public class SymbolGrid {
	
	private static char[] SYMBOLS = {'~', '!', '@', '#', '$', '^', '&', '*'};
	


	// NOTE: 
	//   Do not change this method signature.
	//   This method calls another recursive method, but it should
	//   not call itself.
	public static void findAllPaths(Grid grid, char[] sequence) {
		// TO DO: 
		//     Add code to traverse the grid and search for paths 
		//     starting at each cell using the findPathsAt() method.



		//searches through the enitre grid to find any characters that match the first character in the sequence.
		//makes that character on the grid a new Cell and initiates a new Path for each matching symbol on the grid.
		for (int row = 0;row <= grid.getSize()-1; row++){

			for (int col = 0; col <= grid.getSize()-1; col++){

				if (grid.getSymbolAt(row,col) == sequence[0]){

					Cell cells = new Cell(row, col);

					Path currentPath = new Path();

					findPathsAt(grid,cells, currentPath, sequence);


				}

			}

		}



		System.out.println("\n--- finished searching");
	}
	
	// TO DO: 
	//     Implement recursive method with backtracking
	//     
	// NOTE:
	//   You may change the list of parameters here, but before you do
	//   check out all the helpful helper methods in Grid, Cell and Path
	//   below.
	private static void findPathsAt(Grid grid,
                             Cell here,
                             Path currentPath,
                             char[] sequence) {

		//checks to make sure the path's length does not go past the sequence length
		//if it has not, then it adds a new Cell
		if (currentPath.getLength() < sequence.length)
			currentPath.add(here, sequence[currentPath.getLength()]);

		//if the Path's length is the same as the sequence's length and the final symbol matches it displays the Path
		if (currentPath.getLength() == sequence.length && grid.getSymbolAt(here) == sequence[currentPath.getLength()-1])
			currentPath.display();

		//otherwise if the current symbol does not match the symbol in the sequence, it will remove it from the Path
		else if (currentPath.getLength() > 0 && grid.getSymbolAt(here) != sequence[currentPath.getLength()-1])
			currentPath.removeLast();


		//if the other cases don't work, it will proceed to search the locations around the specific cell from the parameter
		else {
			int[][] direction = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, -1}, {1, 1}, {-1, -1}, {-1, 1}};

			for (int i = 0; i < direction.length; i++) {



					int row = here.r + direction[i][0];
					int col = here.c + direction[i][1];

					//if the location is on the grid, it will create a new cell
					if (grid.isOnGrid(row, col)) {
						Cell cellOnPath = new Cell(row, col);

						//if the cell is not already in the Path, recursively find the rest of the path using new Cells.
						if (!currentPath.contains(cellOnPath))
						    findPathsAt(grid, cellOnPath, currentPath, sequence);
					}


			}


		}


	}

	public static void main(String[] args) {
		// NOTE:
		//   You may modify this to let the user choose grid size.
		Grid grid = new Grid(7, SYMBOLS);
		grid.display();
		
		System.out.println();

		// NOTE:
		//   You may modify this to let the use choose length of
		//   the sequence and/or to enter the sequence they want
		//   the program to find.
		char[] seq = randomSymbolSequence(4);
		System.out.print("sequence: "); 
		System.out.println(seq);
		
		System.out.println("\npaths:");
		findAllPaths(grid, seq);


	}
	
	/* Helper methods below -- you shouldn't need to alter them but you can add your own. */
	
	private static char[] randomSymbolSequence(int length) {
		char[] sequence = new char[length];
		for(int i = 0; i < length; i++) {
			sequence[i] = SYMBOLS[(int)(Math.random()*SYMBOLS.length)];
		}
		return sequence;
	}


}

/* Represents a cell on a grid -- just a convenient way of
 * packaging a pair of indices  */
class Cell {
	int r, c;
	
	Cell(int r, int c) {
		this.r = r;
		this.c = c;
	}

	@Override
	public boolean equals(Object o) {
		Cell cell = (Cell) o;
		return this.r == cell.r && this.c == cell.c;
	}
	
	@Override
	public String toString() {
		return "(" + r + ", " + c + ")";
	}
}

/* Represents a path of cells on a grid of symbols. */
class Path {
	private LinkedList<Cell> cells;
	private LinkedList<Character> symbols;

	Path() {
		cells = new LinkedList<Cell>();
		symbols = new LinkedList<Character>();
	}

	int getLength() {
		return cells.size();
	}

	void add(Cell location, char symbol) {
		cells.addLast(location);
		symbols.addLast(symbol);
	}

	void removeLast() {
		cells.removeLast();
		symbols.removeLast();
	}

	boolean contains(Cell cell) {
		return cells.contains(cell);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < cells.size(); i++) {
			sb.append(symbols.get(i));
			sb.append(cells.get(i).toString());
			sb.append("  ");
		}

		return sb.toString();
	}

	void display() {
		System.out.println(toString());
	}


}

/* Represents a grid of symbols. */
class Grid {
	private char[][] grid;

	Grid(int size, char[] symbols) {
		grid = initGrid(size, symbols);
	}

	private char[][] initGrid(int size, char[] symbols) {
		char[][] symbolGrid = new char[size][size];

		for(int row = 0; row < size; row++) {
			for(int col = 0; col < size; col++) {
				// picks a random symbol for each cell on the grid
				symbolGrid[row][col] = symbols[(int)(Math.random() * symbols.length)];
			}
		}

		return symbolGrid;
	}

	int getSize() {
		return grid.length;
	}

	char getSymbolAt(Cell location) {
		return getSymbolAt(location.r, location.c);
	}

	char getSymbolAt(int r, int c) {
		return grid[r][c];
	}

	boolean isOutside(Cell location) {
		return isOutside(location.r, location.c);
	}

	boolean isOutside(int r, int c) {
		return 0 > r || r >= grid.length || 0 > c || c >= grid[r].length;
	}

	boolean isOnGrid(Cell location) {
		return isOnGrid(location.r, location.c);
	}

	boolean isOnGrid(int r, int c) {
		return 0 <= r && r < grid.length && 0 <= c && c < grid[r].length;
	}

	void display() {
		// Display column indices
		System.out.print("\n    ");
		for(int i = 0; i < grid.length; i++) {
			System.out.print(i + "  ");
		}
		System.out.println();

		// Display grid
		for(int r = 0; r < grid.length; r++) {
			// Display row index
			System.out.print("  " + r + " ");
			for(int c = 0; c < grid[r].length; c++) {
				System.out.print(grid[r][c] + "  ");
			}
			System.out.println();
		}
	}
}
