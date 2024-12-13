#!/usr/bin/python

##################################
# Written by Dana D. Matthews    #
# Date: 12/17/12                 #
# Tested with Python 2.6.6       #
##################################

def main():
	# The Thing does most of the work #
	theThing = Thing()
	
	# Open the input file and get the first line #
	oInput = open("input", "r")
	s = oInput.readline()
	
	# Open a file for output #
	oOutput = open("output", "w")
	
	# Process each line of the input file #
	while s != "":
		# Interpret the line of characters #
		s = theThing.think(s)
		
		# Write to the output file if a string was returned #
		if s != None:
			oOutput.write(s)
		
		# Get the next line from the input file #
		s = oInput.readline()
		
	# Close the files #
	oInput.close()
	oOutput.close()

class Thing:
	def __init__(self):
		# Indicates what line of characters is being interpreted (1, 2, or 3) #
		self.nLine = 0
		
		# Holds sets of values used to interpret the characters #
		self.nDigits = [0 for n in range(9)]
		
		# A sequence of possible unique sums representing digits 0-9 #
		self.nSums = (490, 288, 242, 434, 312, 410, 474, 290, 506, 442)
	
	def think(self, p):
		# Skip a blank line and increment the line counter #
		if p.strip() == "":
			self.nLine += 1
			return
		
		# Initialize these counters. They will help us #
		# keep track of characters in sets of three.   #
		n2 = 0; n3 = 0
		
		# Initialize this to zero. It will be used to figure out a value #
		# appropriate for what line a character is on (1, 2, or 3).      #
		nDigit = 0
		
		# For each of the characters in the line #
		for n1 in range(27):
			# Get the character #
			c1 = p[n1:n1 + 1]
			
			# The line may be too short, so test the character #
			# and fix it if necessary. #
			if c1 == "": c1 = " "
			
			# Decide if a bit needs switched on #
			if [True for c2 in ("|", "_") if c1 == c2]:
				# Switch on the correct bit #
				nDigit |= 1 << n2
			elif not [True for c2 in (" ", "\r", "\n") if c1 == c2]:
				# Something other than a valid character or whitespace.        #
				# Non valid characters precede a new line of valid characters. #
				# Reset the line counter and get the next line.                #
				self.nLine = 0
				return
			
			# Increment n2. Do additional processing if    #
			# we've processed a group of three characters. #
			n2 += 1
			if n2 == 3:
				# Shift the bits if we're on line two or three #
				if self.nLine == 1:
					nDigit <<= 3
				elif self.nLine == 2:
					nDigit <<= 6
				
				# Add this value to the appropriate list element in nDigits #
				self.nDigits[n3] += nDigit
				
				# Reset variables for the next three characters #
				n2 = 0
				n3 += 1
				nDigit = 0
		
		# Increment the line counter #
		self.nLine += 1
		
		# If nLine is three, return the results #
		if self.nLine == 3:
			s = self.__do()
			
			# Reset the list and line counter #
			self.nDigits = [0 for n in range(9)]
			self.nLine = 0
			
			# Return the output string #
			return s
	
	def __do(self):
		# Initialize a list to hold our calculated values #
		l = []
		
		# This boolean value will indicate if this set of numbers contains an illegible digit #
		bIll = False
		
		# For each digit in the account number #
		for n1 in range(9):
			# Initialize a boolean value which will indicate a legible character #
			b = False
			
			# Look for the digit in the valid list of sums #
			for n2 in range(10):
				if self.nDigits[n1] == self.nSums[n2]:
					l.append(n2)
					b = True
					break
			
			# If a corresponding valid digit was not found in nSums #
			if not b:
				l.append("?")
				bIll = True
		
		# Begin building a return string #
		s = "".join(str(c) for c in l)
		
		# If the account number has an illegible character #
		if bIll:
			s += " ILL\n"
		else:
			# Indicate if we have a bad checksum #
			s += "\n" if self.__checksum(l) == 0 else " ERR\n"
		
		# Return the result #
		return s
	
	def __checksum(self, p):
		# Calculate a checksum for the account number in the following three steps #
		n2 = 7
		for n1 in range(2, 10):
			p[n2] *= n1
			n2 -= 1
		
		nChkSum = 0
		for n1 in range(0, 9):
			nChkSum += p[n1]
		
		return nChkSum % 11

# Run the main function #
main()

