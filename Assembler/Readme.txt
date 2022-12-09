----------------------------------------
Using IntelliJ to load the project.

a.sal & b.sal are for testing
-----------------------------------------
Edge case Test:
 
// jjjj
.data
    sum
    x

.code
loop:
   //Comment 2
   LOAD r0, x           // Comment 3
   ADD r0, #345
   INC r1,  r7               // Comment 4
   STORE r0, Sum
   JMP loop
   JGT r2, loop
End:

1100 000 1  0100 0000    LOAD  4bytes

0000 0001 0100 0000      ADD    4bytes

1001 0010  1100 0000      INC   2bytes

1101 0001 0100 0000       Store  4bytes

0100 0000 1000 0000       JMP  2bytes

0101 0100 1000 0000       JGT   2bytes


22bits  decimal: 0-4194303 (inclusive)
6 bits           0-63 (inclusive)

delimiters: "OneOrMoreSpaces" | ",ZeroOrMoreSpaces"
\s+ | ,\s*

substring starting with:
r1/R1   register 0-7
#123    constant
123     address
