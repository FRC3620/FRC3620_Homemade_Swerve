package org.usfirst.frc3620.misc;

import com.ctre.phoenix.motorcontrol.can.BaseTalon;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

public class MotorStatus implements NamedObject {

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

  @NetworkTableEntryInformation(name = "velocity")
  public double getActualSensorVelocity() {
    return actualSensorVelocity;
  }

  public double getActualSensorPosition() {
    return actualSensorPosition;
  }

  public double getActualRPM() {
    return actualRPM;
  }

  @NetworkTableEntryInformation(name = "current")
  public double getStatorCurrent() {
    return statorCurrent;
  }

  public double getSupplyCurrent() {
    return supplyCurrent;
  }

  @NetworkTableEntryInformation(name = "power")
  public double getAppliedPower() {
    return appliedPower;
  }

  public void setRequestedRPM(double r){
      requestedRPM = r;
  }

  public void setRequestedSensorVelocity(double v) {
      requestedSensorVelocity = v;
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
  }
}
