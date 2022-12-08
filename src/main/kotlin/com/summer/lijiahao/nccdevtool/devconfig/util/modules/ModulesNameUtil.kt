package com.summer.lijiahao.nccdevtool.devconfig.util.modules

import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.dom4j.Node
import org.dom4j.io.SAXReader
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class ModulesNameUtil {

    fun getModuleNameFromXML() : LinkedHashMap<String, String>  {
        val allModuleName: LinkedHashMap<String, String> = linkedMapOf()
        val reader = SAXReader()
        // 解析xml得到Document
        val document: Document = DocumentHelper.parseText(readModules())
        val moduleNodes: MutableList<Node> = document.selectNodes("//ROW")
        for (moduleNode in moduleNodes) {
            val key = moduleNode.selectSingleNode("./NAME").text
            val value = moduleNode.selectSingleNode("./DISPLAYNAME").text

            allModuleName[key] = value
        }

        return allModuleName
    }

    private fun readModules(): String {
        val inputStream = this.javaClass.getResourceAsStream("/modulesNames.xml")

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
}