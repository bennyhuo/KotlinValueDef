package com.bennyhuo.kotlin.valuedef.common.value

/**
 * Created by benny.
 */
class IntRangeList(val min: Int, val max: Int) : List<Int> {
    override val size: Int
        get() = max - min + 1

    override fun contains(element: Int): Boolean {
        return element in min..max
    }

    override fun containsAll(elements: Collection<Int>): Boolean {
        return elements.all { it in this }
    }

    override fun get(index: Int): Int {
        return min + index
    }

    override fun indexOf(element: Int): Int {
        return element - min
    }

    override fun isEmpty(): Boolean {
        return min > max
    }

    override fun iterator(): Iterator<Int> {
        return (min..max).iterator()
    }

    override fun lastIndexOf(element: Int): Int {
        return element - min
    }

    override fun listIterator(): ListIterator<Int> = TODO()

    override fun listIterator(index: Int): ListIterator<Int> = TODO()

    override fun subList(fromIndex: Int, toIndex: Int): List<Int> {
        return IntRangeList(min + fromIndex, min + toIndex)
    }

    override fun toString(): String {
        return "[$min .. $max]"
    }
}