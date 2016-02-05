package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

//////////////////////////////////////////
// EASTER EGG PRESS ALT+F4 TO ACTIVATE! //
// #define ROBOT_APP_SPEED_FACTOR 4     //
//////////////////////////////////////////

/**
 * @class Manual
 * The manual opmode
 */
public class Manual extends OpMode {

    // FrontLeft, FrontRight, RearLeft, RearRight, Flipper
	protected DcMotor FL, FR, RL, RR, Flipper;

	private final double STEER_SENS = 0.78;
	private final double GEAR_FACTOR = 0.5;
	private final double FLIPPER_SPEED = 0.8;

	private boolean flipperPressed;

	@Override
	public void init(){
		FL = hardwareMap.dcMotor.get("fl");
		FR = hardwareMap.dcMotor.get("fr");
		RL = hardwareMap.dcMotor.get("rl");
		RR = hardwareMap.dcMotor.get("rr");

		Flipper = hardwareMap.dcMotor.get("flipper");
	}

	@Override
	public void loop(){
		double gas, steer;
		boolean gear, flippererer;

		// gamepad1 - The drivers controller
		gas = gamepad1.left_stick_y;
		steer = gamepad1.right_stick_x;
		gear = gamepad1.left_stick_button;

		// gamepad2 - The controllers controller
		flippererer = gamepad2.a;

		motorControl(steer, gas, gear);

		if(flippererer)
			toggleFlipperControl();

	}

	protected void toggleFlipperControl(){
		if(flipperPressed){
			this.Flipper.setPower(FLIPPER_SPEED);
			this.flipperPressed = false;
		}else{
			this.Flipper.setPower(0.0);
			this.flipperPressed = true;
		}
	}

	protected void motorControl(double powerIn, double steeringIn, boolean gearing){
		double steeringReal, powerReal;

		double fl, rl, fr, rr;

		steeringReal = steeringIn *= this.STEER_SENS;

		powerReal = gearing ? powerIn : powerIn * this.GEAR_FACTOR;

		if(powerReal == 0.0) {
			fl = -steeringReal;
			rl = -steeringReal;
			fr = steeringReal;
			rr = steeringReal;
		}else{
			fl = powerReal + steeringReal;
			rl = powerReal + steeringReal;
			fr = powerReal - steeringReal;
			rr = powerReal - steeringReal;
		}

		fl = fixBoundaries(fl);
		rl = fixBoundaries(rl);
		fr = fixBoundaries(fr);
		rr = fixBoundaries(rr);

		this.FL.setPower(fl);
		this.RL.setPower(rl);
		this.FR.setPower(fr);
		this.RR.setPower(rr);
	}

	private double fixBoundaries(double in){
		return Math.max(Math.min(in, 1.0), -1.0);
	}
}
