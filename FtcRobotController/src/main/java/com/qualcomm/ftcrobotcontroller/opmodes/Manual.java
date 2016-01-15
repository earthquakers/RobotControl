package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * @class Manual
 * The manual opmode
 */
public class Manual extends OpMode {

    // FrontLeft, FrontRight, RearLeft, RearRight
	protected DcMotor FL, FR, RL, RR;

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

		gas = gamepad1.left_stick_y;
		steer = gamepad1.right_stick_x;

		motorControl(steer, gas, 0.5);
	}

	/**
	 * This is where the magic happens
	 * @param steering The angle, has to be between -1.0 and 1.0
	 * @param throttle The power, between -1.0 and 1.0
	 * @param motorFactor To fine tune the steering
	 */
	public void motorControl(double steering, double throttle, double motorFactor) {
		double motorFL, motorFR, motorRL, motorRR = 0.0;
		boolean reversedThrottle = false;
		//double steeringFactor = motorFactor;
		//Throttle is reversed so robot needs reveresed
		//For easy calcualtions use throttle alwats absolute and change afterwards
		if (throttle < 0.0f)
		{
			reversedThrottle = true;
			throttle = Math.abs(throttle);
		}

		//First of check the steering direction
		if(steering > 0.0f) //Steering direction is left?
		{
			//Motors on left turn slower than right to change direction
			motorFL = (throttle * motorFactor) * ( 1.0 - Math.abs(steering));
			motorRL = (throttle * motorFactor) * ( 1.0 - Math.abs(steering));

			//Motors on right stay at speed of throttle
			motorFR = (throttle * motorFactor) * ( 1.0 + Math.abs(steering));
			motorRR = (throttle * motorFactor) * ( 1.0 + Math.abs(steering));
		}
		else if (steering < 0.0f) //Steering direction is right
		{
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
}
