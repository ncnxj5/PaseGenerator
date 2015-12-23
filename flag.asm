.386
.8087
.MODEL flat, stdcall

include e:\masm32\include\msvcrt.inc
includelib e:\masm32\lib\msvcrt.lib

.data
	numFmt db '%d',0
	chaFmt db '%c',0
	floFmt db '%f',0
	spaFmt db ' ',0
	entFmt db '---',0
	FLOAT_TEMP dd 0
	szFmt db '%d, ', 0
	F_EAX REAL8 0.0
	F_EBX REAL8 0.0
	F_ECX REAL8 0.0
	F_EDX REAL8 0.0
	F_EDI REAL8 0.0
	F_ESI REAL8 0.0
	M1	REAL8 +3.0
	M2	REAL8 3.0

    _FLOAT2 REAL8 0.0
    array_0 DWORD 0,0,0,0,0,0,0,0,0,0
    _FLOAT1 REAL8 0.0
    _FLOAT0 REAL8 0.0
    _NUM2 DWORD 0
    _NUM1 DWORD 0
    _NUM0 DWORD 0
    len_0 DWORD 0
    .code
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
printa proc s,len
    LOCAL i_1:DWORD
    mov esi,0
    mov i_1,esi
    mov ebx,i_1
    mov ecx,len
    push ecx
    push esi
    push ebx
    push ecx
    pop esi
    pop ecx
    cmp esi,ecx
    mov eax,0
    jle @F
    mov eax,1
    @@:
    pop esi
    pop ecx
    mov ebx,eax
    mov eax,ebx
    .WHILE eax>0
    mov edx,i_1
    mov edi,s
    mov edx,DWORD ptr[edi+edx*4]
    mov _NUM0,edx
    invoke printnum,_NUM0
    invoke printspace
    mov esi,i_1
    mov ebx,1
    add esi,ebx
    mov i_1,esi
    mov ecx,i_1
    mov edx,len
    push ecx
    push ebx
    push ecx
    push edx
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jle @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov ecx,eax
    mov eax,ecx
    .ENDW
    mov edi,0
    mov eax,edi
    ret
    printa endp
    quicksort proc s,l,r
    LOCAL x_4:DWORD
    LOCAL j_4:DWORD
    LOCAL i_4:DWORD
    mov esi,l
    mov ebx,r
    push ecx
    push ebx
    push esi
    push ebx
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jle @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov esi,eax
    mov eax,esi
    .IF eax>0
    mov ecx,l
    mov i_4,ecx
    mov edx,r
    mov j_4,edx
    mov edi,l
    mov esi,s
    mov edi,DWORD ptr[esi+edi*4]
    mov x_4,edi
    mov ebx,i_4
    mov ecx,j_4
    push ecx
    push ebx
    push ebx
    push ecx
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jle @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov ebx,eax
    mov eax,ebx
    .WHILE eax>0
    mov edx,i_4
    mov edi,j_4
    push ecx
    push ebx
    push edx
    push edi
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jle @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov edx,eax
    mov esi,j_4
    mov ebx,s
    mov esi,DWORD ptr[ebx+esi*4]
    mov ecx,x_4
    push ecx
    push ebx
    push ecx
    push esi
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jl @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov esi,eax
    and edx,esi
    mov eax,edx
    .WHILE eax>0
    mov edx,j_4
    mov edi,1
    sub edx,edi
    mov j_4,edx
    mov esi,i_4
    mov ebx,j_4
    push ecx
    push ebx
    push esi
    push ebx
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jle @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov esi,eax
    mov ecx,j_4
    mov edx,s
    mov ecx,DWORD ptr[edx+ecx*4]
    mov edi,x_4
    push ecx
    push ebx
    push edi
    push ecx
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jl @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov ecx,eax
    and esi,ecx
    mov eax,esi
    .ENDW
    mov esi,i_4
    mov ebx,j_4
    push ecx
    push ebx
    push esi
    push ebx
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jle @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov esi,eax
    mov eax,esi
    .IF eax>0
    mov ecx,j_4
    mov edx,s
    mov ecx,DWORD ptr[edx+ecx*4]
    mov edi,i_4
    mov esi,s
    mov DWORD ptr[esi+edi*4],ecx
    mov ebx,i_4
    mov ecx,1
    add ebx,ecx
    mov i_4,ebx
    .ENDIF
    mov edx,i_4
    mov edi,j_4
    push ecx
    push ebx
    push edx
    push edi
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jle @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov edx,eax
    mov esi,i_4
    mov ebx,s
    mov esi,DWORD ptr[ebx+esi*4]
    mov ecx,x_4
    push ecx
    push ebx
    push esi
    push ecx
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jle @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov esi,eax
    and edx,esi
    mov eax,edx
    .WHILE eax>0
    mov edx,i_4
    mov edi,1
    add edx,edi
    mov i_4,edx
    mov esi,i_4
    mov ebx,j_4
    push ecx
    push ebx
    push esi
    push ebx
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jle @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov esi,eax
    mov ecx,i_4
    mov edx,s
    mov ecx,DWORD ptr[edx+ecx*4]
    mov edi,x_4
    push ecx
    push ebx
    push ecx
    push edi
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jle @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov ecx,eax
    and esi,ecx
    mov eax,esi
    .ENDW
    mov esi,i_4
    mov ebx,j_4
    push ecx
    push ebx
    push esi
    push ebx
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jle @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov esi,eax
    mov eax,esi
    .IF eax>0
    mov ecx,i_4
    mov edx,s
    mov ecx,DWORD ptr[edx+ecx*4]
    mov edi,j_4
    mov esi,s
    mov DWORD ptr[esi+edi*4],ecx
    mov ebx,j_4
    mov ecx,1
    sub ebx,ecx
    mov j_4,ebx
    .ENDIF
    mov edx,i_4
    mov edi,j_4
    push ecx
    push ebx
    push edx
    push edi
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jle @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov edx,eax
    mov eax,edx
    .ENDW
    mov esi,x_4
    mov ebx,i_4
    mov ecx,s
    mov DWORD ptr[ecx+ebx*4],esi
    mov edx,l
    mov _NUM1,edx
    mov edi,i_4
    mov esi,1
    sub edi,esi
    mov _NUM2,edi
    invoke quicksort,s,_NUM1,_NUM2
    mov ebx,i_4
    mov ecx,1
    add ebx,ecx
    mov _NUM1,ebx
    mov edx,r
    mov _NUM2,edx
    invoke quicksort,s,_NUM1,_NUM2
    .ENDIF
    mov edi,0
    mov eax,edi
    ret
    quicksort endp
    fuse proc 
    mov esi,34
    mov array_0[0],esi
    mov ebx,65
    mov array_0[1],ebx
    mov ecx,12
    mov array_0[2],ecx
    mov edx,43
    mov array_0[3],edx
    mov edi,67
    mov array_0[4],edi
    mov esi,5
    mov array_0[5],esi
    mov ebx,78
    mov array_0[6],ebx
    mov ecx,10
    mov array_0[7],ecx
    mov edx,3
    mov array_0[8],edx
    mov edi,70
    mov array_0[9],edi
    mov esi,10
    mov len_0,esi
    mov ebx,0
    mov _NUM1,ebx
    mov ecx,len_0
    mov edx,1
    sub ecx,edx
    mov _NUM2,ecx
    invoke quicksort,addr array_0,_NUM1,_NUM2
    mov edi,10
    mov _NUM1,edi
    invoke printa,addr array_0,_NUM1
    mov esi,0
    mov eax,esi
    ret
    fuse endp
    main proc
	invoke fuse
	;invoke print_num,eax
	invoke crt_scanf
main endp

end main
