.section .data
_ch: .byte '\0'
     .byte '\0','\0','\0'
_c: .int 0
_s: .int 0
__0_const: .int 0
__1_const: .float 3.0
__2_const: .byte '\n'
__3_const: .asciz "%f"
__4_const: .int 0

.section .text

.globl _test
_test:
    pushl %ebx
    movl %esp, %ebp
    sub $0,%esp
    movl $__0_const,%ebx
    movl (%ebx),%eax
    leave
    ret

.globl _main
_main:
    pushl %ebx
    movl %esp, %ebp
    sub $16,%esp
    movl $__1_const,%ebx
    movl (%ebx),%eax
    movl %eax,0(%ebp)
    movl $__3_const,%ebx
    movl %ebx,0(%esp)
    flds 0(%ebp)
    fstpl 4(%esp)
    call _printf
    movl $__4_const,%ebx
    movl (%ebx),%eax
    leave
    ret
