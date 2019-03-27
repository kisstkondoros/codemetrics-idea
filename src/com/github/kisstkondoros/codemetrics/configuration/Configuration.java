package com.github.kisstkondoros.codemetrics.configuration;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

@State(
        name = "CodeMetricsConfiguration",
        storages = {@Storage("CodeMetricsConfiguration.xml")}
)
public class Configuration implements PersistentStateComponent<Configuration> {

    public int lambdaComplexity = 1;
    public int classComplexity = 1;
    public int returnComplexity = 1;
    public int tryComplexity = 1;
    public int forComplexity = 1;
    public int foreachComplexity = 1;
    public int ifComplexity = 1;
    public int doWhileComplexity = 1;
    public int conditionalComplexity = 1;
    public int switchComplexity = 1;
    public int whileComplexity = 1;
    public int polyadicComplexity = 1;
    public int catchComplexity = 1;
    public int breakComplexity = 1;
    public int continueComplexity = 1;

    public static Configuration getInstance() {
        return ServiceManager.getService(Configuration.class);
    }

    public Integer errorColor = 0xFF5722FF;
    public Integer errorTextColor = 0xE2000000;
    public Integer warningColor = 0xFFEB3BFF;
    public Integer warningTextColor = 0xE2000000;
    public Integer informationColor = 0x4CAF50FF;
    public Integer informationTextColor = 0xE2000000;

    @Nullable
    @Override
    public Configuration getState() {
        return this;
    }

    @Override
    public void loadState(Configuration state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}