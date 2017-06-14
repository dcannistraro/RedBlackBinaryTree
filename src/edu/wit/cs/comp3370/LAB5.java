
package edu.wit.cs.comp3370;

import java.util.Scanner;

/* Provides next/previous/height info on (int, int) disk locations 
 * 
 * Wentworth Institute of Technology
 * COMP 3370
 * Lab Assignment 5
 * 
 */

public class LAB5 {

	// read pairs of ints from scanner
	// returns an array of DiskLocations
	private static DiskLocation getLoc(Scanner s) {

		DiskLocation d = new DiskLocation(-1, -1);
		d.track = s.nextInt();
		if (s.hasNextInt())
			d.sector = s.nextInt();
		else {
			System.err.println("track/sector mismatch on input");
			System.exit(0);
		}

		if (d.track < 0 || d.sector < 0) {
			System.err.println("track and sector values must be non-negative");
			System.exit(0);
		}
		return d;
	}

	// prints the next/prev n items after a specific location
	// the location must be a valid location in l
	private static void printIter(LocationHolder l, DiskLocation d, char direction, int number) {

		DiskLocation temp = l.find(d);
		for (int i = 0; i < number; i++) {
			if (temp == LocationHolder.nil)
				return;
			if (direction == 'n')
				temp = l.next(temp);
			else if (direction == 'p')
				temp = l.prev(temp);
			if (temp.toString().length() == 0)
				break;
			System.out.println(temp.toString());
		}
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		LocationHolder l = new BST();

		System.out.printf("Enter the data structure to use ([l]ist, [b]inary tree, [r]ed-black tree): ");
		char algo = s.next().charAt(0);

		switch (algo) {
		case 'l':
			l = new L();
			break;
		case 'b':
			l = new BST();
			break;
		case 'r':
			l = new RBTree();
			break;
		default:
			System.out.println("Invalid data structure");
			System.exit(0);
			break;
		}

		System.out.printf("Enter non-negative track/sector pairs separated by spaces.\nTerminate the list with one of the following options:\n");
		System.out.printf("Enter [n] <track> <sector> <number> to print the next \"number\" values after the location (must be valid location).\n");
		System.out.printf("Enter [p] <track> <sector> <number> to print the next \"number\" values before the location (must be valid location).\n");
		System.out.printf("Enter [h] to print the height of the data structure\n");
		System.out.printf("Enter [q] to quit\n");
		System.out.printf("Example: 0 1 1 1 2 1 p 2 1 2\n");

		while (s.hasNextInt())
			l.insert(getLoc(s));

		char nextAction = s.next().charAt(0);

		switch (nextAction) {
		case 'n':
		case 'p':
			int track = -1;
			int sector = -1;
			int number = -1;
			if (s.hasNextInt())
				track = s.nextInt();
			if (s.hasNextInt())
				sector = s.nextInt();
			if (s.hasNextInt())
				number = s.nextInt();
			else {
				System.err.println("couldn't read track/sector and number");
				System.exit(0);
			}
			printIter(l, new DiskLocation(track, sector), nextAction, number);
			break;
		case 'h':
			System.out.printf("height: %d\n", l.height());
			break;
		case 'q':
			System.exit(0);
		default:
			System.out.println("Invalid action");
			System.exit(0);
			break;
		}
		s.close();

	}

}
