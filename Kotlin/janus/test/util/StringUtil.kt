package janus.test.util

/**
 * Title:字符串工具类
 * Description:
 * Project: MyStudyDoc
 * Author: JanusMix
 * Create Time:2018/8/14 16:43
 */
fun <T> joinToString(collection: Collection<T>,
                     separator: String,
                     prefix: String,
                     postfix:String
) : String {
    val result = StringBuilder(prefix)
    for ((index, element) in collection.withIndex()) {
        if (index >0) result.append(separator)
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}