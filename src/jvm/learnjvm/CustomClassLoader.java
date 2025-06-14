package jvm.learnjvm;

import java.io.*;

public class CustomClassLoader extends ClassLoader {
    private String classPath;

    public CustomClassLoader(String classPath) {
        // 指定父加载器为应用程序类加载器
        super(ClassLoader.getSystemClassLoader().getParent());
        this.classPath = classPath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classData = loadClassData(name);
        if (classData == null) {
            throw new ClassNotFoundException();
        }
        return defineClass(name, classData, 0, classData.length);
    }

    private byte[] loadClassData(String className) {
        String path = classPath + File.separatorChar +
                className.replace('.', File.separatorChar) + ".class";
        try (InputStream is = new FileInputStream(path);
             ByteArrayOutputStream byteSt = new ByteArrayOutputStream()) {
            int len;
            while ((len = is.read()) != -1) {
                byteSt.write(len);
            }
            return byteSt.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }
}
