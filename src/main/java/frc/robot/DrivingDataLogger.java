package frc.robot;

import com.github.meanbeanlib.mirror.Executables;
import org.usfirst.frc3620.logger.FastDataLoggerCollections;
import org.usfirst.frc3620.logger.IDataLoggerDataProvider;
import org.usfirst.frc3620.logger.IFastDataLogger;
import org.usfirst.frc3620.misc.MotorStatus;
import org.usfirst.frc3620.misc.TelemetryInformation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

public class DrivingDataLogger {
    public static IFastDataLogger getDrivingDataLogger (String name) {
        return getDrivingDataLogger(name, 15.0);
    }

    public static IFastDataLogger getDrivingDataLogger (String name, double length) {
        IFastDataLogger dataLogger = new FastDataLoggerCollections();
        dataLogger.setInterval(0.1);
        dataLogger.setMaxLength(length);
        dataLogger.setFilename(name);
        Date timestamp = new Date();
        dataLogger.setFilenameTimestamp(timestamp);

        dataLogger.addMetadata("timestamp", timestamp.toString());

        for (SwerveModule module : RobotContainer.driveSubsystem.getSwerveModules()) {
            addMotor (dataLogger, module.driveMotorStatus);
            addMotor (dataLogger, module.azimuthMotorStatus);
        }

        return dataLogger;
    }

    private DrivingDataLogger() {}

    static void addMotor (IFastDataLogger dataLogger, MotorStatus motorStatus) {
        addField(dataLogger, motorStatus, Executables.findMethod(MotorStatus::getActualSensorVelocity));
        addField(dataLogger, motorStatus, Executables.findMethod(MotorStatus::getActualSensorPosition));
        addField(dataLogger, motorStatus, Executables.findMethod(MotorStatus::getStatorCurrent));
        addField(dataLogger, motorStatus, Executables.findMethod(MotorStatus::getSupplyCurrent));
        addField(dataLogger, motorStatus, Executables.findMethod(MotorStatus::getAppliedPower));
    }

    static void addField (IFastDataLogger datalogger, MotorStatus motorStatus, Method method) {
        String name = method.getName();
        TelemetryInformation anno = method.getAnnotation(TelemetryInformation.class);
        if (anno != null) {
            name = anno.name();
        }
        String fieldName = motorStatus.getName() + "." + name;
        datalogger.addDataProvider(fieldName, new MotorStatusDataProvider(motorStatus, method));
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