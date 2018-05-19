.section .data
ch: .byte '\0'
     .byte '\0','\0','\0'
c: .int 0
s: .int 0
__0_const: .int 0
__1_const: .float 3.0
__2_const: .asciz "%c"
__3_const: .asciz "%c\n"
__4_const: .asciz "%f"
   .byte '\0'
__5_const: .int 0

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
#调用函数:scanf
    movl $__2_const,%ebx
    movl %ebx,0(%esp)
    movl $ch,%ebx
    movl %ebx,4(%esp)
    call _scanf

#调用函数:printf
    movl $__3_const,%ebx
    movl %ebx,0(%esp)
    movl $ch,%ebx
    movl (%ebx),%eax
    movl %eax,4(%esp)
    call _printf

#调用函数:printf
    movl $__4_const,%ebx
    movl %ebx,0(%esp)
    flds 0(%ebp)
    fstpl 4(%esp)
    call _printf

    movl $__5_const,%ebx
    movl (%ebx),%eax
    leave
    ret
