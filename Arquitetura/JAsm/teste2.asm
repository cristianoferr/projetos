DB4     EQU   P1.4
DB5     EQU   P1.5
DB6     EQU   P1.6
DB7     EQU   P1.7
EN      EQU   P3.7
RS      EQU   P3.6
RW      EQU   P3.5
DATA    EQU   P1

ORL   DATA,#128  
SETB  EN
MOV  A,DATA
CLR   EN
ANL  A,#128
PUSH  ACC
SETB  EN
MOV   A,DATA  
CLR   EN 
ANL   A,#128
SWAP  A            
MOV   R7,A 
POP   ACC 
ORL   A,R7  
RET
