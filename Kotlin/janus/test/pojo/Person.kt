package janus.test.pojo

/**
 * Title:实体类，不可变name，可变年龄
 * Description:
 * Project: MyStudyDoc
 * Author: JanusMix
 * Create Time:2018/8/7 13:59
 */
class Person(val name: String,var age: Int) {
    val isAdult : Boolean
        get() {     //自定义访问器
            return age > 18
        }

    override fun toString(): String {
        return "name: $name , age: $age"
    }
}