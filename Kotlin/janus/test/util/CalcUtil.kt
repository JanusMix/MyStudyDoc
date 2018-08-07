package janus.test.util

import janus.test.`interface`.IExpr
import janus.test.pojo.Num
import janus.test.pojo.Sum

/**
 * Title:求值工具类
 * Description:
 * Project: MyStudyDoc
 * Author: JanusMix
 * Create Time:2018/8/7 13:56
 */
/**
 * 运算方法，接受一个表达式接口，数值表达式直接返回，求和表达式两数相加
 */
fun eval(e: IExpr): Int =
//    if (e is Num)   //智能转换，使用is判断后没必要使用as显式转换了
//        return e.value
//    if (e is Sum)
//        return eval(e.right) + eval(e.left)
//    throw IllegalArgumentException("Unknown expression")
    when (e) {
        is Num ->
            e.value
        is Sum ->
            eval(e.right) + eval(e.left)
        else ->
            throw IllegalArgumentException("Unknown expression")
    }

/**
 * 是否是字母
 */
fun isLetter(c : Char) = c in 'a'..'z' || c in 'A'..'Z'

/**
 * 是否不是数字
 */
fun isNotDigit(c : Char) = c !in '0'..'9'

/**
 * 识别字符
 * !可以将in所谓when的分支
 */
fun recognize(c : Char) =  when (c) {
        in '0'..'9' -> "It's a digit!"
        in 'a'..'z',in 'A'..'Z' -> "It's a letter"
        else -> "I don't know"
    }
