// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.usfirst.frc3620.misc;

import com.ctre.phoenix.motorcontrol.can.BaseTalon;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/** Add your docs here. */
public class MotorStatus {

  String name;

  // only one of these will be set
  BaseTalon talon;
  CANSparkMax sparkMax;

  double requestedRPM = -1;
  double requestedSensorVelocity = -1;
  double actualRPM = -1;
  double actualSensorVelocity = -1;
  double actualSensorPosition = -1;
  double statorCurrent = -1;
  double supplyCurrent = -1;
  double appliedPower = -1;

  public MotorStatus(String _name, BaseTalon _talon) {
    this.name = _name;
    this.talon = _talon;
  }

  public MotorStatus(String _name, CANSparkMax _sparkMax) {
    this.name = _name;
    this.sparkMax = _sparkMax;
  }

  public String getName() {
    return name;
  }

  public double getRequestedRPM() {
    return requestedRPM;
  }

  public double getRequestedSensorVelocity() {
    return requestedSensorVelocity;
  }

  public double getActualSensorVelocity() {
    return actualSensorVelocity;
  }

  public double getActualSensorPosition() {
    return actualSensorPosition;
  }

  public double getActualRPM() {
    return actualRPM;
  }

  public double getStatorCurrent() {
    return statorCurrent;
  }

  public double getSupplyCurrent() {
    return supplyCurrent;
  }

  public double getAppliedPower() {
    return appliedPower;
  }

  public void setRequestedRPM(double r){
      requestedRPM = r;
      SmartDashboard.putNumber(name + ".rpm.target", r);
  }

  public void setRequestedSensorVelocity(double v) {
      requestedSensorVelocity = v;
      SmartDashboard.putNumber(name + ".velocity.target", v);
  }

  public void gatherActuals() {
    if (talon != null) {
      actualSensorPosition = talon.getSelectedSensorPosition();
      actualSensorVelocity = talon.getSelectedSensorVelocity();
      actualRPM = actualSensorVelocity * 600 / 2048;
      statorCurrent = talon.getStatorCurrent();
      supplyCurrent = talon.getSupplyCurrent();
      appliedPower = talon.getMotorOutputPercent() / 100.0;
    } else if (sparkMax != null) {
      RelativeEncoder encoder = sparkMax.getEncoder();
      actualSensorPosition = encoder.getPosition();
      actualSensorVelocity = encoder.getVelocity();
      actualRPM = actualSensorVelocity;
      statorCurrent = sparkMax.getOutputCurrent();
      supplyCurrent = -1;
      appliedPower = sparkMax.getAppliedOutput();
    } else {
      // everything got set at creation
    }
    updateDashboard();
  }

  void updateDashboard() {
    SmartDashboard.putNumber(name + ".position.actual", actualSensorPosition);
    SmartDashboard.putNumber(name + ".velocity.actual", actualSensorVelocity);
    SmartDashboard.putNumber(name + ".rpm.actual", actualRPM);
    SmartDashboard.putNumber(name + ".current.stator", statorCurrent);
    SmartDashboard.putNumber(name + ".current.supply", supplyCurrent);
    SmartDashboard.putNumber(name + ".applied.power", appliedPower);
  }
}
