package janus.test.main

import janus.test.config.RGBColor
import janus.test.config.SimpleColor
import janus.test.pojo.Num
import janus.test.pojo.Person
import janus.test.pojo.Sum
import janus.test.util.*
import java.util.*

/**
 * Title:Kotlin初体验
 * Description:
 * Project: MyStudyDoc
 * Author: JanusMix
 * Create Time:2018/07/27 16:23
 */
//这相当于main函数
fun main(args: Array<String>) {

    val trueFlag = "fuck"
    val falseFlag = "shit"

    //测试自定义访问器
    val person1 = Person("Bob", 20)
    println(person1.isAdult)

    //尝试对不可变属性赋值会直接编译期报错
//    person.name = "Peter"

    //强大的字符串模板
    println("Hello ${person1.name}! He is a ${if (person1.age > 20) trueFlag else falseFlag}")

    //引用传递，与Java同
    val person2 = person1
    person2.age = 21
    println(person2)
    println(person1)

    //使用导入的Java包
    val random = Random()
    println(random.nextInt(10))

    //调用枚举类中的方法
    println(RGBColor.RED.rgb())

    //支持组件中的方法
    println(getMnemonic(SimpleColor.VIOLET))
    println(mix(SimpleColor.YELLOW, SimpleColor.RED))
    println(mixOptimized(SimpleColor.YELLOW, SimpleColor.RED))

    //求(1+2)+4
    println(eval(Sum(Sum(Num(1), Num(2)), Num(4))))

    for (i in 1..10)    //区间迭代
        print("$i ")
    println()
    for (i in 10 downTo 1 step 2)   //带步长的区间
        print("$i ")
    println()
    for (i in 1 until 10)   //右边是开区间，其实等同于1..9，但是清晰的表达了意图
        print("$i ")
    println()

    val binaryReps = TreeMap<Char, String>()    //按key排序的map

    for (i in 'A'..'Z') {   //..还可以生成字符串区间
        val binary = Integer.toBinaryString(i.toInt())
        binaryReps[i] = binary  //可以使用map[key] = value更新值，相当于put
    }

    for ((letter, binary) in binaryReps)    //map的展开方式
        println("$letter = $binary ")

    println(isLetter('%'))
    println(isNotDigit('5'))
    println(recognize('@'))

    val number = 101
    //throw结构可以作为表达式使用，不区分受检异常和不受检异常
    val percentage =
            if (number in 0..100)
                number
            else
                throw IllegalArgumentException("A percentage value must be between 0 and 100: $number")

    //try也可以作为表达式
    val tryNumber = try {
        Integer.parseInt(trueFlag)
    } catch (e : NumberFormatException) {
        return
    }



}
