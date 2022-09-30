package com.summer.lijiahao.script.studio.ui.preference.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Vector;

public class XMLToObject {
    public static Class[] classA = {
            Boolean.class, Character.class, Integer.class, Long.class, Double.class, Float.class, String.class,
            BigDecimal.class, int.class, char.class,
            boolean.class, long.class, double.class, float.class};

    private boolean m_AllowNoField = false;

    public static Object getJavaObjectFromFile(String fileName) throws Exception {
        return getJavaObjectFromFile(fileName, true);
    }

    public static <T> T getJavaObjectFromFile(File file, Class<T> rootClass, boolean allowNoField) throws Exception {
        DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = parser.parse(file);
        return getJavaObjectFromDocument(doc, rootClass, allowNoField);
    }

    public static Object getJavaObjectFromStream(InputStream stream, Class rootClass, boolean allowNoField) throws Exception {
        DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = parser.parse(stream);
        return getJavaObjectFromDocument(doc, rootClass, allowNoField);
    }

    public static <T> T getJavaObjectFromDocument(Document doc, Class<T> rootClass, boolean allowNoField) throws Exception {
        XMLToObject xto = new XMLToObject();
        xto.setAllowNoField(allowNoField);
        Node node = doc.getDocumentElement();
        return xto.revertDocument(node, rootClass, null);
    }

    public static Object getJavaObjectFromNode(Node node, Class rootClass, boolean allowNoField) throws Exception {
        XMLToObject xto = new XMLToObject();
        xto.setAllowNoField(allowNoField);
        return xto.revertDocument(node, rootClass, null);
    }

    public static Object getJavaObjectFromFile(String fileName, boolean allowNoField) throws Exception {
        DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = parser.parse(fileName);
        XMLToObject xto = new XMLToObject();
        xto.setAllowNoField(allowNoField);
        Node node = doc.getDocumentElement();
        Class defaultClass = "(Java lang)Middleware depoly parameter".equals(((Element) node).getAttribute("docType")) ? Class.forName("nc.bs.mw.pm.MiddleParameter") :
                Object.class;
        String classType = ((Element) node).getAttribute("ClassType");
        if (classType != null && classType.length() > 2)
            defaultClass = Class.forName(classType);
        return xto.revertDocument(node, defaultClass, null);
    }

    public static Object getJavaObjectFromFile(String fileName, Class rootClass, boolean allowNoField) throws Exception {
        return getJavaObjectFromFile(new File(fileName), rootClass, allowNoField);
    }

    private void fillFieldValue(Field f, Object o, String str) throws Exception {
        Class<?> itemClass = f.getType();
        String value = str.trim();
        boolean isObjectPrimitiveClass = false;
        Class<?>[] classA = {Boolean.class, Character.class, Integer.class, Long.class, String.class, Double.class, Float.class, BigDecimal.class};
        for (Class<?> aClass : classA) {
            if (aClass == itemClass) {
                isObjectPrimitiveClass = true;
                break;
            }
        }
        Object itemValue = null;
        if (!value.equals("null") && isObjectPrimitiveClass)
            if (itemClass == String.class) {
                itemValue = value;
            } else if (itemClass == Integer.class) {
                itemValue = Integer.valueOf(value);
            } else if (itemClass == Boolean.class) {
                itemValue = Boolean.valueOf(value);
            } else if (itemClass == Character.class) {
                itemValue = value.charAt(0);
            } else if (itemClass == Long.class) {
                itemValue = Long.valueOf(value);
            } else if (itemClass == Double.class) {
                itemValue = Double.valueOf(value);
            } else if (itemClass == Float.class) {
                itemValue = Float.valueOf(value);
            } else if (itemClass == BigDecimal.class) {
                itemValue = new BigDecimal(value);
            }
        if (isObjectPrimitiveClass) {
            f.set(o, itemValue);
            return;
        }
        if (itemClass == int.class) {
            f.setInt(o, Integer.parseInt(value));
        } else if (itemClass == boolean.class) {
            f.setBoolean(o, Boolean.parseBoolean(value));
        } else if (itemClass == char.class) {
            f.setChar(o, value.charAt(0));
        } else if (itemClass == long.class) {
            f.setLong(o, Long.parseLong(value));
        } else if (itemClass == double.class) {
            f.setDouble(o, Double.parseDouble(value));
        } else if (itemClass == float.class) {
            f.setDouble(o, Float.parseFloat(value));
        }
    }

    private Class<?> getArrayItemClass(String className, ClassLoader loader) throws Exception {
        int key = className.indexOf("[L");
        if (key >= 0) {
            int lastLoc = className.indexOf(";");
            String classPureName = className.substring(key + 2, lastLoc);
            Class<?> pureClass;
            if (loader != null) {
                pureClass = loader.loadClass(classPureName);
            } else {
                pureClass = Class.forName(classPureName);
            }
            if (key == 0)
                return pureClass;
            int[] arrayList = new int[key];
            Arrays.fill(arrayList, 1);
            return Array.newInstance(pureClass, arrayList).getClass();
        }
        String[] id = {"[B", "[C", "[I", "[J"};
        Class<?>[] type = {byte.class, char.class, int.class, long.class};
        for (int i = 0; i < id.length; i++) {
            key = className.indexOf(id[i]);
            if (key >= 0) {
                Class<?> pureClass = type[i];
                if (key == 0)
                    return pureClass;
                int[] arrayList = new int[key];
                Arrays.fill(arrayList, 1);
                return Array.newInstance(pureClass, arrayList).getClass();
            }
        }
        if (loader != null)
            return loader.loadClass(className);
        return Class.forName(className);
    }

    private boolean isAllowNoField() {
        return this.m_AllowNoField;
    }

    public void setAllowNoField(boolean newAllowNoField) {
        this.m_AllowNoField = newAllowNoField;
    }

    private boolean isArrayClass(String classArrayName) throws Exception {
        return classArrayName.startsWith("[");
    }

    private boolean isNullNode(Node cNode) {
        Node valueNode = cNode.getAttributes().getNamedItem("value");
        if (valueNode == null)
            return false;
        return valueNode.getNodeValue().equals("null");
    }

    private boolean isNullNodeArray(Node cNode) {
        Node valueNode = cNode.getAttributes().getNamedItem("arrayValue");
        if (valueNode == null)
            return false;
        return valueNode.getNodeValue().equals("null");
    }

    private boolean isPrimitive(Class cl) {
        for (Class<?> aClass : classA) {
            if (aClass == cl)
                return true;
        }
        return false;
    }

    private <T> T revertArray(Node cNode, Class<T> defautClass, String nodeName) throws Exception {
        String arrayName = defautClass.getName();
        ClassLoader loader = defautClass.getClassLoader();
        if (defautClass.isArray())
            loader = defautClass.getComponentType().getClassLoader();
        Class<?> arrayItemClass = getArrayItemClass(arrayName, loader);
        NodeList nl = cNode.getChildNodes();
        Vector<Node> vNl = new Vector<>();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeName().equals(nodeName))
                vNl.addElement(nl.item(i));
        }
        if (vNl.size() == 1 &&
                isNullNodeArray(vNl.elementAt(0)))
            return null;
        T o = (T) Array.newInstance(arrayItemClass, vNl.size());
        if (isPrimitive(arrayItemClass)) {
            for (int i = 0; i < vNl.size(); i++) {
                Node item = vNl.elementAt(i);
                String str = item.getChildNodes().item(0).getNodeValue().trim();
                setArrayPrimitiveValue(o, i, arrayItemClass, str);
            }
        } else {
            for (int i = 0; i < vNl.size(); i++) {
                Node item = vNl.elementAt(i);
                Array.set(o, i, revertDocument(item, arrayItemClass, nodeName));
            }
        }
        return o;
    }

    private <T> T revertDocument(Node item, Class<T> defaultClass, String nodeName) throws Exception {
        if (isNullNode(item))
            return null;
        String className = defaultClass.getName();
        if (isArrayClass(className))
            return (T) revertArray(item, defaultClass, nodeName);
        Class<?> classType = defaultClass.getClassLoader().loadClass(className);
        T o = newInstance(defaultClass);
        if (isPrimitive(classType))
            throw new Exception("Parse Error");
        NodeList nl = item.getChildNodes();
        Field[] fa = o.getClass().getDeclaredFields();
        for (Field field : fa) {
            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);
            if (!Modifier.isFinal(field.getModifiers())) {
                Class<?> c = field.getType();
                Node cNode = null;
                for (int j = 0; j < nl.getLength(); j++) {
                    if (field.getName().equals(nl.item(j).getNodeName())) {
                        cNode = nl.item(j);
                        break;
                    }
                }
                if (cNode == null && !field.getType().isArray()) {
                    if (!isAllowNoField()) {
                        String msg = MessageFormat.format("Description of {0} lost", field.getName());
                        throw new Exception(msg);
                    }
                } else {
                    if (field.getType().isArray()) {
                        Object oa = revertArray(item, field.getType(), field.getName());
                        field.set(o, oa);
                    } else if (isPrimitive(c)) {
                        NodeList nlc = cNode.getChildNodes();
                        if (nlc.item(0) == null) {
                            fillFieldValue(field, o, "");
                        } else {
                            fillFieldValue(field, o, nlc.item(0).getNodeValue());
                        }
                    } else {
                        Object os = revertDocument(cNode, field.getType(), nodeName);
                        field.set(o, os);
                    }
                    field.setAccessible(isAccessible);
                }
            }
        }
        return o;
    }

    private <T> T newInstance(Class<T> clazz) throws Exception {
        Constructor<T> cnst = clazz.getDeclaredConstructor();
        cnst.setAccessible(true);
        return cnst.newInstance();
    }

    private void setArrayPrimitiveValue(Object arrayObject, int location, Class<?> itemClass, String value) throws Exception {
        boolean isObjectPrimitiveClass = false;
        Class<?>[] classA = {Boolean.class, Character.class, Integer.class, Long.class, String.class, Double.class, Float.class, BigDecimal.class};
        for (Class<?> aClass : classA) {
            if (aClass == itemClass) {
                isObjectPrimitiveClass = true;
                break;
            }
        }
        Object itemValue = null;
        if (!value.equals("null") && isObjectPrimitiveClass)
            if (itemClass == String.class) {
                itemValue = value;
            } else if (itemClass == Integer.class) {
                itemValue = Integer.valueOf(value);
            } else if (itemClass == Boolean.class) {
                itemValue = Boolean.valueOf(value);
            } else if (itemClass == Character.class) {
                itemValue = value.charAt(0);
            } else if (itemClass == Long.class) {
                itemValue = Long.valueOf(value);
            } else if (itemClass == Double.class) {
                itemValue = Double.valueOf(value);
            } else if (itemClass == Float.class) {
                itemValue = Float.valueOf(value);
            } else if (itemClass == BigDecimal.class) {
                itemValue = new BigDecimal(value);
            }
        if (isObjectPrimitiveClass) {
            Array.set(arrayObject, location, itemValue);
            return;
        }
        if (itemClass == int.class) {
            Array.setInt(arrayObject, location, Integer.parseInt(value));
        } else if (itemClass == boolean.class) {
            Array.setBoolean(arrayObject, location, Boolean.parseBoolean(value));
        } else if (itemClass == char.class) {
            Array.setChar(arrayObject, location, value.charAt(0));
        } else if (itemClass == long.class) {
            Array.setLong(arrayObject, location, Long.parseLong(value));
        } else if (itemClass == double.class) {
            Array.setDouble(arrayObject, location, Double.parseDouble(value));
        } else if (itemClass == float.class) {
            Array.setDouble(arrayObject, location, Float.parseFloat(value));
        }
    }
}
