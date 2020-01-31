package org.firstinspires.ftc.teamcode.robot.mecanumv2

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.*
import org.firstinspires.ftc.teamcode.opmode.BBOpMode
import org.firstinspires.ftc.teamcode.opmode.get

@TeleOp(name = "MECANUM: CONTROLLED V2", group = "SKYSTONE MECANUM")
class Controlled : BBOpMode() {
	override val robot: Robot = Robot(this,
		setOf(
			Mecanum(this),
			Hook(this),
			Lift(this),
			Intake(this),
			Output(this)))

	var maxSpeed = 1.0
	var speedModifier = 0.0005

	override fun init() {
		robot.modules.forEach { it.init() }
	}

	override fun loop() {
		get<Intake>().run(0.0  )
		when {
			gamepad2.a -> get<Hook>().grab()
			gamepad2.y -> get<Hook>().ungrab()
			gamepad1.y -> get<Lift>().float()
			gamepad2.right_bumper -> get<Intake>().run(1.0)
			gamepad2.left_bumper -> get<Intake>().run(-1.0)
			gamepad2.x -> get<Output>().grab()
			gamepad2.b -> get<Output>().ungrab()
		}

		if (gamepad1.dpad_up) {
			maxSpeed += speedModifier
		} else if (gamepad1.dpad_down) {
			maxSpeed -= speedModifier
		}

		get<Lift>().run(Range.clip(-gamepad2.left_stick_y.toDouble(), 0.0, 1.0))
		get<Output>().run((-gamepad2.left_trigger + gamepad2.right_trigger).toDouble())

		maxSpeed = Range.clip(maxSpeed, 0.0, 1.0)

		get<Mecanum>().motorsWithNames.forEach { (name, motor) ->
			motor.power = Range.clip((gamepad1.left_trigger) - gamepad1.right_trigger +
				when (name) {
					"lf" -> {
						(-gamepad1.left_stick_x + -gamepad1.right_stick_x).toDouble()
					}
					"rf" -> {
						(-gamepad1.left_stick_x + gamepad1.right_stick_x).toDouble()
					}
					"lb" -> {
						(gamepad1.left_stick_x + -gamepad1.right_stick_x).toDouble()
					}
					"rb" -> {
						(gamepad1.left_stick_x + gamepad1.right_stick_x).toDouble()
					}
					else -> 0.0
				},
				-maxSpeed, maxSpeed)
		}

		get<Mecanum>().motorsWithNames.forEach {
			telemetry.addData("MOTOR", "${it.key}: ${(it.value as DcMotor).power}")
		}
		telemetry.addData("MAX SPEED", maxSpeed)
		telemetry.addData("LIFT SPEED", get<Lift>().motor.power)
		telemetry.addData("FISHING ROD SPEED", get<Output>().motor.power)
		telemetry.addData("LEFT INTAKE SPEED", get<Intake>().leftMotor.power)
		telemetry.addData("RIGHT INTAKE SPEED", get<Intake>().rightMotor.power)
		telemetry.addData("OUTPUT SERVO", get<Output>().servo.position)
	}
}