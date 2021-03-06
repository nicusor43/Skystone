package org.firstinspires.ftc.teamcode.robot.mecanumv2

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.*
import org.firstinspires.ftc.teamcode.modules.*
import org.firstinspires.ftc.teamcode.opmode.BBOpMode
import org.firstinspires.ftc.teamcode.opmode.get
import org.firstinspires.ftc.teamcode.test.TestLift


@TeleOp(name = "MECANUM: CONTROLLED V2 Test", group = "SKYSTONE MECANUM")
class ControlledSimple : BBOpMode() {
	override val robot: Robot = Robot(this,
		setOf(
			Mecanum(this),
			Hook(this),
			//	ArmRight(this),
			ArmV3Module(this),
			//Lift(this),
			DcLinear(this),
			ServoLinear(this),
			Intake(this),
			LiftModuleFree(this)
		))


	override fun init() { 
		robot.modules.forEach { it.init() }
		get<Mecanum>().motors.forEach { it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE }
		get<ArmV3Module>().grabberDown()


		//get<ArmRight>().setup(0.18, 0.3, 1.0, 0.5583)
		// get<ArmLeft>().setup(0.0, 0.0, 0.0, 0.0)
	}

	override fun loop() {
		when {
			gamepad1.a -> get<Hook>().grab()
			gamepad1.y -> get<Hook>().ungrab()
//			gamepad1.x -> get<ArmRight>().grab()
//			gamepad1.b -> get<ArmRight>().ungrab()
//			gamepad1.dpad_down -> get<ArmRight>().goDown()
//			gamepad1.dpad_up -> get<ArmRight>().goUp()


			//gamepad2.right_bumper -> get<Intake>().run(1.0)
			//gamepad2.left_bumper -> get<Intake>().run(-1.0)
			gamepad2.x -> get<ServoLinear>().ungrab()
			gamepad2.b -> get<ServoLinear>().grab()
			gamepad2.dpad_up -> get<LiftModuleFree>().goUp()
			gamepad2.dpad_down -> get<LiftModuleFree>().goDown()
		}

		if (gamepad2.right_bumper) get<Intake>().run(1.0)
		else if (gamepad2.left_bumper) get<Intake>().run(-1.0)
		else get<Intake>().run(0.0)

		if (gamepad2.a) get<DcLinear>().fwd()
		else if (gamepad2.y) get<DcLinear>().bck()
		else get<DcLinear>().stopOvr()


/*
		if (!get<Lift>().isBusy) {
			if (timer.seconds() > TIME_TO_TAP) {
				when {
					gamepad2.dpad_left -> {
						get<Lift>().level--
						timer.reset()
					}
					gamepad2.dpad_right -> {
						get<Lift>().level++
						timer.reset()
					}
				}
			}
			when {
				gamepad2.dpad_up -> get<Lift>().update()
				gamepad2.dpad_down -> {
					val oldLvl = get<Lift>().level
					get<Lift>().level = 1
					get<Lift>().update()
					get<Lift>().level = oldLvl
				}
			}
		}
*/

		/*if (gamepad1.dpad_up) {
			maxSpeed += speedModifier
		} else if (gamepad1.dpad_down) {
			maxSpeed -= speedModifier
		}

		 */

		/*get<Lift>().run(Range.clip(-gamepad2.left_stick_y.toDouble(), 0.0, 1.0))
		get<Output>().run((-gamepad2.left_trigger + gamepad2.right_trigger).toDouble())

		maxSpeed = Range.clip(maxSpeed, 0.0, 1.0)


		 */

		get<Mecanum>().motorsWithNames.forEach { (name, motor) ->
			motor.power = Range.clip((gamepad1.left_trigger) - gamepad1.right_trigger +
				when (name) {
					"lf" -> {
						(-gamepad1.left_stick_x + -gamepad1.right_stick_x).toDouble()
					}
					"rf" -> {
						(gamepad1.left_stick_x + gamepad1.right_stick_x).toDouble()
					}
					"lb" -> {
						(gamepad1.left_stick_x + -gamepad1.right_stick_x).toDouble()
					}
					"rb" -> {
						(-gamepad1.left_stick_x + gamepad1.right_stick_x).toDouble()
					}
					else -> 0.0
				},
				-1.0, 1.0)
		}
		//-maxSpeed, maxSpeed)


		get<Mecanum>().motorsWithNames.forEach {
			telemetry.addData("MOTOR", "${it.key}: ${(it.value as DcMotor).power}")
		}

		//telemetry.addData("Color Sensor", get<ColorSensorModule>().IsSkystone())
		//telemetry.addData("Distance Sensor", get<ColorSensorModule>().Distance())

		//telemetry.addData("MAX SPEED", maxSpeed)
		//telemetry.addData("LIFT SPEED", get<Lift>().motor.power)
		//telemetry.addData("FISHING ROD SPEED", get<Output>().motor.power)
		//telemetry.addData("LEFT INTAKE SPEED", get<Intake>().leftMotor.power)
		//telemetry.addData("RIGHT INTAKE SPEED", get<Intake>().rightMotor.power)
		//telemetry.addData("OUTPUT SERVO", get<Output>().servo.position)

		telemetry.addData("click time", timer.seconds())
		//telemetry.addData("selected level", get<Lift>().level)
		//telemetry.addData("actual level", get<Lift>().actualLevel)
		telemetry.addData("CurrentPosition", get<LiftModuleFree>().motor.currentPosition)
		telemetry.addData("TargetPositon", get<LiftModuleFree>().motor.targetPosition)
		telemetry.addData("ServoPosition", get<ServoLinear>().servolinear.position)
		telemetry.update()
	}

	companion object {
		var timer = ElapsedTime()
		const val TIME_TO_TAP = 0.25

	}
}