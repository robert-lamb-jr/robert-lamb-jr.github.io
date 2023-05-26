#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "calc.h"
#include <ctype.h>

unsigned short get_binary_op(bin_str bin)
{
	//takes in a binary string and returns its value as an unsigned short
	short bin_op = 0;
	short helper = 1;
	
	int j = 0; //used to find length of input binary string
	int bin_len = 0;
	while(bin[j] == '0' || bin[j] == '1') {
		bin_len++;
		j++;
	}
	
	for(int i = bin_len - 1; i >= 0; i--) {
		if(bin[i] == '1') {
			bin_op = bin_op + helper;
		}
		helper = helper * 2;
	}
	return bin_op;
}

void convert_to_binary(short val, bin_str bin)
{
	//takes short val, converts it into a string of 1s and 0s, and then assigns it to bin
	unsigned short subval = val;
	unsigned short expval = 32768;
	for(int i = 0; i < 16; i++) {
		if(subval >= expval) {
			bin[i] = '1';
			subval = subval - expval;
		}
		else {
			bin[i] = '0';
		}
		expval = expval / 2;
	}
}

short get_operand(char mode)
{
    short op;
    bin_str bs;
    int bs_len;
    int a;
    int b = 0;
    
    //switch to change the value of val based on the given mode
    
    switch(mode)
    {
        case 'D':
            printf("Enter decimal value: ");
            scanf(" %hi", &op);
            printf("%hi\n\n", op);
            break;
        case 'H':
            printf("Enter hex value: ");
            scanf(" %hX", &op);
            printf("%hX\n\n", op);
            break;
        case 'O':
            printf("Enter octal value: ");
            scanf(" %ho", &op);
            printf("%ho\n\n", op);
            break;
        case 'B':
        	printf("Enter binary value: ");
        	scanf(" %[^\n]%*c", &bs);
     		while(bs[b] == '0' || bs[b] == '1') {
     			printf("%c", bs[b]);
     			b++;
     		}
        	printf("\n\n");
        	op = get_binary_op(bs);
        	break;
        case 'C':
            op = 0;
            break;
    }
    return op; //returns short for val in whichever base specified

}

void print_bases(short val, char mode)
{
    //converts val to binary to be printed
    bin_str b;
    convert_to_binary(val, b); 
    
    //formats and prints input in each base with borders
    printf("****************************************\n");
    printf("* Base Values:         Input Mode: ");
    switch(mode) //switch to check input mode and print correct one
    {
        case 'D':
            printf("Dec *\n");
            break;
        case 'H':
            printf("Hex *\n");
            break;
        case 'O':
            printf("Oct *\n");
            break;
        case 'B':
        	printf("Bin *\n");
        	break;
    }
    printf("*   Binary  :  %c%c%c%c %c%c%c%c %c%c%c%c %c%c%c%c     *\n", b[0], b[1], b[2], b[3], b[4], b[5], b[6], b[7], b[8], b[9], b[10], b[11], b[12], b[13], b[14], b[15]);
    printf("*   Hex     :  %04hX                    *\n", val);
    printf("*   Octal   :  %06ho                  *\n", val);
    printf("*   Decimal :  %-10hi              *\n", val);
    printf("****************************************\n\n");
}

char print_menu(void)
{
    char op[20]; //represents string input from user
    printf("Please select one of the following options: \n\n");
    printf("B  Binary Mode             &  AND with Value           +  Add to Value\n");
    printf("O  Octal Mode              |  OR  with Value           -  Subtract from Value\n");
    printf("H  Hexadecimal Mode        ^  XOR with Value           N  Negate Value\n");
    printf("D  Decimal Mode            ~  Complement Value\n\n");
    printf("C  Clear Value       <  Shift Value Left\n");
    printf("S  Set Value         >  Shift Value Right\n\n");
    printf("Q  Quit \n\n");
    printf("Option: ");
    scanf(" %s", op);
    printf("%s\n", op);
    
    switch(toupper(op[0])) //switch based on first character of input
    {
        case 'O':
            printf("Mode is Octal\n\n");
            return 'O';
            break;
        case 'H':
            printf("Mode is Hexadecimal\n\n");
            return 'H';
            break;
        case 'D':
            printf("Mode is Decimal\n\n");
            return 'D';
            break;
        case 'C':
            get_operand('C'); //calls back to get_operand to reset val
            printf("\n");
            return 'C';
            break;
        case 'S':
            return 'S';
            break;
        case 'Q':
            if(strlen(op) > 1)
            {
            	printf("\nInvalid option: %s\n\n", op);
            	return print_menu();
            	break; //checks to see if input is a single Q or word
            	//starting with Q
            }
            else
            {
            	printf("\n");
            	return 'Q';
            	break;
            }
        case 'B':
        	printf("Mode is Binary\n\n");
        	return 'B';
        	break;
        case '&':
       		return '&';
       		break;
       	case '|':
       		return '|';
       		break;
       	case '^':
       		return '^';
       		break;
       	case '~':
       		return '~';
       		break;
       	case '+':
       		return '+';
       		break;
       	case '-':
       		return '-';
       		break;
       	case 'N':
       		return 'N';
       		break;
       	case '<':
       		return '<';
       		break;
       	case '>':
       		return '>';
       		break;
        default:
            printf("\nInvalid option: %s\n\n", op);
            return print_menu();
            break; //default option for when first char is not valid
            //recursively prints menu until option is valid
    }

}

void add(short *val, char mode) 
{
	//takes val, scans for user input to get value to be added, and then adds the two together
	short to_add;
	short sum;
	bin_str bs;
	int d = 0;
	
	switch(mode)
    {
        //switch for input is necessary for formatting purposes, using get_operand messed with indentation for this
        //particular function
        case 'D':
            printf("Enter decimal value: ");
            scanf(" %hi", &to_add);
            printf("%hi\n", to_add);
            break;
        case 'H':
            printf("Enter hex value: ");
            scanf(" %hX", &to_add);
            printf("%hX\n", to_add);
            break;
        case 'O':
            printf("Enter octal value: ");
            scanf(" %ho", &to_add);
            printf("%ho\n", to_add);
            break;
        case 'B':
        	printf("Enter binary value: ");
        	scanf(" %[^\n]%*c", &bs);
        	while(bs[d] == '0' || bs[d] == '1') {
     			printf("%c", bs[d]);
     			d++;
     		}
        	printf("\n");
        	to_add = get_binary_op(bs);
        	break;
    }
    sum = *val + to_add;
    //checks for overflow and prints if necessary
    if((*val > 0) && (to_add > 0) && (sum < 0)) {
    	printf("Positive Overflow\n\n");
    }
    else if((*val < 0) && (to_add < 0) && (sum > 0)) {
    	printf("Negative Overflow\n\n");
    }
    else {
    	printf("\n");
    }
    
    *val = sum;
}

void subtract(short *val, char mode)
{
	//operates the same as add method but subtracts user input value from val instead of adding
	short to_subtract;
	short difference;
	bin_str bs;
	int c = 0;
	
	switch(mode)
    {
        case 'D':
            printf("Enter decimal value: ");
            scanf(" %hi", &to_subtract);
            printf("%hi\n", to_subtract);
            break;
        case 'H':
            printf("Enter hex value: ");
            scanf(" %hX", &to_subtract);
            printf("%hX\n", to_subtract);
            break;
        case 'O':
            printf("Enter octal value: ");
            scanf(" %ho", &to_subtract);
            printf("%ho\n", to_subtract);
            break;
        case 'B':
        	printf("Enter binary value: ");
        	scanf(" %[^\n]%*c", &bs);
        	while(bs[c] == '0' || bs[c] == '1') {
     			printf("%c", bs[c]);
     			c++;
     		}
        	printf("\n");
        	to_subtract = get_binary_op(bs);
        	break;
    }
    difference = *val - to_subtract;
    if((*val > 0) && (to_subtract < 0) && (difference < 0)) {
    	printf("Positive Overflow\n\n");
    }
    else if((*val < 0) && (to_subtract > 0) && (difference > 0)) {
    	printf("Negative Overflow\n\n");
    }
    else {
    	printf("\n");
    }
    
    *val = difference;
}

int main(void)
{
    char mode = 'D'; //sets initial mode to decimal
    char option = 'D';
    short val = 0; //sets initial value to 0
    short val_helper; //represents user input operator value
    int leftshift = 0;
    int rightshift = 0;
    printf("\n");
    while(option != 'Q'){
		//loops continuously until a single Q is entered as option
        print_bases(val, mode); //prints and formats specified
        //conversions
        option = print_menu(); //calls to print menu function and
        //sets option based on user input

        switch(option) //sets mode or changes val based on option input
        {
            case 'D':
                mode = 'D';
                break;
            case 'H':
                mode = 'H';
                break;
            case 'O':
                mode = 'O';
                break;
            case 'B':
            	mode = 'B';
            	break;
            case 'S':
                val = get_operand(mode);
                break;  
            case 'C':
                val = get_operand('C');
                break;  
            case '&':
	       		val_helper = get_operand(mode);
	       		val &= val_helper;
	       		break;
	       	case '|':
	       		val_helper = get_operand(mode);
	       		val |= val_helper;
	       		break;
	       	case '^':
	       		val_helper = get_operand(mode);
	       		val ^= val_helper;
	       		break;
	       	case '~':
	       		val = ~val;
	       		printf("\n");
	       		break;
	       	case '+':
	       		add(&val, mode);
	       		break;
	       	case '-':
	       		subtract(&val, mode);
	       		break;
	       	case 'N':
	       		val = -val;
	       		printf("\n");
	       		break;
	       	case '<':
	       		printf("Enter number of positions to left shift value: ");
	       		scanf(" %d", &leftshift);
	       		printf("%d\n\n", leftshift);
	       		val = val << leftshift;
	       		break;
	       	case '>':
	       		printf("Enter number of positions to right shift value: ");
	       		scanf(" %d", &rightshift);
	       		printf("%d\n\n", rightshift);
	       		val = val >> rightshift;
	       		break;
        }
    }
    return 0;
}