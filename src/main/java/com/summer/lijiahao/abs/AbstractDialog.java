package com.summer.lijiahao.abs;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class AbstractDialog extends JDialog {

    private final Map<String, JComponent> componentMap = new HashMap();

    public <T> T getComponent(Class<T> clazz, String key) {
        return (T) componentMap.get(key);
    }

    public void addComponent(String key, JComponent component) {
        componentMap.put(key, component);
    }
}
