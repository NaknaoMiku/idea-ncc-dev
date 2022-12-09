package com.summer.lijiahao.nccdevtool.devconfig.util.datasource

import java.util.regex.Pattern

class CreateDataUrlUtil {
    companion object {
        fun getJDBCInfo(url: String): Array<String?> {
            val jdbc = arrayOfNulls<String>(3)
            val regex = Pattern.compile("(?<=(?://|@))([^:;]+)(?::((?<=:)\\d+))?.*?[=:/]([^<;]+).*", 128)
            val matcher = regex.matcher(url)
            if (matcher.find()) {
                jdbc[0] = matcher.group(1)
                jdbc[1] = if (matcher.group(2) != null) matcher.group(2) else ""
                jdbc[2] = matcher.group(3)
            }
            return jdbc
        }

        fun isJDBCUrl(url: String): Boolean {
            val regex = Pattern.compile("^[^:]+:[^:]+:[^:]+$", 136)
            val matcher = regex.matcher(url)
            return !matcher.find()
        }

        fun getJDBCUrl(example: String, database: String, host: String, port: String): String {
            val url = StringBuffer()
            val regex = Pattern.compile("(.*)(?<=(?://|@))([^:;]+)(:(?<=:)\\d+)?(.*?[=:/])([^<;]+)(.*)", 128)
            val matcher = regex.matcher(example)
            if (matcher.find()) {
                url.append(matcher.group(1))
                url.append(host)
                if (matcher.group(3) != null) if ("" != port) {
                    url.append(":")
                    url.append(port)
                } else {
                    url.append(matcher.group(3))
                }
                url.append(matcher.group(4))
                url.append(database)
                url.append(matcher.group(6))
            }
            return url.toString()
        }

        fun getODBCUrl(example: String, database: String): String {
            val url = StringBuffer()
            val regex = Pattern.compile("(.*:)([^:]+)", 128)
            val matcher = regex.matcher(example)
            if (matcher.find()) {
                url.append(matcher.group(1))
                url.append(database)
            }
            return url.toString()
        }
    }
}