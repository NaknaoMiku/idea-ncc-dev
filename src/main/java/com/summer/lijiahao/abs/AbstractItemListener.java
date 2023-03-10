package com.summer.lijiahao.abs;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * δΈζηε¬
 */
public abstract class AbstractItemListener implements ItemListener {

    private final AbstractDialog dialog;

    public AbstractItemListener(AbstractDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        afterSelect(e);
    }

    public abstract void afterSelect(ItemEvent e);

    public AbstractDialog getDialog() {
        return dialog;
    }
}
