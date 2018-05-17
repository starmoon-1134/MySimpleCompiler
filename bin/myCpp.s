.section .data
.byte '\0','\0','\0','\0'
.int 0
.int 0
0_const: .int 35
1_const: .int 0

.section .text
.globl _main
_main
    pushl %ebx
    movl %esp, %ebp
    xor %ebx,%ebx
    movl $0_const,4(%ebx)
    xor %ebx,%ebx
    xor %eax,%eax
    movl 16(%ebx),%eax
    leave
    ret
