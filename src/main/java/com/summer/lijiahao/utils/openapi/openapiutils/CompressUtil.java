package com.summer.lijiahao.utils.openapi.openapiutils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;


public class CompressUtil {
    private static final int buffSize = 1024;


    public static String deflaterCompress(String source) throws Exception {
        String value = null;

        Deflater compressor = new Deflater();

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] input = source.getBytes(StandardCharsets.UTF_8);

            compressor.setLevel(-1);
            compressor.setInput(input);
            compressor.finish();
            byte[] buf = new byte[buffSize];

            while (!compressor.finished()) {
                int count = compressor.deflate(buf);
                bos.write(buf, 0, count);
            }
            value = Base64Util.encryptBASE64(bos.toByteArray());
        } finally {
            compressor.end();
        }

        return value;
    }


    public static String deflaterDecompress(String source) throws Exception {
        String value = null;

        Inflater decompressor = new Inflater();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] input = Base64Util.decryptBASE64(source);

            decompressor.setInput(input);
            byte[] buf = new byte[buffSize];

            while (!decompressor.finished()) {
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
            }

            value = bos.toString(StandardCharsets.UTF_8);
        } catch (java.util.zip.DataFormatException e) {
            throw new Exception("解压异常 ", e);
        } finally {
            decompressor.end();
        }
        return value;
    }


    public static String gzipCompress(String source) throws Exception {
        String value;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            byte[] input = source.getBytes(StandardCharsets.UTF_8);
            gzip.write(input);
            gzip.close();
            value = Base64Util.encryptBASE64(out.toByteArray());
        } catch (IOException e) {
            throw new Exception("gzip压缩异常 ", e);
        }

        return value;
    }


    public static String gzipDecompress(String source) throws Exception {
        String value;

        byte[] input = Base64Util.decryptBASE64(source);

        try(ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(input)) {


            GZIPInputStream ungzip = new GZIPInputStream(in);

            byte[] buffer = new byte[buffSize];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            ungzip.close();
            value = out.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new Exception("gzip解压异常 ", e);
        }

        return value;
    }
}
