package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.github.meanbeanlib.mirror.Executables;
import com.revrobotics.CANSparkMax;

import org.usfirst.frc3620.misc.DataExtractorToNetworkTables;
import org.usfirst.frc3620.misc.MotorStatus;
import org.usfirst.frc3620.misc.SwerveModuleId;

public class SwerveModule {

  SwerveModuleId id;
  String name;
  CANSparkMax driveMotor;
  TalonSRX azimuthMotor;
  MotorStatus driveMotorStatus, azimuthMotorStatus;
  DataExtractorToNetworkTables<MotorStatus> driveMotorStatusExtractor, azimuthMotorStatusExtractor;

  public SwerveModule (SwerveModuleId id, CANSparkMax driveMotor, TalonSRX azimuthMotor) {
    this.id = id;
    this.name = id.name();
    this.driveMotor = driveMotor;
    this.driveMotorStatus = new MotorStatus(this.name, driveMotor);
    this.azimuthMotor = azimuthMotor;
    this.azimuthMotorStatus = new MotorStatus(this.name, azimuthMotor);

    driveMotorStatusExtractor = new DataExtractorToNetworkTables<>();
    driveMotorStatusExtractor.addPrefix("swerve.");
    driveMotorStatusExtractor.addMiddle(".drive");
    setupStatusExtractor(driveMotorStatusExtractor);
  
    azimuthMotorStatusExtractor = new DataExtractorToNetworkTables<>();
    azimuthMotorStatusExtractor.addPrefix("swerve.");
    azimuthMotorStatusExtractor.addMiddle(".azimuth");
    setupStatusExtractor(azimuthMotorStatusExtractor);
  }

  void setupStatusExtractor(DataExtractorToNetworkTables<MotorStatus> x) {
    x.addField(Executables.findMethod(MotorStatus::getActualSensorVelocity));
    // x.addField(Executables.findMethod(MotorStatus::getActualSensorPosition));
    x.addField(Executables.findMethod(MotorStatus::getStatorCurrent));
    x.addField(Executables.findMethod(MotorStatus::getAppliedPower));
  }

  /*
   * 
    driveMotorStatusExtractor.addField(y);
    public void xxxxxxxx(MotorStatus motorStatus) {
      SmartDashboard.putNumber(name + "/position.actual", actualSensorPosition);
      SmartDashboard.putNumber(name + "/velocity.actual", actualSensorVelocity);
      SmartDashboard.putNumber(name + "/rpm.actual", actualRPM);
      SmartDashboard.putNumber(name + "/current.stator", statorCurrent);
      SmartDashboard.putNumber(name + "/current.supply", supplyCurrent);
      SmartDashboard.putNumber(name + "/applied.power", appliedPower);
    }
  
    }

   */

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
    driveMotorStatusExtractor.extract(driveMotorStatus);
    azimuthMotorStatusExtractor.extract(azimuthMotorStatus);
  }

}
