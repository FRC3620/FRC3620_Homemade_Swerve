package org.usfirst.frc3620.misc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

abstract public class DataExtractor<T extends NamedObject> {
  String prefix = "", middle = "";

  static class MethodAndNames {
    Method method;
    String suffix;
    MethodAndNames(Method x, String suffix) {
      this.method = x;
      this.suffix = suffix;
    }
  }

  public DataExtractor<T> addPrefix(String prefix) {
    this.prefix = prefix;
    return this;
  }

  public DataExtractor<T> addMiddle(String middle) {
    this.middle = middle;
    return this;
  }

  List<MethodAndNames> l = new ArrayList<>();

  public void addField(Method method) {
    addField(method, null);
  }

  public void addField(Method method, String name) {
    if (name == null) {
      name = method.getName();

      NetworkTableEntryInformation anno = method.getAnnotation(NetworkTableEntryInformation.class);

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
      String fqName = prefix + object.getName() + "." + middle + "."+ supplier.suffix;
      fqName = fqName.replace("..", ".");
      place (fqName, x);
    }
  }

  abstract public void place (String name, Object o);
}
