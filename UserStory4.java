
// 1 + Math.floor(Math.log(value) / Math.log(2))

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

public class UserStory4 {

	public static void main(String args[]) {
		// The Thing does most of the work //
		Thing theThing = new Thing();
		
		String s;
		
		try {
			// Open the input and output files //
			BufferedReader ioInput = new BufferedReader(new FileReader("input"));
			FileWriter ioOutput = new FileWriter("output");
			
			// Process each line of the input file //
			while ((s = ioInput.readLine()) != null) {
				// Interpret the line of characters and write to the output file //
				// if a non-empty string was returned                            //
				if (!(s = theThing.think(s)).equals("")) {
					// Write to the output file if a non-empty string was returned //
					ioOutput.write(s);
				}
			}
			
			// Close the files //
			ioInput.close();
			ioOutput.close();
		} catch(IOException e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}
}

class Thing {
	// Indicates what line of characters is being interpreted (1, 2, or 3) //
	int iLine = 0;
	
	// Holds sets of values used to interpret the characters //
	int[] iDigits = new int[9];
	
	// An array of possible unique sums representing digits 0-9 //
	int[] iSums = {490, 288, 242, 434, 312, 410, 474, 290, 506, 442};
	
	public String think(String p) {
		String s;
		
		// Skip a blank line and increment the line counter //
		if (p.trim().equals("")) {
			this.iLine += 1;
			return "";
		}
		
		// Initialize these counters. They will help us //
		// keep track of characters in sets of three.   //
		int i2 = 0, i3 = 0;
		
		// Initialize this to zero. It will be used to figure out a value //
		// appropriate for what line a character is on (1, 2, or 3).      //
		int iDigit = 0;
		
		// For each of the characters in the line //
		int iLen = p.length();
		for (int i1 = 0; i1 < 27; i1++) {
			// Get the character //
			if (i1 < iLen) {
				s = p.substring(i1, i1 + 1);
			} else {
				// The line may be too short, so fix the character if necessary. //
				s = " ";
			}
			
			// Decide if a bit needs switched on //
			if ((s.equals("|")) || (s.equals("_"))) {
				// Switch on the correct bit //
				iDigit |= 1 << i2;
			} else if ((!s.equals(" ")) && (!s.equals("\r")) && (!s.equals("\n"))) {
				// Something other than a valid character or whitespace.        //
				// Non valid characters precede a new line of valid characters. //
				// Reset the line counter and get the next line.                //
				this.iLine = 0;
				return "";
			}
			
			// Increment i2. Do additional processing if    //
			// we've processed a group of three characters. //
			i2 += 1;
			if (i2 == 3) {
				// Shift the bits if we're on line two or three //
				if (this.iLine == 1) {
					iDigit <<= 3;
				} else if (this.iLine == 2) {
					iDigit <<= 6;
				}
				
				// Add this value to the appropriate array element in iDigits //
				this.iDigits[i3] += iDigit;
				
				// Reset variables for the next three characters //
				i2 = 0;
				i3 += 1;
				iDigit = 0;	
			}
		}
		
		// Increment the line counter //
		this.iLine += 1;
		
		// If nLine is three, return the results //
		if (this.iLine == 3) {
			s = this.__do();
			
			// Reset the list and line counter //
			this.iDigits = new int[9];
			this.iLine = 0;
			
			// Return the output string //
			return s;
		}
		
		// If we've fallen through the above logic, then //
		// return an empty string. More work to do.      //
		return "";
	}
	
	String __do() {
		// A boolean flag which will indicate a legible character was found //
		boolean b;
		
		// Initialize a string to hold our calculated values //
		String s = "";
		
		// This boolean value will indicate if this set of numbers contains an illegible digit //
		boolean bIll = false;
		
		// For each digit in the account number //
		for (int i1 = 0; i1 < 9; i1++) {
			b = false;
			
			// Look for the digit in the valid list of sums //
			for (int i2 = 0; i2 < 10; i2++) {
				if (this.iDigits[i1] == this.iSums[i2]) {
					s += i2;
					b = true;
					break;
				}
			}
			
			// If a corresponding valid digit was not found in iSums //
			if (!b) {
				s += "?";
				bIll = true;
			}
		}
		
		// If the account number has an illegible character //
		if (bIll) {
			return this.__guessIllegibleString(s);
		}
		
		/*} else {
			// Indicate if we have a bad checksum //
			s += (this.__checksum(s) == 0) ? "\r\n" : " ERR\r\n";
		}*/
		
		// Return the result //
		return s;
	}
	
	String __guessIllegibleString(String p) {
		
		
		
		
		
		
		return p;
	}
	
	int __checksum(String p) {
		int i1, i2 = 7;
		int nChkSum = 0;
		int[] a = new int[9];
		
		// Calculate a checksum for the account number in the following three steps //
		a[8] = Integer.parseInt(p.substring(8, 9));
		
		for (i1 = 2; i1 < 10; i1++) {
			a[i2] = i1 * Integer.parseInt(p.substring(i2, i2 + 1));
			i2 -= 1;
		}
		
		for (i1 = 0; i1 < 9; i1++) {
			nChkSum += a[i1];
		}
		
		return nChkSum % 11;
	}
}

