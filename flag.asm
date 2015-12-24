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
     
    line_0 DWORD 0
    DWORD 0,0,0,0,0,0,0
    _FLOAT1 REAL8 0.0
    _FLOAT0 REAL8 0.0
    _NUM1 DWORD 0
    answer_0 DWORD 0
    _NUM0 DWORD 0
    .code
        show proc 
    LOCAL i_1:DWORD
    LOCAL j_1:DWORD
    mov esi,0
    mov i_1,esi
    mov ebx,0
    mov j_1,ebx
    mov ecx,i_1
    mov edx,8
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
    .WHILE eax>0
    mov edi,j_1
    mov esi,8
    push ecx
    push ebx
    push edi
    push esi
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jle @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov edi,eax
    mov eax,edi
    .WHILE eax>0
    mov ebx,i_1
    mov ebx,line_0[ebx*4]
    mov ecx,j_1
    push ecx
    push ebx
    push ecx
    push ebx
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jne @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov ebx,eax
    mov eax,ebx
    .IF eax>0
    mov edx,81
    mov _NUM0,edx
    invoke printcha,_NUM0
    .ENDIF
    mov edi,i_1
    mov edi,line_0[edi*4]
    mov esi,j_1
    push ecx
    push ebx
    push esi
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
    mov edi,eax
    mov ebx,i_1
    mov ebx,line_0[ebx*4]
    mov ecx,j_1
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
    or edi,ebx
    mov eax,edi
    .IF eax>0
    mov edx,42
    mov _NUM0,edx
    invoke printcha,_NUM0
    .ENDIF
    mov edi,j_1
    mov esi,1
    add edi,esi
    mov j_1,edi
    mov ebx,j_1
    mov ecx,8
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
    .ENDW
    mov edx,0
    mov j_1,edx
    mov edi,10
    mov _NUM0,edi
    invoke printcha,_NUM0
    mov esi,i_1
    mov ebx,1
    add esi,ebx
    mov i_1,esi
    mov ecx,i_1
    mov edx,8
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
    mov edi,answer_0
    mov esi,1
    add edi,esi
    mov answer_0,edi
    mov ebx,answer_0
    mov _NUM0,ebx
    invoke printnum,_NUM0
    mov ecx,10
    mov _NUM0,ecx
    invoke printcha,_NUM0
    invoke inputcha
    mov edx,0
    mov eax,edx
    ret
    show endp
        judge proc n
    LOCAL i_6:DWORD
    mov edi,0
    mov i_6,edi
    mov esi,i_6
    mov ebx,n
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
    .WHILE eax>0
    mov ecx,n
    mov ecx,line_0[ecx*4]
    mov edx,i_6
    mov edx,line_0[edx*4]
    push ecx
    push ebx
    push edx
    push ecx
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jne @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov ecx,eax
    mov eax,ecx
    .IF eax>0
    mov edi,1
    mov eax,edi
    ret
    .ENDIF
    mov esi,n
    mov esi,line_0[esi*4]
    mov ebx,n
    sub esi,ebx
    mov ecx,i_6
    mov ecx,line_0[ecx*4]
    mov edx,i_6
    sub ecx,edx
    push ecx
    push ebx
    push ecx
    push esi
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jne @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov esi,eax
    mov eax,esi
    .IF eax>0
    mov edi,1
    mov eax,edi
    ret
    .ENDIF
    mov esi,n
    mov esi,line_0[esi*4]
    mov ebx,n
    add esi,ebx
    mov ecx,i_6
    mov ecx,line_0[ecx*4]
    mov edx,i_6
    add ecx,edx
    push ecx
    push ebx
    push ecx
    push esi
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jne @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov esi,eax
    mov eax,esi
    .IF eax>0
    mov edi,1
    mov eax,edi
    ret
    .ENDIF
    mov esi,i_6
    mov ebx,1
    add esi,ebx
    mov i_6,esi
    mov ecx,i_6
    mov edx,n
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
    judge endp
        control proc n
    LOCAL k_11:DWORD
    LOCAL temp_11:DWORD
    mov esi,8
    mov k_11,esi
    mov ebx,0
    mov ecx,n
    mov line_0[ecx*4],ebx
    mov edx,n
    mov edx,line_0[edx*4]
    mov edi,k_11
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
    .WHILE eax>0
    mov esi,n
    mov _NUM0,esi
    invoke judge,_NUM0
    mov esi,eax
    mov temp_11,esi
    mov ebx,temp_11
    mov ecx,0
    push ecx
    push ebx
    push ecx
    push ebx
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jne @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov ebx,eax
    mov eax,ebx
    .IF eax>0
    mov edx,n
    mov edi,7
    push ecx
    push ebx
    push edi
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
    mov edx,eax
    mov esi,n
    mov ebx,7
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
    or edx,esi
    mov eax,edx
    .IF eax>0
    mov ecx,n
    mov edx,1
    add ecx,edx
    mov _NUM0,ecx
    invoke control,_NUM0
    .ENDIF
    mov edi,n
    mov esi,7
    push ecx
    push ebx
    push esi
    push edi
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jne @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov edi,eax
    mov eax,edi
    .IF eax>0
    invoke show
    .ENDIF
    .ENDIF
    mov ebx,n
    mov ebx,line_0[ebx*4]
    mov ecx,1
    add ebx,ecx
    mov edx,n
    mov line_0[edx*4],ebx
    mov edi,n
    mov edi,line_0[edi*4]
    mov esi,k_11
    push ecx
    push ebx
    push edi
    push esi
    pop ebx
    pop ecx
    cmp ebx,ecx
    mov eax,0
    jle @F
    mov eax,1
    @@:
    pop ebx
    pop ecx
    mov edi,eax
    mov eax,edi
    .ENDW
    mov ebx,0
    mov eax,ebx
    ret
    control endp
        fuse proc 
    mov ecx,0
    mov _NUM0,ecx
    invoke control,_NUM0
    mov edx,0
    mov eax,edx
    ret
    fuse endp
    main proc
	invoke fuse
	;invoke print_num,eax
	invoke inputcha
	invoke inputcha
	ret
main endp

end main
