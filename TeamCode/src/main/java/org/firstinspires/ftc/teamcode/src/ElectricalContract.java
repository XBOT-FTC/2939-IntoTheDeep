package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class ElectricalContract {
    public ElectricalContract() {

    }

    public static String imu() {
        return "imu";
    }
    public static String FrontLeftDriveMotor() {
        return "lf_drive";  // Control Hub, Port 3 also par dead wheel
    }
    public static String BackLeftDriveMotor() {
        return "lb_drive";  // Control Hub, Port 0 also perp dead wheel
    }
    public static String FrontRightDriveMotor() {
        return "rf_drive";  // Control Hub, Port 2
    }
    public static String BackRightDriveMotor() {
        return "rb_drive";  // Control Hub, Port 1
    }



}
