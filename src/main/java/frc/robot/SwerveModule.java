package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;

import org.usfirst.frc3620.misc.MotorStatus;
import org.usfirst.frc3620.misc.SwerveModuleId;

public class SwerveModule {

  SwerveModuleId id;
  String name;
  CANSparkMax driveMotor;
  TalonSRX azimuthMotor;
  MotorStatus driveMotorStatus, azimuthMotorStatus;

  public SwerveModule (SwerveModuleId id, CANSparkMax driveMotor, TalonSRX azimuthMotor) {
    this.id = id;
    this.name = id.getName();
    this.driveMotor = driveMotor;
    this.driveMotorStatus = new MotorStatus("swerve." + id + ".drive", driveMotor);
    this.azimuthMotor = azimuthMotor;
    this.azimuthMotorStatus = new MotorStatus("swerve." + id + ".azimuth", azimuthMotor);
  }

  public String getName() {
    return name;
  }

  public CANSparkMax getDriveMotor() {
    return driveMotor;
  }

  public TalonSRX getAzimuthMotor() {
    return azimuthMotor;
  }

  public MotorStatus getDriveMotorStatus() {
    return driveMotorStatus;
  }

  public MotorStatus getAzimuthMotorStatus() {
    return azimuthMotorStatus;
  }

  public void setDriveMotorPower(double p) {
    if (driveMotor != null) {
      driveMotor.set(p);
    }
  }

  public void stopDriveMotor() {
    if (driveMotor != null) {
      driveMotor.stopMotor();
    }
  }

  public void setAzimuthMotorPower(double p) {
    if (azimuthMotor != null) {
      azimuthMotor.set(ControlMode.PercentOutput, p);
    }
  }

  public void stopAzimuthMotor() {
    if (azimuthMotor != null) {
      azimuthMotor.neutralOutput();
    }
  }

  public void updateBasicDashboard() {
    driveMotorStatus.gatherActuals();
    azimuthMotorStatus.gatherActuals();
  }
}
