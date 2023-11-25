package frc.robot.commands;

import edu.wpi.first.networktables.BooleanEntry;
import edu.wpi.first.networktables.DoubleEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.DrivingDataLogger;
import frc.robot.subsystems.DriveSubsystem;
import org.usfirst.frc3620.logger.IFastDataLogger;

public class SpinAzimuthMotorsCommand extends CommandBase {
  DriveSubsystem driveSubsystem;
  DoubleEntry powerEntry;
  BooleanEntry shouldCollectEntry;
  IFastDataLogger dataLogger;

  public SpinAzimuthMotorsCommand(DriveSubsystem driveSubsystem) {
    this.driveSubsystem = driveSubsystem;
    powerEntry = NetworkTableInstance.getDefault().getDoubleTopic("/SmartDashboard/azimuthPower").getEntry(0);
    shouldCollectEntry = NetworkTableInstance.getDefault().getBooleanTopic("/SmartDashboard/shouldCollectSwerve").getEntry(false);

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (!powerEntry.exists()) powerEntry.set(0.0);
    if (!shouldCollectEntry.exists()) shouldCollectEntry.set(false);

    if (shouldCollectEntry.get()) {
      dataLogger = DrivingDataLogger.getDrivingDataLogger("azimuthMotors");
      dataLogger.start();
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double p = powerEntry.get();
    driveSubsystem.setAzimuthMotorPowers(p);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    driveSubsystem.stopAzimuthMotors();
    if (dataLogger != null) {
      dataLogger.done();
      dataLogger = null;
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
