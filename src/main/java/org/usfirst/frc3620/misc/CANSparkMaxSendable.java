package org.usfirst.frc3620.misc;

import org.slf4j.Logger;
import org.usfirst.frc3620.logger.EventLogging;
import org.usfirst.frc3620.logger.EventLogging.Level;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;

public class CANSparkMaxSendable extends CANSparkMax implements Sendable  {
    static Logger logger = EventLogging.getLogger(CANSparkMaxSendable.class, Level.INFO);
    int deviceId;
    public CANSparkMaxSendable(int deviceId, MotorType motorType) {
        super(deviceId, motorType);

        this.deviceId = deviceId;

        SendableRegistry.addLW(this, "SparkMax", deviceId);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Motor Controller");
        builder.setActuator(true);
        builder.setSafeState(this::sendableStopMotor);
        builder.addDoubleProperty("Value", this::sendableGet, this::sendableSet);
    }

    public void sendableStopMotor() {
        logger.info ("stopMotor[{}]", deviceId);
        this.stopMotor();
    }

    public void sendableSet(double p) {
        logger.info ("set[{}] {}", deviceId, p);
        this.set (p);
    }

    public double sendableGet() {
        return this.get();
    }

}