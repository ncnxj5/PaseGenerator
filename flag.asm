.386
.8087
.MODEL flat, stdcall

include e:\masm32\include\msvcrt.inc
includelib e:\masm32\lib\msvcrt.lib

.data
	numFmt db '%d',0
	chaFmt db '%c',0
	floFmt db '%lf',0
	spaFmt db ' ',0
	entFmt db '---',0
	FLOAT_TEMP dd 0
	BUFFER_  dword 0,0,0,0
	BUFFER_F REAL8 0.0
	szFmt db '%d, ', 0
	F_EAX REAL8 0.0
	F_EBX REAL8 0.0
	F_ECX REAL8 0.0
	F_EDX REAL8 0.0
	F_EDI REAL8 0.0
	F_ESI REAL8 0.0
	M1	REAL8 +3.0
	M2	REAL8 3.0
.code

inputnum proc
	invoke crt_scanf, addr numFmt,addr BUFFER_
	mov eax,BUFFER_[0]
	ret 
inputnum endp

inputcha proc
	invoke crt_scanf, addr chaFmt,addr BUFFER_
	mov eax,BUFFER_[0]
	ret 
inputcha endp

inputflo proc
	invoke crt_scanf, addr floFmt,addr BUFFER_F
	finit
	fld BUFFER_F
	fstp F_EAX
	ret 
inputflo endp

printspace proc
	invoke crt_printf, addr spaFmt
	ret
printspace endp

printnum proc num
	invoke crt_printf, addr numFmt, num
	ret
printnum endp

printcha proc char
	invoke crt_printf, addr chaFmt, char
	ret
printcha endp

printflo proc float:REAL8
	invoke crt_printf, addr floFmt, float
	ret
printflo endp

print proc arr, len
	mov edi, arr								;把数组起始地址给一个寄存器
    mov ecx, len								;把数组元素数(将要反复的次数)给 ECX
    xor eax, eax
 Lp:
    mov  eax, [edi]								;edi 中的地址将不断变化, 通过 [edi] 获取元素值
	push ecx
	push edi
	invoke crt_printf, addr szFmt, eax
	pop edi
	pop ecx
    add  edi, type arr								;获取下一个元素的地址
    loop Lp
	;invoke crt_printf, addr entFmt
	ret
print endp

f_add proc f1:REAL8, f2:REAL8
	finit
	fld	f1
	fld	f2
	fadd	;add and pop (f1+f2)
	fstp F_EAX
	ret
f_add endp

f_sub proc f1:REAL8, f2:REAL8
	finit
	fld	f1
	fld	f2
	fsub	;add and pop (f1-f2)
	fstp F_EAX
	ret
f_sub endp

f_imul proc f1:REAL8, f2:REAL8
	finit
	fld	f1
	fld	f2
	fmul	;add and pop (f1*f2)
	fstp F_EAX
	ret
f_imul endp

f_idiv proc f1:REAL8, f2:REAL8
	finit
	fld	f1
	fld	f2
	fdiv	;add and pop (f1/f2)
	fstp F_EAX
	ret
f_idiv endp

float2num proc f:REAL8 
	finit
	fld	f
	fistp	FLOAT_TEMP
	mov eax,FLOAT_TEMP
	ret
float2num endp

num2float proc n
	push eax
	mov eax,n
	mov FLOAT_TEMP,eax
	
	finit
	fild	FLOAT_TEMP
	fstp	F_EAX
	pop eax
	ret
num2float endp

f_BE proc f1:REAL8, f2:REAL8
	push ebx
	mov ebx,0
	fld	f1
	fcomp f2
	fstsw ax
	sahf
	jc @F
	mov ebx,1
@@:
	finit
	mov eax,ebx
	pop ebx
	ret
f_BE endp

f_LE proc f1:REAL8, f2:REAL8
	push ebx
	mov ebx,0
	fld	f2
	fcomp f1
	fstsw ax
	sahf
	jc @F
	mov ebx,1
@@:
	finit
	mov eax,ebx
	pop ebx
	ret
f_LE endp

f_LARGER proc f1:REAL8, f2:REAL8
	push ebx
	mov ebx,0
	fld	f2
	fcomp f1
	fstsw ax
	sahf
	jc @F
	mov ebx,1
@@:
	finit
	mov eax,ebx
	pop ebx
	not eax
	and eax, 1
	ret
f_LARGER endp

f_LESS proc f1:REAL8, f2:REAL8
	push ebx
	mov ebx,0
	fld	f1
	fcomp f2
	fstsw ax
	sahf
	jc @F
	mov ebx,1
@@:
	finit
	mov eax,ebx
	pop ebx
	not eax
	and eax, 1
	ret
f_LESS endp

f_EQUAL proc f1:REAL8, f2:REAL8
	LOCAL a:DWORD
	invoke f_LE,f1,f2
	mov a,eax
	invoke f_BE,f1,f2
	and eax,a
	ret
f_EQUAL endp
.data
     
    _FLOAT1 REAL8 0.0
    _FLOAT0 REAL8 0.0
    _NUM1 DWORD 0
    _NUM0 DWORD 0
    .code
    main proc
	invoke fuse
	;invoke print_num,eax
	invoke inputcha
	invoke inputcha
	ret
main endp

end main
