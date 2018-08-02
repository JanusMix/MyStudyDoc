package janus.test.config

/**
 * Title:带属性的枚举
 * Description:
 * Project: MyStudyDoc
 * Author: JanusMix
 * Create Time:2018/8/1 16:06
 */
enum class RGBColor(private val r : Int, private val g : Int, private val b : Int) {
    RED(255,0,0),
    ORANGE(255,165,0);

    /**
     * 计算颜色rgb值
     */
    fun rgb() = (r * 256 + g) * 256 + b
}