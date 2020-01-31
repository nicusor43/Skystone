package org.firstinspires.ftc.teamcode.robot.mecanumv2

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.Hook
import org.firstinspires.ftc.teamcode.Mecanum
import org.firstinspires.ftc.teamcode.Robot
import org.firstinspires.ftc.teamcode.Touch
import org.firstinspires.ftc.teamcode.modules.mecanumV2_module.TouchSensorModule
import org.firstinspires.ftc.teamcode.opmode.BBLinearOpMode
import org.firstinspires.ftc.teamcode.opmode.get
import org.firstinspires.ftc.teamcode.utils.waitForStartFixed

@Autonomous(name="AUTO TRAY RIGHT", group="SKYSTONE")
class AutoTrayRightV2 : BBLinearOpMode() {
    override val robot: Robot = Robot(this, setOf(Mecanum(this), Touch(this), Hook(this)))
    override fun runOpMode() {
        robot.modules.forEach { it.init() }


        waitForStartFixed()
        //Robotul incepe cu fata lipita de perete, cu partea stanga a robotului langa a doua linie
        get<Mecanum>().sideways(-18.0, 0.9, 2.0)
        get<Mecanum>().forwardUntil(-0.9) { get<Touch>().isPressed() }
        get<Hook>().grab()
        get<Mecanum>().rotate(90.0, 0.9, 3.0)
        get<Mecanum>().sideways(-30.0, 0.9, 3.0)
        get<Hook>().ungrab()
        get<Mecanum>().forward(40.0, 0.9, 3.0)
    }
}