package com.summer.lijiahao.nccdevtool.devconfig.util.datasource

import com.summer.lijiahao.nccdevtool.devconfig.ui.NCCloudDevConfigDialog
import nc.uap.plugin.studio.ui.preference.rsa.AESEncode
import nc.uap.plugin.studio.ui.preference.rsa.AESGeneratorKey
import nc.uap.plugin.studio.ui.preference.rsa.Encode
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class DataSourcePwdAESEncodeUtil {

    companion object {
        private val IV = IvParameterSpec("1234567890123456".toByteArray(StandardCharsets.UTF_8))


        /**
         * 数据源密码解密
         */
        fun decrypt(dialog: NCCloudDevConfigDialog, data: String): String {
            var data = data
            return if (data[0] == '#') {
                data = data.substring(1)
                aesDecode(data, dialog.homeText!!.text)
            } else {
                Encode().decode(data)
            }
        }

        private fun aesDecode(encodedString: String, homePath: String): String {
            val decoded = ByteBuffer.allocateDirect(1024)
            try {
                val outBuffer = ByteBuffer.allocateDirect(1024)
                outBuffer.put(parseHexStr2Byte(encodedString))
                outBuffer.flip()
                val secretKeySpec: SecretKeySpec = generateKey(homePath)
                val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                cipher.init(2, secretKeySpec, IV)
                cipher.update(outBuffer, decoded)
                cipher.doFinal(outBuffer, decoded)
                decoded.flip()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val charset = Charset.forName("utf-8")
            return charset.decode(decoded).toString()
        }

        private fun parseHexStr2Byte(hexStr: String): ByteArray? {
            return if (hexStr.isEmpty()) {
                null
            } else {
                val result = ByteArray(hexStr.length / 2)
                for (i in 0 until hexStr.length / 2) {
                    val high = hexStr.substring(i * 2, i * 2 + 1).toInt(16)
                    val low = hexStr.substring(i * 2 + 1, i * 2 + 2).toInt(16)
                    result[i] = (high * 16 + low).toByte()
                }
                result
            }
        }

        private fun generateKey(homePath: String): SecretKeySpec {
            val keySpec: SecretKeySpec = if (AESEncode.query(homePath) != null) {
                SecretKeySpec(parseHexStr2Byte(AESEncode.query(homePath)), "AES")
            } else {
                val keysecByte = AESGeneratorKey.genBindIpKey()
                AESEncode.insert(parseByte2HexStr(keysecByte), homePath)
                SecretKeySpec(keysecByte, "AES")
            }
            return keySpec
        }

        private fun parseByte2HexStr(buf: ByteArray): String {
            val sb = StringBuilder()
            for (b in buf) {
                var hex = Integer.toHexString(b.toInt() and 255)
                if (hex.length == 1) {
                    hex = "0$hex"
                }
                sb.append(hex.uppercase(Locale.getDefault()))
            }
            return sb.toString()
        }

    }
}