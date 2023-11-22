package frc.robot.commands;

import edu.wpi.first.networktables.DoubleEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

public class SpinDriveMotorsCommand extends CommandBase {
  DriveSubsystem driveSubsystem;
  DoubleEntry powerEntry;

  public SpinDriveMotorsCommand(DriveSubsystem driveSubsystem) {
    this.driveSubsystem = driveSubsystem;
    powerEntry = NetworkTableInstance.getDefault().getDoubleTopic("/SmartDashboard/drivePower").getEntry(0);

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (!powerEntry.exists()) powerEntry.set(0.0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double p = powerEntry.get();
    driveSubsystem.setDriveMotorPowers(p);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    driveSubsystem.stopDriveMotors();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
