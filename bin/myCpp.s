.section .data
total: .space 4
sum: .space 4
__0_const: .int 0
__1_const: .asciz "input array[10] of char_\n"
 .space 2#碎片填充  
__2_const: .int 10
__3_const: .asciz "%c"
 .space 2#碎片填充  
__4_const: .int 1
__5_const: .int 0
__6_const: .asciz "before sort_\n"
 .space 2#碎片填充  
__7_const: .int 10
__8_const: .asciz "a[%d] is %c\n"
 .space 3#碎片填充  
__9_const: .int 1
__10_const: .int 1
__11_const: .int 0
__12_const: .int 0
__13_const: .int 0
__14_const: .int 10
__15_const: .int 1
__16_const: .int 1
__17_const: .int 1
__18_const: .int 1
__19_const: .int 1
__20_const: .int 1
__21_const: .asciz "after sort_\n"
 .space 3#碎片填充  
__22_const: .int 0
__23_const: .int 10
__24_const: .asciz "a[%d] is %c\n"
 .space 3#碎片填充  
__25_const: .int 1
__26_const: .asciz "input float b_"
__27_const: .asciz "%f"
__28_const: .asciz "input float d_"
__29_const: .asciz "%f"
__30_const: .asciz "b is %f\n"
__31_const: .asciz "d is %f\n"
__32_const: .asciz "maxnum is %f\n"
__33_const: .int 0

.section .text

.globl _max
_max:
    pushl %ebp
    movl %esp, %ebp
    sub $8,%esp
    movl 8(%ebp),%eax
    movl %eax,-4(%ebp)
    flds 8(%ebp)
    fcomp 12(%ebp)
    fnstsw %ax
    sahf
    JAE L0
    jmp L1
L0:     movl 8(%ebp),%eax
    movl %eax,-4(%ebp)
    #Sen->@null产生的空行
    jmp L2
L1:     movl 12(%ebp),%eax
    movl %eax,-4(%ebp)
    #Sen->@null产生的空行
L2:     #Sen->@null产生的空行
    movl -4(%ebp),%eax
    leave
    ret

.globl _main
_main:
    pushl %ebp
    movl %esp, %ebp
    sub $56,%esp
    movl __0_const,%eax
    movl %eax,-24(%ebp)
#调用函数:printf
    movl $__1_const,0(%esp)
    call _printf

L3:     movl -24(%ebp),%eax
    movl __2_const,%ebx
    cmp %ebx,%eax
    JL L4
    jmp L5
L4: #调用函数:scanf
    movl $__3_const,%ebx
    movl %ebx,0(%esp)
    leal -28(%ebp),%ebx
    movl %ebx,4(%esp)
    call _scanf

    movl -24(%ebp),%esi
    leal -13(%ebp,%esi,1),%ebx
    movb -28(%ebp),%al
    movb %al,%al
    movb %al,(%ebx)
#调用函数:getchar
    call _getchar

    movl -24(%ebp),%eax
    movl __4_const,%ebx
    add %ebx,%eax
    movl %eax,-32(%ebp)
    movl -32(%ebp),%eax
    movl %eax,-24(%ebp)
    #CirSen->@null产生的空行
    jmp L3
L5:     movl __5_const,%eax
    movl %eax,-24(%ebp)
#调用函数:printf
    movl $__6_const,0(%esp)
    call _printf

L6:     movl -24(%ebp),%eax
    movl __7_const,%ebx
    cmp %ebx,%eax
    JL L7
    jmp L8
L7: #调用函数:printf
    movl $__8_const,0(%esp)
    movl -24(%ebp),%eax
    movl %eax,4(%esp)
    movl -24(%ebp),%eax
    movl %eax,%ebx
    leal -13(%ebp),%edx
    movb (%edx,%ebx,1),%al
    movb %al,%al
    movb %al,-32(%ebp)
    movsbl -32(%ebp),%eax
    movl %eax,8(%esp)
    call _printf

    movl -24(%ebp),%eax
    movl __9_const,%ebx
    add %ebx,%eax
    movl %eax,-32(%ebp)
    movl -32(%ebp),%eax
    movl %eax,-24(%ebp)
    #CirSen->@null产生的空行
    jmp L6
L8:     movl __10_const,%eax
    movl %eax,-32(%ebp)
L9:     movl -32(%ebp),%eax
    movl __11_const,%ebx
    cmp %ebx,%eax
    JNE L10
    jmp L16
L10:     movl __12_const,%eax
    movl %eax,-32(%ebp)
    movl __13_const,%eax
    movl %eax,-24(%ebp)
L11:     movl __14_const,%eax
    movl __15_const,%ebx
    sub %ebx,%eax
    movl %eax,-36(%ebp)
    movl -24(%ebp),%eax
    movl -36(%ebp),%ebx
    cmp %ebx,%eax
    JL L12
    jmp L15
L12:     movl -24(%ebp),%eax
    movl %eax,%ebx
    leal -13(%ebp),%edx
    movb (%edx,%ebx,1),%al
    movb %al,%al
    movb %al,-40(%ebp)
    movl -24(%ebp),%eax
    movl __16_const,%ebx
    add %ebx,%eax
    movl %eax,-44(%ebp)
    movl -44(%ebp),%eax
    movl %eax,%ebx
    leal -13(%ebp),%edx
    movb (%edx,%ebx,1),%al
    movb %al,%al
    movb %al,-48(%ebp)
    movb -40(%ebp),%al
    movb -48(%ebp),%bl
    cmp %bl,%al
    JG L13
    jmp L14
L13:     movl -24(%ebp),%eax
    movl %eax,%ebx
    leal -13(%ebp),%edx
    movb (%edx,%ebx,1),%al
    movb %al,%al
    movb %al,-40(%ebp)
    movb -40(%ebp),%al
    movb %al,-52(%ebp)
    movl -24(%ebp),%eax
    movl __17_const,%ebx
    add %ebx,%eax
    movl %eax,-40(%ebp)
    movl -40(%ebp),%eax
    movl %eax,%ebx
    leal -13(%ebp),%edx
    movb (%edx,%ebx,1),%al
    movb %al,%al
    movb %al,-44(%ebp)
    movl -24(%ebp),%esi
    leal -13(%ebp,%esi,1),%ebx
    movb -44(%ebp),%al
    movb %al,%al
    movb %al,(%ebx)
    movl -24(%ebp),%eax
    movl __18_const,%ebx
    add %ebx,%eax
    movl %eax,-40(%ebp)
    movl -40(%ebp),%esi
    leal -13(%ebp,%esi,1),%ebx
    movb -52(%ebp),%al
    movb %al,%al
    movb %al,(%ebx)
    movl -32(%ebp),%eax
    movl __19_const,%ebx
    add %ebx,%eax
    movl %eax,-40(%ebp)
    movl -40(%ebp),%eax
    movl %eax,-32(%ebp)
    #CirSen->@null产生的空行
L14:     movl -24(%ebp),%eax
    movl __20_const,%ebx
    add %ebx,%eax
    movl %eax,-40(%ebp)
    movl -40(%ebp),%eax
    movl %eax,-24(%ebp)
    #CirSen->@null产生的空行
    jmp L11
L15:     #CirSen->@null产生的空行
    jmp L9
L16: #调用函数:printf
    movl $__21_const,0(%esp)
    call _printf

    movl __22_const,%eax
    movl %eax,-24(%ebp)
L17:     movl -24(%ebp),%eax
    movl __23_const,%ebx
    cmp %ebx,%eax
    JL L18
    jmp L19
L18: #调用函数:printf
    movl $__24_const,0(%esp)
    movl -24(%ebp),%eax
    movl %eax,4(%esp)
    movl -24(%ebp),%eax
    movl %eax,%ebx
    leal -13(%ebp),%edx
    movb (%edx,%ebx,1),%al
    movb %al,%al
    movb %al,-40(%ebp)
    movsbl -40(%ebp),%eax
    movl %eax,8(%esp)
    call _printf

    movl -24(%ebp),%eax
    movl __25_const,%ebx
    add %ebx,%eax
    movl %eax,-40(%ebp)
    movl -40(%ebp),%eax
    movl %eax,-24(%ebp)
    #CirSen->@null产生的空行
    jmp L17
L19: #调用函数:printf
    movl $__26_const,0(%esp)
    call _printf

#调用函数:scanf
    movl $__27_const,%ebx
    movl %ebx,0(%esp)
    leal -16(%ebp),%ebx
    movl %ebx,4(%esp)
    call _scanf

#调用函数:printf
    movl $__28_const,0(%esp)
    call _printf

#调用函数:scanf
    movl $__29_const,%ebx
    movl %ebx,0(%esp)
    leal -20(%ebp),%ebx
    movl %ebx,4(%esp)
    call _scanf

#调用函数:printf
    movl $__30_const,0(%esp)
    flds -16(%ebp)
    fstpl 4(%esp)
    call _printf

#调用函数:printf
    movl $__31_const,0(%esp)
    flds -20(%ebp)
    fstpl 4(%esp)
    call _printf

#调用函数:max
    movl -16(%ebp),%eax
    movl %eax,0(%esp)
    movl -20(%ebp),%eax
    movl %eax,4(%esp)
    call _max
    movl %eax,-44(%ebp)

    movl -44(%ebp),%eax
    movl %eax,-40(%ebp)
#调用函数:printf
    movl $__32_const,0(%esp)
    flds -40(%ebp)
    fstpl 4(%esp)
    call _printf

    #Sen->@null产生的空行
    movl $__33_const,%ebx
    movl (%ebx),%eax
    leave
    ret
