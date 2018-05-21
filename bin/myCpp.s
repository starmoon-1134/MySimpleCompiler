.section .data
__0_const: .int 0
__1_const: .asciz "func_test_   %d\n"
 .space 3#碎片填充  
__2_const: .int 0
__3_const: .float 3.3
__4_const: .int 2
__5_const: .int 1
__6_const: .byte 'e'
 .space 3#碎片填充  
__7_const: .int 0
__8_const: .byte 'a'
 .space 3#碎片填充  
__9_const: .int 2
__10_const: .int 5
__11_const: .int 889
__12_const: .asciz "%c\n"
__13_const: .asciz "%d\n"
__14_const: .int 1
__15_const: .asciz "%f\n"
__16_const: .asciz "%f\n"
__17_const: .asciz "%f\n"
__18_const: .asciz "%d\n"
__19_const: .asciz "%f\n"
__20_const: .asciz "%f\n"
__21_const: .asciz "%f\n"
__22_const: .float 1.5
__23_const: .asciz "%d\n"
__24_const: .int 1
__25_const: .asciz "%d\n"
__26_const: .int 1
__27_const: .int 0

.section .text

.globl _aaaaa
_aaaaa:
    pushl %ebp
    movl %esp, %ebp
    sub $8,%esp
#调用函数:test
    movl 8(%ebp),%eax
    movl %eax,0(%esp)
    call _test

    movl $__0_const,%ebx
    movl (%ebx),%eax
    leave
    ret

.globl _test
_test:
    pushl %ebp
    movl %esp, %ebp
    sub $12,%esp
#调用函数:printf
    movl $__1_const,0(%esp)
    movl 8(%ebp),%eax
    movl %eax,4(%esp)
    call _printf

    movl $__2_const,%ebx
    movl (%ebx),%eax
    leave
    ret

.globl _main
_main:
    pushl %ebp
    movl %esp, %ebp
    sub $44,%esp
    movl __3_const,%eax
    movl %eax,-4(%ebp)
    movl __4_const,%eax
    movl %eax,-8(%ebp)
    movl __5_const,%eax
    movb %al,-12(%ebp)
    movb __6_const,%al
    movb %al,-13(%ebp)
    movl __7_const,%esi
    leal -14(%ebp,%esi,1),%ebx
    movb __8_const,%al
    movb %al,%al
    movb %al,(%ebx)
    movl __9_const,%esi
    leal -20(%ebp,%esi,4),%ebx
    movl __10_const,%eax
    movl %eax,%eax
    movl %eax,(%ebx)
#调用函数:aaaaa
    movl __11_const,%eax
    movl %eax,0(%esp)
    call _aaaaa

#调用函数:printf
    movl $__12_const,0(%esp)
    movb -13(%ebp),%al
    movb -12(%ebp),%bl
    sub %al,%bl
    movb %al,-32(%ebp)
    movsbl -32(%ebp),%eax
    movl %eax,4(%esp)
    call _printf

#调用函数:printf
    movl $__13_const,0(%esp)
    movl -8(%ebp),%eax
    movl %eax,%ebx
    leal -20(%ebp),%edx
    movl (%edx,%ebx,4),%eax
    movl %eax,%eax
    movl %eax,-32(%ebp)
    movl -32(%ebp),%eax
    movl __14_const,%ebx
    add %ebx,%eax
    movl %eax,-36(%ebp)
    movl -36(%ebp),%eax
    movl %eax,4(%esp)
    call _printf

#调用函数:printf
    movl $__15_const,0(%esp)
    flds -4(%ebp)
    fiadd -8(%ebp)
    fstps -32(%ebp)
    flds -32(%ebp)
    fstpl 4(%esp)
    call _printf

#调用函数:printf
    movl $__16_const,0(%esp)
    movl -32(%ebp),%eax
    movl %eax,4(%esp)
    call _printf

#调用函数:printf
    movl $__17_const,0(%esp)
    flds -4(%ebp)
    flds -4(%ebp)
    faddp
    fstps -32(%ebp)
    flds -32(%ebp)
    fstpl 4(%esp)
    call _printf

#调用函数:printf
    movl $__18_const,0(%esp)
    movl -8(%ebp),%eax
    movl -8(%ebp),%ebx
    add %ebx,%eax
    movl %eax,-32(%ebp)
    movl -32(%ebp),%eax
    movl %eax,4(%esp)
    call _printf

#调用函数:printf
    movl $__19_const,0(%esp)
    flds -4(%ebp)
    fisub -8(%ebp)
    fstps -32(%ebp)
    flds -32(%ebp)
    fstpl 4(%esp)
    call _printf

#调用函数:printf
    movl $__20_const,0(%esp)
    flds -4(%ebp)
    fild -8(%ebp)
    fsubp
    fstps -32(%ebp)
    flds -32(%ebp)
    fstpl 4(%esp)
    call _printf

#调用函数:printf
    movl $__21_const,0(%esp)
    flds __22_const
    flds -4(%ebp)
    fsubp 
    fstps -32(%ebp)
    flds -32(%ebp)
    fstpl 4(%esp)
    call _printf

#调用函数:printf
    movl $__23_const,0(%esp)
    movl -8(%ebp),%eax
    movl __24_const,%ebx
    sub %ebx,%eax
    movl %eax,-32(%ebp)
    movl -32(%ebp),%eax
    movl %eax,4(%esp)
    call _printf

#调用函数:printf
    movl $__25_const,0(%esp)
    movl __26_const,%eax
    movl -8(%ebp),%ebx
    sub %ebx,%eax
    movl %eax,-32(%ebp)
    movl -32(%ebp),%eax
    movl %eax,4(%esp)
    call _printf

    movl $__27_const,%ebx
    movl (%ebx),%eax
    leave
    ret
