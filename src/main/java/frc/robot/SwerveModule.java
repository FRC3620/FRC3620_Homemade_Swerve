package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.github.meanbeanlib.mirror.Executables;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;

import org.usfirst.frc3620.misc.DataExtractorToNetworkTables;
import org.usfirst.frc3620.misc.MotorSetup;
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
    this.driveMotorStatus = new MotorStatus("swerve." + this.id + ".drive", driveMotor);
    if (driveMotor != null) {
      MotorSetup.resetSparkMaxToKnownState(driveMotor, false);
      MotorSetup.setCurrentLimit(driveMotor, 20);
      driveMotor.setIdleMode(IdleMode.kBrake);
      driveMotor.getEncoder().setVelocityConversionFactor(1);
      driveMotor.getEncoder().setPositionConversionFactor(1);
    }
    this.azimuthMotor = azimuthMotor;
    this.azimuthMotorStatus = new MotorStatus("swerve." + this.id + ".azimuth", azimuthMotor);
    if (azimuthMotor != null) {
      MotorSetup.resetCANTalonToKnownState(azimuthMotor, false);
      MotorSetup.setCurrentLimit(azimuthMotor, 20);
      azimuthMotor.setNeutralMode(NeutralMode.Brake);
    }

    driveMotorStatusExtractor = new DataExtractorToNetworkTables<>(driveMotorStatus.getName());
    setupStatusExtractor(driveMotorStatusExtractor);
  
    azimuthMotorStatusExtractor = new DataExtractorToNetworkTables<>(azimuthMotorStatus.getName());
    setupStatusExtractor(azimuthMotorStatusExtractor);
  }

  void setupStatusExtractor(DataExtractorToNetworkTables<MotorStatus> x) {
    x.addField(Executables.findMethod(MotorStatus::getActualSensorVelocity));
    x.addField(Executables.findMethod(MotorStatus::getActualSensorPosition));
    x.addField(Executables.findMethod(MotorStatus::getStatorCurrent));
    x.addField(Executables.findMethod(MotorStatus::getAppliedPower));
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
    driveMotorStatusExtractor.extract(driveMotorStatus);
    azimuthMotorStatusExtractor.extract(azimuthMotorStatus);
  }

}
