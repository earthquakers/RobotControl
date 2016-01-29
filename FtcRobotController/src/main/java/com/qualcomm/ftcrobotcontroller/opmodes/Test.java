package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by inktvis on 29-1-16.
 */
public class Test extends OpMode {

    protected DcMotor motor;

    @Override
    public void init(){
        telemetry.addData("~", "init");
        this.motor = hardwareMap.dcMotor.get("anotherone");

    }

    @Override
    public void loop(){
        telemetry.addData("~", "loop");
        this.motor.setPower(gamepad1.left_stick_y);
    }
}
