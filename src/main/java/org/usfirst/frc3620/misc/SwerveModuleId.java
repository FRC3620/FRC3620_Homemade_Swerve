package org.usfirst.frc3620.misc;

public enum SwerveModuleId {
  LF("Left Front"),
  RF("Right Front"),
  LB("Left Back"),
  RB("Right Back");

  final String name;

  SwerveModuleId(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
