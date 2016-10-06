package ch.bildspur.sva.sound

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * Created by cansik on 21.09.16.
 */
class LoopRingBuffer(size: Int) {
    private val buffer: FloatArray
    var position = 0
        internal set

    init {
        buffer = FloatArray(size)
    }

    constructor(values: FloatArray) : this(values.size) {
        put(values)
    }

    constructor(lrb: LoopRingBuffer, length: Int) : this(length) {
        val data = lrb.getBuffer()
        for (i in 0..length - 1)
            put(data[i])
    }

    fun put(value: Float) {
        buffer[position] = value
        position = (position + 1) % buffer.size
    }

    fun put(values: FloatArray) {
        for (i in values.indices) {
            put(values[i])
        }
    }

    operator fun get(index: Int): Float {
        return buffer[index]
    }

    fun getBuffer(): FloatArray {
        return buffer.clone()
    }

    fun getBuffer(start: Int, size: Int): FloatArray {
        val part = FloatArray(size)

        for (i in 0..size - 1) {
            val p = Math.floorMod(start + i, buffer.size)
            part[i] = buffer[p]
        }

        return part
    }

    fun size(): Int {
        return buffer.size
    }


    /***
     * Returns latest endpoints
     * @param length
     * *
     * @return
     */
    fun getLatest(length: Int): FloatArray {
        assert(length < size())

        val result = FloatArray(length)

        for (i in 0..length - 1) {
            val p = (buffer.size + position - i - 1) % buffer.size
            result[result.size - 1 - i] = buffer[p]
        }

        return result
    }

    fun saveBuffer(fileName: String) {
        val s = StringBuilder()
        val b = getBuffer()
        for (i in 0..position - 1) {
            s.append(i)
            s.append(",")
            s.append(b[i])
            s.append("\n")
        }

        try {
            Files.write(Paths.get(fileName), s.toString().toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun loadBuffer(fileName: String) {
        val input = ArrayList<String>()

        try {
            input.addAll(Files.readAllLines(Paths.get(fileName)))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // add into buffer
        for (s in input) {
            val f = java.lang.Float.parseFloat(s.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
            put(f)
        }
    }
}