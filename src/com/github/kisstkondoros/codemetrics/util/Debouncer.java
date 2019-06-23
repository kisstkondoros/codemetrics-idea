package com.github.kisstkondoros.codemetrics.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.util.Alarm;
import com.intellij.util.AlarmFactory;

public class Debouncer {
  private Alarm alarm = AlarmFactory.getInstance().create();;

  public void debounce(Runnable runnable) {
    alarm.cancelAllRequests();
    alarm.addRequest(() -> ApplicationManager.getApplication().runReadAction(runnable), 1000);
  }
}
