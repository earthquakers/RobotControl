package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * A class containing speeds and factors to fine tune the control
 * Here you can change the 'feel' of the controls
 */
public class Constants{

    /**
     * The speed is multiplied by this if the boost button is NOT pressed
     * Number has to be < 1.0 to have the desired effect
     */
    public final static double BOOSTFACTOR = 0.7;

    /**
     * The exponent of the speed if the boost button is NOT pressed
     * Cannot be a negative number!
     */
    public final static double BOOSTEXPONENT = 2.0;

    /**
     * The exponent of the speed if the boost button is pressed
     * Cannot be a negative number!
     */
    public final static double EXPONENT = 2.0;

    /**
     * The speed of the flipper/brush when it is turned on
     * Ranging from -1.0 (reversed) to 1.0
     */
    public final static double FLIPPER_SPEED_ON = 1.0;

    /**
     * The speed of the flipper/brush when it is turned off
     * Ranging from -1.0 (reversed) to 1.0
     */
    public final static double FLIPPER_SPEED_OFF = 0.0;

    /**
     * The steering value (gamepad x axis) is multiplied by this
     * Make negative to invert steering
     */
    public final static double STEER_SENS = -0.5;

    /**
     * The minimum position of the valve servo
     * Ranging from 0.0 to 1.0
     */
    public final static double BAGSWITCH_MINPOS = 0.0;

    /**
     * The maximum position of the valve servo
     * Ranging from 0.0 to 1.0
     */
    public final static double BAGSWITCH_MAXPOS = 1.0;

    /**
     * The names of the engines and servos
     */
    public final static String[] ENTITY_NAMES = {
            /**
             * Front Left driving engine
             */
            "fl", // <-- Change this

            /**
             * Front Right driving engine
             */
            "fr", // <-- Change this

            /**
             * Rear Left driving engine
             */
            "rl", // <-- Change this

            /**
             * Rear right driving engine
             */
            "rr", // <-- Change this

            /**
             * The flipper/brush on the front
             */
            "flipper", // <-- Change this

            /**
             * The valve on the top of the robot
             */
            "valve" // <-- Change this
    };

}