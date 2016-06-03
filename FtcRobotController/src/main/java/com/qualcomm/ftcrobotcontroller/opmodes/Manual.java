package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

/**
 * @class Manual
 * The manual opmode
 */
public class Manual extends OpMode {

    // FrontLeft, FrontRight, RearLeft, RearRight, Flipper
    protected DcMotor FL, FR, RL, RR, flipper;

    // The servo controlling the 'valve' on the 'bag'
    protected Servo bagswitch;

    private boolean flipperPressed, flipperActivated = false;
    private boolean bagSwitchPressed, bagSwitchActivated = false;

    @Override
    public void init(){
        ServoController sc;

        // Front Left
        FL = hardwareMap.dcMotor.get(Constants.ENTITY_NAMES[0]);

        // Front Right
        FR = hardwareMap.dcMotor.get(Constants.ENTITY_NAMES[1]);

        // Rear Left
        RL = hardwareMap.dcMotor.get(Constants.ENTITY_NAMES[2]);

        // Rear Right
        RR = hardwareMap.dcMotor.get(Constants.ENTITY_NAMES[3]);

        // The flipper/brush to grab the objects
        //flipper = hardwareMap.dcMotor.get(Constants.ENTITY_NAMES[4]);

        // The matrix module controlling the flipper and other extra engines
        //sc = hardwareMap.servoController.get("top_controller");

        // Enable PWM
        //sc.pwmEnable();

        // The switch
        //bagswitch = hardwareMap.servo.get(Constants.ENTITY_NAMES[5]);

    }

    @Override
    public void loop(){

        /**
         * Gamepad 1
         * The driver
         * If you need to change the controls, do it here
         */
        motorControl(
                // Gas
                gamepad1.left_stick_y,

                // Steering
                gamepad1.right_stick_x,

                // Boost button
                gamepad1.left_stick_button
        );
        this.toggleFlipper(gamepad2.a);

        this.toggleBagSwitch(gamepad2.b);

        if(gamepad2.left_trigger > 0.0 && !flipperActivated)
            flipper.setPower(-gamepad2.left_trigger);

        else if(gamepad2.right_trigger > 0.0 && !flipperActivated)
            flipper.setPower(gamepad2.right_trigger);

        /**
         * Gamepad 2
         * The controller
         * Change the controls in the functions here
         */
//        this.toggleFlipper(gamepad2.a);
//
//        this.toggleBagSwitch(gamepad2.b);
//
//        if(gamepad2.left_trigger > 0.0 && !flipperActivated)
//            flipper.setPower(-gamepad2.left_trigger);
//
//        else if(gamepad2.right_trigger > 0.0 && !flipperActivated)
//            flipper.setPower(gamepad2.right_trigger);
    }

    /**
     * Toggle the valve on the top
     * @param btn The valve-switch-button
     */
    private void toggleBagSwitch(boolean btn){
        if(btn && !bagSwitchPressed){
            if(!bagSwitchActivated){
                bagswitch.setPosition(Constants.BAGSWITCH_MAXPOS);
                bagSwitchActivated = true;
            }else{
                bagswitch.setPosition(Constants.BAGSWITCH_MINPOS);

                bagSwitchActivated = false;
            }
            bagSwitchPressed = true;
        }
        else if(!btn)
            bagSwitchPressed = false;
    }

    /**
     * Toggle the flipper/brush on and off
     * @param btn The flipper button
     */
    private void toggleFlipper(boolean btn){
        if(btn && !flipperPressed){
            if(!flipperActivated){
                flipper.setPower(Constants.FLIPPER_SPEED_ON);
                flipperActivated = true;
            }else{
                flipper.setPower(Constants.FLIPPER_SPEED_OFF);

                flipperActivated = false;
            }
            flipperPressed = true;
        }
        else if(!btn)
            flipperPressed = false;
    }

    /**
     * Control the engines
     * @param powerIn       The input power from the joystick (typically y axis)
     * @param steeringIn    The input steering from the joystick (typically x axis)
     * @param gearing       Is the gearing button pressed or not
     */
    protected void motorControl(double powerIn, double steeringIn, boolean gearing){
        double steeringReal, powerReal;

        double fl, rl, fr, rr;

        steeringReal = steeringIn * Constants.STEER_SENS;

        if(powerIn < 0){
            if(gearing)
                powerReal = -Math.pow(Math.abs(powerIn), Constants.EXPONENT);
            else
                powerReal = -Math.pow(Math.abs(powerIn * Constants.BOOSTFACTOR), Constants.BOOSTEXPONENT);
        }else{
            if(gearing)
                powerReal = Math.pow(powerIn, Constants.EXPONENT);
            else
                powerReal = Math.pow(powerIn * Constants.BOOSTFACTOR, Constants.BOOSTEXPONENT);
        }

        if(powerReal == 0.0) {
            fl = -steeringReal;
            fr = steeringReal;
            rl = -steeringReal;
            rr = steeringReal;
        }else{
            fl = powerReal - steeringReal;
            fr = powerReal + steeringReal;
            rl = powerReal - steeringReal;
            rr = powerReal + steeringReal;
        }

        // Values must stay in range of <-1.0, 1.0>
        fl = fixBoundaries(fl);
        rl = fixBoundaries(rl);
        fr = -fixBoundaries(fr);
        rr = -fixBoundaries(rr);

        // Apply the power to the engines
        this.FL.setPower(fl);
        this.RL.setPower(rl);
        this.FR.setPower(fr);
        this.RR.setPower(rr);
    }

    /**
     * Make sure the values don't exceed the boundaries <-1.0, 1.0>
     * @param in   The input value
     * @return     The input value between the boundaries <-1.0, 1.0>
     */
    private double fixBoundaries(double in){
        return Math.max(
                Math.min(
                        in,
                        1.0
                ),
                -1.0
        );
    }
}
