package org.usfirst.frc3620.misc;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DataExtractorToNetworkTables<T extends NamedObject> extends DataExtractor<T> {

    @Override
    public void place(String name, Object o) {
        //System.out.println ("putting " + o  + " to " + name);
        if (o instanceof Double) {
            SmartDashboard.putNumber(name, (Double) o);
        } else {
            throw new RuntimeException ("funny type: " + o.getClass());
        }
    }

}
