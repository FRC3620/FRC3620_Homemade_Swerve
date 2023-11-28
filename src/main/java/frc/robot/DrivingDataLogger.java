package frc.robot;

import com.github.meanbeanlib.mirror.Executables;

import edu.wpi.first.wpilibj.PowerDistribution;

import org.usfirst.frc3620.logger.FastDataLoggerCollections;
import org.usfirst.frc3620.logger.IDataLoggerDataProvider;
import org.usfirst.frc3620.misc.MotorStatus;
import org.usfirst.frc3620.misc.TelemetryInformation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

public class DrivingDataLogger extends FastDataLoggerCollections {
    PowerDistribution pdp = new PowerDistribution();

    public DrivingDataLogger(String name, double length) {
        super();

        setInterval(0.1);
        setMaxLength(length);
        setFilename(name);
        Date timestamp = new Date();
        setFilenameTimestamp(timestamp);

        addMetadata("timestamp", timestamp.toString());

        addDataProvider("swerve.lf.drive.pdp", () -> pdp.getCurrent(13));
        addDataProvider("swerve.rf.drive.pdp", () -> pdp.getCurrent(2));
        addDataProvider("swerve.lb.drive.pdp", () -> pdp.getCurrent(14));
        addDataProvider("swerve.rb.drive.pdp", () -> pdp.getCurrent(1));

        addDataProvider("swerve.lf.azimuth.pdp", () -> pdp.getCurrent(12));
        addDataProvider("swerve.rf.azimuth.pdp", () -> pdp.getCurrent(3));
        addDataProvider("swerve.lb.azimuth.pdp", () -> pdp.getCurrent(15));
        addDataProvider("swerve.rb.azimuth.pdp", () -> pdp.getCurrent(0));

        for (SwerveModule module : RobotContainer.driveSubsystem.getSwerveModules()) {
            addMotor (module.driveMotorStatus);
            addMotor (module.azimuthMotorStatus);
        }
    }

    void addMotor (MotorStatus motorStatus) {
        addField(motorStatus, Executables.findMethod(MotorStatus::getActualSensorVelocity));
        addField(motorStatus, Executables.findMethod(MotorStatus::getActualSensorPosition));
        addField(motorStatus, Executables.findMethod(MotorStatus::getStatorCurrent));
        addField(motorStatus, Executables.findMethod(MotorStatus::getSupplyCurrent));
        addField(motorStatus, Executables.findMethod(MotorStatus::getAppliedPower));
    }

    void addField (MotorStatus motorStatus, Method method) {
        String name = method.getName();
        TelemetryInformation anno = method.getAnnotation(TelemetryInformation.class);
        if (anno != null) {
            name = anno.name();
        }
        String fieldName = motorStatus.getName() + "." + name;
        addDataProvider(fieldName, new MotorStatusDataProvider(motorStatus, method));
    }

    static class MotorStatusDataProvider implements IDataLoggerDataProvider {
        MotorStatus motorStatus;
        Method method;
        MotorStatusDataProvider (MotorStatus motorStatus, Method method) {
            this.motorStatus = motorStatus;
            this.method = method;
        }

        @Override
        public Object get() {
            try {
                return method.invoke(motorStatus);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}