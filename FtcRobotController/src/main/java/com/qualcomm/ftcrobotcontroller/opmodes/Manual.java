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

    // FrontLeft, FrontRight, RearLeft, RearRight
	protected DcMotor FL, FR, RL, RR;

	protected final boolean reversed = false;
	protected final double gearfactor  = 0.5;

	@Override
	public void init(){
		FL = hardwareMap.dcMotor.get("fl");
		FR = hardwareMap.dcMotor.get("fr");
		RL = hardwareMap.dcMotor.get("rl");
		RR = hardwareMap.dcMotor.get("rr");
	}

	@Override
	public void loop(){
		double gas, steer;
		boolean gear;

		gas = gamepad1.left_stick_y;
		steer = gamepad1.right_stick_x;
		gear = gamepad1.left_stick_button;

		motorControlNew(steer, gas, gear);
	}

	/**
	 * This is where the magic happens
	 * @param steering The angle, has to be between -1.0 and 1.0
	 * @param throttle The power, between -1.0 and 1.0
	 * @param motorFactor Undefined behaviour???? Nobody knows...
	 */
	@Deprecated // Apologies
	public void motorControl(double steering, double throttle, double motorFactor, boolean gearing) {
		double motorFL, motorFR, motorRL, motorRR = 0.0;
		boolean reversedThrottle = false;

		//Throttle is reversed so robot needs reveresed
		//For easy calcualtions use throttle always absolute and change afterwards
		if (throttle < 0.0f)
		{
			reversedThrottle = true;
			throttle = Math.abs(throttle);
		}

		if(!gearing)
			throttle *= Constants.SPEEDFACTOR;

		//First of check the steering direction
		if(steering > 0.0f) //Steering direction is left?
		{
			if(throttle == 0.0f){
				steering *= motorFactor;
			}

			//Motors on left turn slower than right to change direction
			motorFL = (throttle * motorFactor) * ( 1.0 - Math.abs(steering));
			motorRL = (throttle * motorFactor) * ( 1.0 - Math.abs(steering));

			//Motors on right stay at speed of throttle
			motorFR = (throttle * motorFactor) * ( 1.0 + Math.abs(steering));
			motorRR = (throttle * motorFactor) * ( 1.0 + Math.abs(steering));
		}
		else if (steering < 0.0f) //Steering direction is right
		{
			if(throttle == 0.0f){
				steering *= motorFactor;
			}

			//Motors on right turn slower than left to change direction
			motorFR = (throttle * motorFactor) * ( 1.0 - Math.abs(steering));
			motorRR = (throttle * motorFactor) * ( 1.0 - Math.abs(steering));

			//Motors on right stay at speed of throttle
			motorFL = (throttle * motorFactor) * ( 1.0 + Math.abs(steering));
			motorRL = (throttle * motorFactor) * ( 1.0 + Math.abs(steering));
		}
		else	//Steering direction is zero so robot moves forward
		{
			motorFL = (throttle * motorFactor);
			motorFR = (throttle * motorFactor);
			motorRL = (throttle * motorFactor);
			motorRR = (throttle * motorFactor);
		}

		if(reversedThrottle)
		{
			motorFL = motorFL * (-1.0);
			motorFR = motorFR * (-1.0);
			motorRL = motorRL * (-1.0);
			motorRR = motorRR * (-1.0);
		}

		telemetry.addData("~", String.format("%f, %f, %f, %f, %f, %f", motorFL, motorFR, motorRL, motorRR, steering, throttle));

		// Double check
		motorFL = Math.min(Math.max(motorFL, -1.0), 1.0);
		motorFR = Math.min(Math.max(motorFR, -1.0), 1.0);
		motorRL = Math.min(Math.max(motorRL, -1.0), 1.0);
		motorRR = Math.min(Math.max(motorRR, -1.0), 1.0);




		// Another one
		this.FL.setPower(motorFL);

		// Another one
		this.FR.setPower(-motorFR);

		// Another one
		this.RL.setPower(motorRL);

		// Another one
		this.RR.setPower(-motorRR);
	}

	public void motorControlNew(double power, double steering, boolean gearing){
		double motorFL, motorFR, motorRL, motorRR;
		double leftPower, rightPower;

		if(!gearing)
			power *= this.gearfactor;

		if(this.reversed)
			leftPower = power - steering;
		else
			leftPower = power + steering;

		if(this.reversed)
			rightPower = power + steering;
		else
			rightPower = power - steering;

		motorFL = leftPower;
		motorRL = leftPower;

		motorFR = rightPower;
		motorRR = rightPower;

		telemetry.addData("~", String.format("%g, %g", leftPower, rightPower));

		// Make sure the values don't exceed 1.0 or go below -1.0
		motorFL = Math.min(Math.max(motorFL, -1.0), 1.0);
		motorFR = Math.min(Math.max(motorFR, -1.0), 1.0);
		motorRL = Math.min(Math.max(motorRL, -1.0), 1.0);
		motorRR = Math.min(Math.max(motorRR, -1.0), 1.0);

		// Assign the values
		this.FL.setPower(motorFL);
		this.FR.setPower(motorFR);
		this.RL.setPower(motorRL);
		this.RR.setPower(motorRR);
	}
}
