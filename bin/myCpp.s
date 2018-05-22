.section .data
__0_const: .asciz "input intA  \n"
__1_const: .asciz "%d"
__2_const: .asciz "input intB  \n"
__3_const: .asciz "%d"
__4_const: .asciz "input floatA  \n"
__5_const: .asciz "%f"
__6_const: .asciz "input floatB  \n"
__7_const: .asciz "%f"
__8_const: .asciz "intA+intB=%d\n"
__9_const: .asciz "intA-intB=%d\n"
__10_const: .asciz "intA*intB=%d\n"
__11_const: .asciz "intA/intB=%d\n"
__12_const: .asciz "floatA+floatB=%f\n"
__13_const: .asciz "floatA-floatB=%f\n"
__14_const: .asciz "floatA*floatB=%f\n"
__15_const: .asciz "floatA/floatB=%f\n"
__16_const: .asciz "intA+floatB=%f\n"
__17_const: .asciz "floatA*intB=%f\n"
__18_const: .asciz "intA=%d\n"
__19_const: .asciz "intB=%d\n"
__20_const: .asciz "floatA=%f\n"
__21_const: .asciz "floatB=%f\n"
__22_const: .int 0

.section .text

.globl _main
_main:
    pushl %ebp
    movl %esp, %ebp
    sub $32,%esp
#调用函数:printf
    movl $__0_const,0(%esp)
    call _printf

#调用函数:scanf
    movl $__1_const,%ebx
    movl %ebx,0(%esp)
    leal -4(%ebp),%ebx
    movl %ebx,4(%esp)
    call _scanf

#调用函数:printf
    movl $__2_const,0(%esp)
    call _printf

#调用函数:scanf
    movl $__3_const,%ebx
    movl %ebx,0(%esp)
    leal -8(%ebp),%ebx
    movl %ebx,4(%esp)
    call _scanf

#调用函数:printf
    movl $__4_const,0(%esp)
    call _printf

#调用函数:scanf
    movl $__5_const,%ebx
    movl %ebx,0(%esp)
    leal -12(%ebp),%ebx
    movl %ebx,4(%esp)
    call _scanf

#调用函数:printf
    movl $__6_const,0(%esp)
    call _printf

#调用函数:scanf
    movl $__7_const,%ebx
    movl %ebx,0(%esp)
    leal -16(%ebp),%ebx
    movl %ebx,4(%esp)
    call _scanf

#调用函数:printf
    movl $__8_const,0(%esp)
    movl -4(%ebp),%eax
    movl -8(%ebp),%ebx
    add %ebx,%eax
    movl %eax,-20(%ebp)
    movl -20(%ebp),%eax
    movl %eax,4(%esp)
    call _printf

#调用函数:printf
    movl $__9_const,0(%esp)
    movl -4(%ebp),%eax
    movl -8(%ebp),%ebx
    sub %ebx,%eax
    movl %eax,-20(%ebp)
    movl -20(%ebp),%eax
    movl %eax,4(%esp)
    call _printf

#调用函数:printf
    movl $__10_const,0(%esp)
    movl -4(%ebp),%eax
    imul -8(%ebp),%eax
    movl %eax,-20(%ebp)
    movl -20(%ebp),%eax
    movl %eax,4(%esp)
    call _printf

#调用函数:printf
    movl $__11_const,0(%esp)
    movl -4(%ebp),%eax
    CDQ
    idiv -8(%ebp),%eax
    movl %eax,-20(%ebp)
    movl -20(%ebp),%eax
    movl %eax,4(%esp)
    call _printf

#调用函数:printf
    movl $__12_const,0(%esp)
    flds -12(%ebp)
    flds -16(%ebp)
    faddp
    fstps -20(%ebp)
    flds -20(%ebp)
    fstpl 4(%esp)
    call _printf

#调用函数:printf
    movl $__13_const,0(%esp)
    flds -16(%ebp)
    flds -12(%ebp)
    fsubp 
    fstps -20(%ebp)
    flds -20(%ebp)
    fstpl 4(%esp)
    call _printf

#调用函数:printf
    movl $__14_const,0(%esp)
    flds -12(%ebp)
    fmul -16(%ebp)
    fstps -20(%ebp)
    flds -20(%ebp)
    fstpl 4(%esp)
    call _printf

#调用函数:printf
    movl $__15_const,0(%esp)
    flds -12(%ebp)
    fdiv -16(%ebp)
    fstps -20(%ebp)
    flds -20(%ebp)
    fstpl 4(%esp)
    call _printf

#调用函数:printf
    movl $__16_const,0(%esp)
    fild -4(%ebp)
    fadd -16(%ebp)
    fstps -20(%ebp)
    flds -20(%ebp)
    fstpl 4(%esp)
    call _printf

#调用函数:printf
    movl $__17_const,0(%esp)
    flds -12(%ebp)
    fimul -8(%ebp)
    fstps -20(%ebp)
    flds -20(%ebp)
    fstpl 4(%esp)
    call _printf

#调用函数:printf
    movl $__18_const,0(%esp)
    movl -4(%ebp),%eax
    movl %eax,4(%esp)
    call _printf

#调用函数:printf
    movl $__19_const,0(%esp)
    movl -8(%ebp),%eax
    movl %eax,4(%esp)
    call _printf

#调用函数:printf
    movl $__20_const,0(%esp)
    flds -12(%ebp)
    fstpl 4(%esp)
    call _printf

#调用函数:printf
    movl $__21_const,0(%esp)
    flds -16(%ebp)
    fstpl 4(%esp)
    call _printf

    #Sen->@null产生的空行
    movl $__22_const,%ebx
    movl (%ebx),%eax
    leave
    ret
