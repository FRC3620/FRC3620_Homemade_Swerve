package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMaxLowLevel;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.SwerveModule;
import org.usfirst.frc3620.misc.CANDeviceFinder;
import org.usfirst.frc3620.misc.CANDeviceType;
import org.usfirst.frc3620.misc.CANSparkMaxSendable;
import org.usfirst.frc3620.misc.SwerveModuleId;

public class DriveSubsystem extends SubsystemBase {
  SwerveModule swerveLeftFront, swerveRightFront, swerveLeftBack, swerveRightBack;

  SwerveModule[] swerveModules;

  CANDeviceFinder canDeviceFinder;
  boolean shouldMakeAllCANDevices;

  public DriveSubsystem() {
    canDeviceFinder = RobotContainer.canDeviceFinder;
    shouldMakeAllCANDevices = RobotContainer.shouldMakeAllCANDevices();

    swerveRightFront = makeSwerveModule(true, SwerveModuleId.RF, 1, 2);
    swerveLeftFront = makeSwerveModule(shouldMakeAllCANDevices, SwerveModuleId.LF, 3, 4);
    swerveLeftBack = makeSwerveModule(shouldMakeAllCANDevices, SwerveModuleId.LB, 5, 6);
    swerveRightBack = makeSwerveModule(shouldMakeAllCANDevices, SwerveModuleId.RB, 7, 8);

    swerveModules = new SwerveModule[] { swerveLeftFront, swerveRightFront, swerveLeftBack, swerveRightBack};

    makeShuffleboardTab();
  }

  private SwerveModule makeSwerveModule (boolean needTolook, SwerveModuleId id, int driveMotorId, int azimuthMotorId) {
    CANSparkMaxSendable driveMotor = null;
    if (canDeviceFinder.isDevicePresent(CANDeviceType.SPARK_MAX, driveMotorId, id.getName() + " Drive") || shouldMakeAllCANDevices) {
      driveMotor = new CANSparkMaxSendable(driveMotorId, CANSparkMaxLowLevel.MotorType.kBrushless);
      addChild (id.getName() + " Drive[" + driveMotorId + "]", driveMotor);
    }

    WPI_TalonSRX azimuthMotor = null;
    if (canDeviceFinder.isDevicePresent(CANDeviceType.TALON, azimuthMotorId, id.getName() + " Azimuth") || shouldMakeAllCANDevices) {
      azimuthMotor = new WPI_TalonSRX(azimuthMotorId);
      addChild (id.getName() + " Azimuth[" + azimuthMotorId + "]", azimuthMotor);
    }
    return new SwerveModule(id, driveMotor, azimuthMotor);
  }

  @Override
  public void periodic() {
    for (var s : swerveModules) {
      s.updateBasicDashboard();
    }
  }

  public void setAzimuthMotorPowers(double p) {
    for (var s : swerveModules) {
      s.setAzimuthMotorPower(p);
    }
  }

  public void stopAzimuthMotors() {
    for (var s : swerveModules) {
      s.stopAzimuthMotor();
    }
  }

  public void setDriveMotorPowers(double p) {
    for (var s : swerveModules) {
      s.setDriveMotorPower(p);
    }
  }

  public void stopDriveMotors() {
    for (var s : swerveModules) {
      s.stopDriveMotor();
    }
  }

  void makeShuffleboardTab() {
    // ShuffleboardTab tab = Shuffleboard.getTab("Swerve Modules");
    // tab.
  }

}
