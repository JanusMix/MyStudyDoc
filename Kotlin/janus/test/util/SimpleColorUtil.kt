package janus.test.util

import janus.test.config.SimpleColor

/**
 * Title:颜色处理支持方法
 * Description:
 * Project: MyStudyDoc
 * Author: JanusMix
 * Create Time:2018/8/1 16:37
 */
/**
 * 获取颜色的快速记忆词汇（源于美国儿童颜色的记忆口诀）
 * ！when相当于switch，但是不用写break
 */
fun getMnemonic(color: SimpleColor) =
        when (color) {      //传入的是枚举类，不列举完所有情况会编译器报错；如果只想处理几种枚举情况，最后用else结束
            SimpleColor.RED -> "Richard"
            SimpleColor.ORANGE -> "Of"
            SimpleColor.YELLOW -> "York"
            SimpleColor.GREEN -> "Gave"
            SimpleColor.BLUE -> "Battle"
            SimpleColor.INDIGO -> "In"
            SimpleColor.VIOLET -> "Vein"
        //可以在一个分支上合并多个选项
//            SimpleColor.RED, SimpleColor.ORANGE -> "Sth"
        }

/**
 * when表达式的实参可以是任何对象
 * ！效率会比较低，每次调用会生成多个Set实例
 * */
fun mix(c1 : SimpleColor, c2 : SimpleColor) =
        when  (setOf(c1, c2)) {
            setOf(SimpleColor.RED, SimpleColor.YELLOW) -> SimpleColor.ORANGE
            else -> throw Exception("Dirty color")
        }

/**
 * 不带参数的when，接收任意的bool表达式
 * ！可读性差了些，但是提高了性能
 */
fun mixOptimized(c1 : SimpleColor, c2 : SimpleColor) =
        when {
            (c1 == SimpleColor.RED && c2 == SimpleColor.YELLOW) ||
                    (c1 == SimpleColor.YELLOW && c2 == SimpleColor.RED) -> SimpleColor.ORANGE
            else -> throw Exception("Dirty color")
        }