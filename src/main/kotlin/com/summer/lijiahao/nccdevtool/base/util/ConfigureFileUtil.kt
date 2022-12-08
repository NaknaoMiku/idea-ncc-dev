package com.summer.lijiahao.nccdevtool.base.util

import java.io.*
import java.nio.charset.StandardCharsets

class ConfigureFileUtil {

    fun readTemplate(fileName: String): String {
        val inputStream = this.javaClass.getResourceAsStream("/template/$fileName")
        return readTemplate(inputStream)
    }

    fun readTemplate(file: File?): String {
        return try {
            val fileInputStream = FileInputStream(file)
            readTemplate(fileInputStream)
        } catch (e: FileNotFoundException) {
            throw FileNotFoundException(e.message)
        }
    }

    private fun readTemplate(inputStream: InputStream): String {
        val tempBuilder = StringBuilder()
        try {
            val isr = InputStreamReader(inputStream, StandardCharsets.UTF_8)
            val br = BufferedReader(isr)
            var lineTxt: String?
            while (true) {
                if (br.readLine().also { lineTxt = it } == null) break
                tempBuilder.append(lineTxt)
                tempBuilder.append("\r\n")
            }
            br.close()
        } catch (e: IOException) {
            throw IOException(e.message)
        }
        return tempBuilder.toString()
    }

    fun outFile(file: File?, content: String?, charset: String?, bomFlag: Boolean) {
        try {
            val fos = FileOutputStream(file)
            var dos: OutputStreamWriter? = null
            if (bomFlag) {
                fos.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))
            }
            dos = OutputStreamWriter(fos, charset)
            dos.write(content)
            dos.close()
        } catch (_: FileNotFoundException) {
        } catch (_: IOException) {
        }
    }
}