package org.usfirst.frc3620.misc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

abstract public class DataExtractor<T extends NamedObject> {
  String name = "";

  static class MethodAndNames {
    Method method;
    String suffix;
    MethodAndNames(Method x, String suffix) {
      this.method = x;
      this.suffix = suffix;
    }
  }

  public DataExtractor (String name) {
    this.name = name;
  }

  List<MethodAndNames> l = new ArrayList<>();

  public void addField(Method method) {
    addField(method, null);
  }

  public void addField(Method method, String name) {
    if (name == null) {
      name = method.getName();

      TelemetryInformation anno = method.getAnnotation(TelemetryInformation.class);

      if (anno != null) {
        name = anno.name();
      }
    }
    l.add(new MethodAndNames(method, name));
  }

  public void extract(T object) {
    for (var supplier : l) {
      double x;
      try {
        x = (double) supplier.method.invoke(object);
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
      String fqName = name + "."+ supplier.suffix;
      fqName = fqName.replace("..", ".");
      place (fqName, x);
    }
  }

  abstract public void place (String name, Object o);
}
