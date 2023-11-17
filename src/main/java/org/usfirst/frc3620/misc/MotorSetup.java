// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.usfirst.frc3620.misc;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.CANSparkMax.IdleMode;

/** Add your docs here. */
public class MotorSetup {
    public static void resetMaxToKnownState(CANSparkMaxSendable x, boolean inverted) {
        // TODO set to factory default 
        x.setInverted(inverted);
        x.setIdleMode(IdleMode.kCoast);
        x.setOpenLoopRampRate(1);
        x.setClosedLoopRampRate(1);
        x.setSmartCurrentLimit(80);
    }
    
    public static void resetTalonFXToKnownState(WPI_TalonFX m, InvertType invert) {
        int kTimeoutMs = 0;
        m.configFactoryDefault();
        m.setInverted(invert);
    
        /*
    
        //set max and minimum(nominal) speed in percentage output
        m.configNominalOutputForward(+1, kTimeoutMs);
        m.configNominalOutputReverse(-1, kTimeoutMs);
        m.configPeakOutputForward(+1, kTimeoutMs);
        m.configPeakOutputReverse(-1, kTimeoutMs);
        
        StatorCurrentLimitConfiguration amprage=new StatorCurrentLimitConfiguration(true,40,0,0); 
        m.configStatorCurrentLimit(amprage);
        */
        m.setNeutralMode(NeutralMode.Coast);
    }
    
}