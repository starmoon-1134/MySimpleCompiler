.section .data
.byte '\0','\0','\0','\0'
.int 0
.int 0
0_const: .byte '\n'
1_const: .int 0

.section .text

.globl _main
_main
    pushl %ebx
    movl %esp, %ebp
    xor %ebx,%ebx
    xor %eax,%eax
    movb -20(%ebx),%al
    movb %al,0(%ebx)
    movl 8(%ebp),%eax
    xor %ebx,%ebx
    movl %eax,-6(%ebx)
    xor %ebx,%ebx
    xor %eax,%eax
    movl -21(%ebx),%eax
    leave
    ret
