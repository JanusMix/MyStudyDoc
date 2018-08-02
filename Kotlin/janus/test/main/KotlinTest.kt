package janus.test.main

import janus.test.config.RGBColor
import janus.test.config.SimpleColor
import janus.test.util.getMnemonic
import janus.test.util.mix
import janus.test.util.mixOptimized
import java.util.Random

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
}

/**
 * 实体类，不可变name，可变年龄
 */
class Person(val name: String,var age: Int) {
    val isAdult : Boolean
        get() {     //自定义访问器
            return age > 18
        }

    override fun toString(): String {
        return "name: ${name} , age: ${age}"
    }
}
