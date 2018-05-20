.section .data
ch: .space 1
dddd: .space 5
 .space 2#碎片填充  
c: .space 4
s: .space 4
__0_const: .int 0
__1_const: .float 3.0
__2_const: .asciz "%c"
__3_const: .asciz "%c\n"
__4_const: .asciz "%f"
__5_const: .asciz "s  %d"
 .space 3#碎片填充  
__6_const: .int 0

.section .text

.globl _test
_test:
    pushl %ebx
    movl %esp, %ebp
    sub $4,%esp
    movl $__0_const,%ebx
    movl (%ebx),%eax
    leave
    ret

.globl _main
_main:
    pushl %ebx
    movl %esp, %ebp
    sub $24,%esp
    movl __1_const,%eax
    movl %eax,-4(%ebp)
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
    movl %eax,-8(%ebp)

    movl -8(%ebp),%eax
    movl %eax,s
#调用函数:printf
    movl $__4_const,%ebx
    movl %ebx,0(%esp)
    flds -4(%ebp)
    fstpl 4(%esp)
    call _printf

#调用函数:printf
    movl $__5_const,%ebx
    movl %ebx,0(%esp)
    movl $s,%ebx
    movl (%ebx),%eax
    movl %eax,4(%esp)
    call _printf

    movl $__6_const,%ebx
    movl (%ebx),%eax
    leave
    ret
