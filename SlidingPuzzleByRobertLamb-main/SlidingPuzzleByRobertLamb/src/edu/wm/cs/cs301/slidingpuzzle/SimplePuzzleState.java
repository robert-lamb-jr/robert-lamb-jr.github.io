package edu.wm.cs.cs301.slidingpuzzle;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import static org.junit.Assert.assertNotNull;

import java.security.AccessControlContext;
import org.junit.validator.ValidateWith;


public class SimplePuzzleState implements PuzzleState {

	private int[][] currentState;
	private PuzzleState parentState = null;
	private int emptySlot = 1;
	private Operation o = null;
	private int pathLength = 0;
	
	@Override
	public void setToInitialState(int dimension, int numberOfEmptySlots) {
		// TODO Auto-generated method stub
		// Creates initial matrix to represent the current state
		this.currentState = new int[dimension][dimension];
		this.emptySlot = numberOfEmptySlots;
		// Assigns non-empty values to first 3 rows
		int inc = 1;
		for(int i = 0; i < dimension-1; i++) {
			for(int j = 0; j < dimension; j++) {
				currentState[i][j] = inc; 
				inc++;
				}
			}
		// Assigns values to last row, based on numberOfEmptySlots
		for(int k = 0; k < dimension; k++) {
			if(k >= dimension - numberOfEmptySlots)
				currentState[dimension-1][k] = 0;
			else {
				currentState[dimension-1][k] = inc; 
				inc++;
				}
			}
		
	}

	@Override
	public int getValue(int row, int column) {
		// TODO Auto-generated method stub
		// Simply returns the value at a given row and column
		return this.currentState[row][column];
	}

	@Override
	public PuzzleState getParent() {
		// TODO Auto-generated method stub
		// Returns previous parent state
		return this.parentState;
	}

	@Override
	public Operation getOperation() {
		// TODO Auto-generated method stub
		// Returns current operation
		return this.o;
	}

	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		// Returns current path length
		return this.pathLength;
	}

	@Override
	public PuzzleState move(int row, int column, Operation op) {
		// TODO Auto-generated method stub
		// Calls moveCheck to see if tile can move to given space
		if(!moveCheck(row, column, op))
			return null;
		else {
			int rowChange = 0;
			int colChange = 0;
			SimplePuzzleState newState = new SimplePuzzleState();
			newState.currentState = new int[this.currentState.length][this.currentState.length];
			// Copies current state values to new state
			for(int i = 0; i < currentState.length; i++)
				for(int j = 0; j < currentState.length; j++)
					newState.currentState[i][j] = this.currentState[i][j]; 
			//checks for which operation is being used
			if(op == Operation.MOVERIGHT)
				colChange = 1;
			if(op == Operation.MOVELEFT)
				colChange = -1;
			if(op == Operation.MOVEUP)
				rowChange = -1;
			if(op == Operation.MOVEDOWN)
				rowChange = 1;
			// Moves tile
			newState.currentState[row+rowChange][column+colChange] = newState.currentState[row][column];
			newState.currentState[row][column] = 0; 
			// Establishes instance variables for new state
			newState.parentState = this;
			newState.emptySlot = this.emptySlot;
			newState.o = op;
			newState.pathLength = this.pathLength + 1;
			return newState;
		}
	}

	public boolean moveCheck(int row, int column, Operation op) {
		// Checks if tile can be moved to desired space, returns true if can and false if cannot
		if(op == Operation.MOVERIGHT)
			if(this.currentState[row][column+1] != 0 || column + 1 == this.currentState.length)
				return false;
		if(op == Operation.MOVELEFT)
			if(this.currentState[row][column-1] != 0 || column - 1 < 0)
				return false;
		if(op == Operation.MOVEUP)
			if(this.currentState[row-1][column] != 0 || row - 1 < 0)
				return false;
		if(op == Operation.MOVEDOWN)
			if(this.currentState[row+1][column] != 0 || row + 1 == this.currentState.length)
				return false;
		return true;
	}
	@Override
	public PuzzleState drag(int startRow, int startColumn, int endRow, int endColumn) {
		// TODO Auto-generated method stub
		Operation o1 = null;
		Operation o2 = null;
		int o1_quantity = 0;
		int o2_quantity = 0;
		// Checks to figure out and assign the correct move direction to operation 1 and 2
		if(startRow == endRow) {
			if(startColumn < endColumn) {
				o1 = Operation.MOVERIGHT;
				o1_quantity = endColumn - startColumn;
			}
			if(startColumn > endColumn) {
				o1 = Operation.MOVELEFT;
				o1_quantity = startColumn - endColumn;
			}
		}
		else if(startColumn == endColumn) {
			if(startRow > endRow) {
				o1 = Operation.MOVEUP;
				o1_quantity = startRow - endRow;
			}
			if(startRow < endRow) {
				o1 = Operation.MOVEDOWN;
				o1_quantity = endRow - startRow;
			}
		}
		else if(startRow > endRow) {
			o1 = Operation.MOVEUP;
			o1_quantity = startRow - endRow;
			if(startColumn < endColumn) {
				o2 = Operation.MOVERIGHT;
				o2_quantity = endColumn - startColumn;
			}
			if(startColumn > endColumn) {
				o2 = Operation.MOVELEFT;
				o2_quantity = startColumn - endColumn;
			}
		}
		else if(startRow < endRow) {
			o1 = Operation.MOVEDOWN;
			o1_quantity = endRow - startRow;
			if(startColumn < endColumn) {
				o2 = Operation.MOVERIGHT;
				o2_quantity = endColumn - startColumn;
			}
			if(startColumn > endColumn) {
				o2 = Operation.MOVELEFT;
				o2_quantity = startColumn - endColumn;
			}
		}
		PuzzleState moving_state = this;
		// Executes desired drag movement and returns the newly moved state
		int rowChange = startRow;
		int colChange = startColumn;
		for(int i = 0; i < o1_quantity; i++) {
			moving_state = moving_state.move(rowChange, colChange, o1);
			if(o1 == Operation.MOVERIGHT)
				colChange++;
			if(o1 == Operation.MOVELEFT)
				colChange--;
			if(o1 == Operation.MOVEUP)
				rowChange--;
			if(o1 == Operation.MOVEDOWN)
				rowChange++;
		}
		for(int j = 0; j < o2_quantity; j++) {
			moving_state = moving_state.move(rowChange, colChange, o2);
			if(o2 == Operation.MOVERIGHT)
				colChange++;
			if(o2 == Operation.MOVELEFT)
				colChange--;
			if(o2 == Operation.MOVEUP)
				rowChange--;
			if(o2 == Operation.MOVEDOWN)
				rowChange++;
		}
		return moving_state;
	}

	@Override
	public PuzzleState shuffleBoard(int pathLength) {
		// TODO Auto-generated method stub
		// Shuffles board by checking possible spots to move to and deciding where to move based
		// on a randomly generated number
		PuzzleState shuffling_state = this;
		Operation prev = null;
		int check = 0;
		// Loops through pathLength number of times shuffling each execution
		while(check < pathLength) {
			int[][] zero = zeroFinder(shuffling_state);
			Random rand = new Random();
			int num1 = rand.nextInt(zero.length);
			Operation[] possibleOperations = possibleMoves(zero[num1][0], zero[num1][1], shuffling_state);
			int num2 = rand.nextInt(possibleOperations.length);
			Operation direction = possibleOperations[num2];
			if(direction == Operation.MOVERIGHT) {
				shuffling_state = shuffling_state.move(zero[num1][0], zero[num1][1]-1, direction);
			}
			if(direction == Operation.MOVELEFT) {
				shuffling_state = shuffling_state.move(zero[num1][0], zero[num1][1]+1, direction);
			}
			if(direction == Operation.MOVEUP) {
				shuffling_state = shuffling_state.move(zero[num1][0]+1, zero[num1][1], direction);
			}
			if(direction == Operation.MOVEDOWN) {
				shuffling_state = shuffling_state.move(zero[num1][0]-1, zero[num1][1], direction);
			}
			prev = direction;
			check++;
			
		}
		return shuffling_state;
	}
	
	public int[][] zeroFinder(PuzzleState state) {
		// Creates new matrix to store zero tiles from current state
		int[][] zeroes = new int[this.emptySlot][2];
		int zerorow = 0;
		for(int i = 0; i < this.currentState.length; i++) {
			for(int j = 0; j < this.currentState.length; j++) {
				if(state.isEmpty(i, j)) {
					zeroes[zerorow][0] = i;
					zeroes[zerorow][1] = j;
					zerorow++;
				}
			}
		}
		return zeroes;
	}
	
	public Operation[] possibleMoves(int row, int col, PuzzleState state) {
		// Finds and returns the possible moves given a certain tile
		int possArrLength = 0;
		Operation[] op = new Operation[4];
		if(col != 0) {
			op[possArrLength] = Operation.MOVERIGHT;
			possArrLength++;
		}
		if(col != this.currentState.length - 1) {
			op[possArrLength] = Operation.MOVELEFT;
			possArrLength++;
		}
		if(row != this.currentState.length - 1) {
			op[possArrLength] = Operation.MOVEUP;
			possArrLength++;
		}
		if(row != 0) {
			op[possArrLength] = Operation.MOVEDOWN;
			possArrLength++;
		}
		Operation[] possibilities = new Operation[possArrLength];
		for(int i = 0; i < possArrLength; i++) 
			possibilities[i] = op[i];
		return possibilities;
	}
	@Override
	public boolean isEmpty(int row, int column) {
		// TODO Auto-generated method 
		// Checks if tile at given coordinates is empty and returns true if it is and false otherwise
		if(getValue(row, column) == 0)
			return true;
		return false;
	}

	@Override
	public PuzzleState getStateWithShortestPath() {
		// TODO Auto-generated method stub
		// Returns state with shortest path
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(currentState);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimplePuzzleState other = (SimplePuzzleState) obj;
		return Arrays.deepEquals(currentState, other.currentState);
	}


}
