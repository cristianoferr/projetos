
DIV16U: nop
;Clear the working quotient
;
      clr   a
      mov   r2,a
      mov   r3,a
;
;b counts the number of places+1 the divisor was initially
; shifted left to align its ms bit set with the ms bit set
; in the dividend
;
      mov   b,#1

;
;Make an error return if trying to divide by zero
;
      mov   a,r1
      orl   a,r0
      jz    dua920
;
;Just return with quotient and remainder zero if dividend is zero
;
      mov   a,r5
      orl   a,r4
      jnz   dua200
      mov   r7,a
      mov   r6,a
      sjmp   dua910      ;Make a normal return
;
;Align the msb set in the demoninator with the msb set in the
; numerator. Increment the shift count in b each time a shift left
; is performed.
;
dua200: nop
      mov   a,r1 ;Stop if MSB set
      rlc   a
      jc    dua600
      clr   c
      mov   a,r5        ;Compare r1 & r5
      subb  a,r1
      jc    dua600      ; jump if r1>r5
      jnz   dua240      ; jump if r1<r5
      mov   a,r4        ;r1=r5, so compare r0 & r4
      subb  a,r0
      jc    dua600      ; jump if r0>r4
dua240: nop
      clr   c           ;Now shift the denominator
      mov   a,r0        ; left 1 bit position
      rlc   a
      mov   r0,a
      mov   a,r1
      rlc   a
      mov   r1,a
      inc   b           ;Increment b counter and
      sjmp  dua200      ; continue
;
;Compare the shifted divisor with the remainder (what's
; left of the dividend)
;
dua600: nop
      clr   c
      mov   a,r5
      subb  a,r1
      jc    dua720      ;jump if r1>r5
      jnz   dua700      ;jump if r1<r5
      mov   a,r4
      subb  a,r0
      jc    dua720      ;jump if r0>r4
;
;Divisor is equal or smaller, so subtract it off and
; get a 1 for the quotient
;
dua700: nop
      mov   a,r4
      clr   c
      subb  a,r0
      mov   r4,a
      mov   a,r5
      subb  a,r1
      mov   r5,a
      clr   c
      cpl   c           ;Get a 1 for the quotient
      sjmp  dua730
;
;Divisor is greater, get a 0 for the quotient
;
dua720: nop
      clr   c
;
;Shift 0 or 1 into quotient
;
dua730: nop
      mov   a,r2
      rlc   a
      mov   r2,a
      mov   a,r3
      rlc   a  ;Test for overlow removed here because
      mov   r3,a ; it can't happen when dividing 16 by 16
;
;Now shift the denominator right 1, decrement the counter
; in b until b = 0
;
dua740: nop
      clr   c
      mov   a,r1
      rrc   a
      mov   r1,a
      mov   a,r0
      rrc   a
      mov   r0,a
      djnz  b,dua600
;
;Move quotient and remainder so that quotient is returned in the same
; registers as the dividend. This makes it easier to divide repeatedly
; by the same number as you would do when converting to a new radix.
;
      mov   a,r5
      mov   r7,a
      mov   a,r4
      mov   r6,a
      mov   a,r3
      mov   r5,a
      mov   a,r2
      mov   r4,a
;
;Make the normal return
;
dua910: nop
      clr   c
      ret
;
;Make the error return
;
dua920: nop
      clr   c
      cpl   c
      ret
;End of DIV16U
