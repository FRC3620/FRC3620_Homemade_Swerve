// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.usfirst.frc3620.misc;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.BaseTalon;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;

/** Add your docs here. */
public class MotorSetup {
    public static void resetSparkMaxToKnownState(CANSparkMax m, boolean inverted) {
        // TODO set to factory default 
        m.setInverted(inverted);
        m.setOpenLoopRampRate(1);
        m.setClosedLoopRampRate(1);
        setBrake(m, false);
        setCurrentLimit(m, 10);
    }

    public static void setBrake (CANSparkMax m, boolean shouldBrake) {
        m.setIdleMode(shouldBrake ? IdleMode.kBrake : IdleMode.kCoast);
    }

    public static void setCurrentLimit (CANSparkMax m, int amps) {
        m.setSmartCurrentLimit(amps);
    }
    
    public static void resetCANTalonToKnownState(BaseTalon m, boolean inverted) {
        int kTimeoutMs = 0;
        m.configFactoryDefault();
        m.setInverted(inverted);
    
        /*
       //set max and minimum(nominal) speed in percentage output
        m.configNominalOutputForward(+1, kTimeoutMs);
        m.configNominalOutputReverse(-1, kTimeoutMs);
        m.configPeakOutputForward(+1, kTimeoutMs);
        m.configPeakOutputReverse(-1, kTimeoutMs);
        
        StatorCurrentLimitConfiguration amprage=new StatorCurrentLimitConfiguration(true,40,0,0); 
        m.configStatorCurrentLimit(amprage);
        */

        setBrake(m, false);
        setCurrentLimit(m, 10);
    }
    public static void setBrake (BaseTalon m, boolean shouldBrake) {
        m.setNeutralMode(shouldBrake ? NeutralMode.Brake : NeutralMode.Coast);
    }

    public static void setCurrentLimit (BaseTalon m, int amps) {
        if (m instanceof TalonFX) {
            StatorCurrentLimitConfiguration amprage=new StatorCurrentLimitConfiguration(true, amps,0,0);
            ((TalonFX) m).configStatorCurrentLimit(amprage);
        } else if (m instanceof TalonSRX) {
            ((TalonSRX) m).configContinuousCurrentLimit(amps);
            ((TalonSRX) m).configPeakCurrentLimit(0);
        }
    }


}