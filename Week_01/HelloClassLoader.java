package Week_01;

import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Base64;

/**
 * @author atom.hu
 * @version V1.0
 * @Package com.taiping.test
 * @date 2021/1/7 16:22
 */
public class HelloClassLoader extends ClassLoader {
    public static void main(String[] args) {
       try {
         Object instance = new HelloClassLoader().findClass("Hello").newInstance();
         instance.getClass().getMethod("hello").invoke(instance);
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String helloBase64 = getBase64Str("Hello.xlass");

        byte[] bytes = decode(helloBase64);
        return defineClass(name,bytes,0,bytes.length);
    }

    /**
     * 读取指定文件并转成Base64字符
     * @param fileName
     * @return
     */
    private String getBase64Str(String fileName) {
        String projectPath = System.getProperty("user.dir");
        String packageName = this.getClass().getPackage().getName();
        String packagePath = File.separator + "src" + File.separator + packageName;

        InputStream inputStream = null;
        byte[] buffer = null;
        try {
            inputStream = new FileInputStream(projectPath + packagePath + File.separator + fileName);
            int count = 0;
            while (count == 0) {
                count = inputStream.available();
            }
            buffer = new byte[count];
            inputStream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try {
                    // 关闭inputStream流
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 对字节数组Base64编码
        return new BASE64Encoder().encode(buffer);
    }

    /**
     * 对Base64码先解码，然后根据原始移位规则用255减去每一个字节，得到原始字节码
     * @param base64
     * @return
     */
    private byte[] decode(String base64) {
        byte[] bytes = Base64.getMimeDecoder().decode(base64);
        for(int i = 0; i < bytes.length; i++){
            bytes[i] = (byte) (255 - bytes[i]);
        }
        return bytes;
    }
}
