package com.github.kisstkondoros.codemetrics.configuration;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.ClickListener;
import com.intellij.ui.ColorChooser;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

public class EditorConfig implements Configurable {

    private final Configuration configuration;
    private java.util.List<BeanField> fields;

    protected EditorConfig() {
        configuration = Configuration.getInstance();
        fields = new ArrayList<>();
        colorPicker("errorColor", "Error");
        colorPicker("errorTextColor", "Error text");
        colorPicker("warningColor", "Warning");
        colorPicker("warningTextColor", "Warning text");
        colorPicker("informationColor", "Information");
        colorPicker("informationTextColor", "Information text");

        numeric("lambdaComplexity", "Lambda Complexity");
        numeric("classComplexity", "Class Complexity");
        numeric("returnComplexity", "Return Complexity");
        numeric("tryComplexity", "Try Complexity");
        numeric("forComplexity", "For Complexity");
        numeric("foreachComplexity", "Foreach Complexity");
        numeric("ifComplexity", "If Complexity");
        numeric("doWhileComplexity", "DoWhile Complexity");
        numeric("conditionalComplexity", "Conditional Complexity");
        numeric("switchComplexity", "Switch Complexity");
        numeric("whileComplexity", "While Complexity");
        numeric("polyadicComplexity", "Polyadic Complexity");
        numeric("catchComplexity", "Catch Complexity");
        numeric("breakComplexity", "Break Complexity");
        numeric("continueComplexity", "Continue Complexity");
    }

    private void numeric(String fieldName, String title) {
        fields.add(new NumericField(fieldName, title));
    }

    private void colorPicker(String fieldName, String title) {
        fields.add(new ColorPickerField(fieldName, title));
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Code Metrics";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "code.metrics";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (BeanField field : fields) {
            panel.add(field.getComponent());
        }
        return panel;
    }


    public boolean isModified() {
        for (BeanField field : fields) {
            if (field.isModified(configuration)) return true;
        }
        return false;
    }

    public void apply() {
        for (BeanField field : fields) {
            field.apply(configuration);
        }
    }

    public void reset() {
        for (BeanField field : fields) {
            field.reset(configuration);
        }
    }

    @Override
    public void disposeUIResources() {

    }


    private static abstract class BeanField<T extends JComponent> {
        String myFieldName;
        T myComponent;

        private BeanField(final String fieldName) {
            myFieldName = fieldName;
        }

        T getComponent() {
            if (myComponent == null) {
                myComponent = createComponent();
            }
            return myComponent;
        }

        abstract T createComponent();

        boolean isModified(Object instance) {
            final Object componentValue = getComponentValue();
            final Object beanValue = getBeanValue(instance);
            return !Comparing.equal(componentValue, beanValue);
        }

        void apply(Object instance) {
            setBeanValue(instance, getComponentValue());
        }

        void reset(Object instance) {
            setComponentValue(getBeanValue(instance));
        }

        abstract Object getComponentValue();

        abstract void setComponentValue(Object instance);

        @NonNls
        protected String getterName() {
            return "get" + StringUtil.capitalize(myFieldName);
        }

        @NotNull
        private String setterName() {
            return "set" + StringUtil.capitalize(myFieldName);
        }

        Object getBeanValue(Object instance) {
            try {
                Field field = instance.getClass().getField(myFieldName);
                return field.get(instance);
            } catch (NoSuchFieldException e) {
                try {
                    final Method method = instance.getClass().getMethod(getterName());
                    return method.invoke(instance);
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        void setBeanValue(Object instance, Object value) {
            try {
                Field field = instance.getClass().getField(myFieldName);
                field.set(instance, value);
            } catch (NoSuchFieldException e) {
                try {
                    final Method method = instance.getClass().getMethod(setterName(), getValueClass());
                    method.invoke(instance, value);
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        protected abstract Class getValueClass();
    }

    private static class ColorPickerField extends BeanField<JPanel> {

        private String title;
        private MyColorButton myColorButton;
        private JSlider jSlider;

        private ColorPickerField(final String fieldName, final String title) {
            super(fieldName);
            this.title = title;
            myColorButton = new MyColorButton();
        }

        JPanel createComponent() {
            JPanel jPanel = new JPanel();
            myColorButton.setText(title);
            jPanel.add(myColorButton);
            jSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 255);
            jPanel.add(jSlider);
            jSlider.addChangeListener(e -> {
                myColorButton.setColor(ColorUtil.toAlpha(myColorButton.getColor(), jSlider.getValue()));
                myColorButton.repaint();
            });
            BoxLayout mgr = new BoxLayout(jPanel, BoxLayout.X_AXIS);
            jPanel.setLayout(mgr);
            return jPanel;
        }

        Object getComponentValue() {
            return myColorButton.getColor().getRGB();
        }

        void setComponentValue(final Object instance) {
            Color color = new Color(((int) instance), true);
            jSlider.setValue(color.getAlpha());
            myColorButton.setColor(color);
        }

        @Override
        protected String getterName() {
            return "get" + StringUtil.capitalize(myFieldName);
        }

        protected Class getValueClass() {
            return Integer.class;
        }
    }

    private static class MyColorButton extends JButton {
        private Color color;

        MyColorButton() {
            setMargin(JBUI.emptyInsets());
            setDefaultCapable(false);
            setFocusable(false);
            if (SystemInfo.isMac) {
                putClientProperty("JButton.buttonType", "square");
            }

            new ClickListener() {
                @Override
                public boolean onClick(@NotNull MouseEvent e, int clickCount) {
                    final Color color = ColorChooser.chooseColor(MyColorButton.this, "Chose Color", MyColorButton.this.getColor());
                    if (color != null) {
                        setColor(color);
                    }
                    return true;
                }
            }.installOn(this);
        }

        @Override
        public void paint(Graphics g) {
            final Color color = g.getColor();
            UIUtil.applyRenderingHints(g);
            g.setColor(this.color);
            g.fillRect(2, 2, 20, 20);
            g.setColor(color);
            g.drawString(getText(), 30, 15);
        }

        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        public Dimension getPreferredSize() {
            return new Dimension(200, 20);
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }

    private class NumericField extends BeanField<JPanel> {

        private JBTextField jbTextField;
        private String title;

        public NumericField(String fieldName, String title) {
            super(fieldName);
            this.title = title;
            jbTextField = new JBTextField() {
                public Dimension getMinimumSize() {
                    return getPreferredSize();
                }

                public Dimension getPreferredSize() {
                    return new Dimension(200, 20);
                }
            };
        }

        @Override
        JPanel createComponent() {
            JPanel jPanel = new JPanel();
            JLabel label = new JLabel();
            label.setText(title);
            jPanel.add(label);
            label.setPreferredSize(new Dimension(200, 20));
            jPanel.add(jbTextField);
            BoxLayout mgr = new BoxLayout(jPanel, BoxLayout.X_AXIS);
            jPanel.setLayout(mgr);
            return jPanel;
        }

        @Override
        Object getComponentValue() {
            String text = jbTextField.getText();
            int result = 0;
            try {
                result = Integer.parseInt(text);
            } catch (Exception e) {

            }

            return result;
        }

        @Override
        void setComponentValue(Object instance) {
            jbTextField.setText(Objects.toString(instance, "0"));
        }

        @Override
        protected Class getValueClass() {
            return Integer.class;
        }
    }
}
