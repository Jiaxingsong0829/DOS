import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.RoundRobinPool

import scala.concurrent.duration._
 
object Project1 {

  def main(args: Array[String]): Unit = {

	  	val targetPrefixBuilder = new StringBuilder("000000")
//	  	for (i <- 1 to args(0).toInt) {
//			targetPrefixBuilder.append('0')
//		}
	  	calculate(nrOfWorkers = 8, targetPrefixBuilder.toString() : String)

  }

  sealed trait BitcoinMessage
  case object Calculate extends BitcoinMessage
  case class Work(startSuffix : String, workload : Int, targetPrefix : String) extends BitcoinMessage
  case class Result(value: String) extends BitcoinMessage

	// the special prefix
	val prefix = "zhihuang"
	class Worker extends Actor {

		def search(startSuffix : String, workLoad : Int, targetPrefix : String): String = {
			var suffix = startSuffix

			for (1 <- 0 to workLoad) {
				if (MD5(prefix + suffix).startsWith(targetPrefix)) {
					return prefix + suffix
				}
				suffix = getNext(suffix, 1)
			}
			"FAILED"
		}

		def receive = {
			case Work(startSuffix, workLoad, targetPrefix) ⇒
			sender ! Result(search(startSuffix, workLoad, targetPrefix)) // perform the work
		}
	}

	class Master(nrOfWorkers: Int, targetPrefix: String) extends Actor {
		val workLoad = 10000
		val startTime: Long = System.currentTimeMillis
		val workerRouter = context.actorOf(Props[Worker].withRouter(RoundRobinPool(nrOfWorkers)), name = "workerRouter")
		var startString = "";

		def receive = {
			case Calculate ⇒
				for (i ← 1 until nrOfWorkers)	{
					startString = getNext(startString, workLoad)
					workerRouter ! Work(startString, workLoad, targetPrefix)
				}
			case Result(value) ⇒
				val duration = (System.currentTimeMillis - startTime).millis
				// Stops this actor and all its supervised children
				if (value.equals("FAILED")) {
					startString = getNext(startString, workLoad)
					sender ! Work(startString, workLoad, targetPrefix);
				} else {
					println("%s\t\t%s".format(value, MD5(value)))
					println(duration)
					context.stop(self)
					context.system.shutdown()
				}
		}
	}

	def calculate(nrOfWorkers: Int, targetPrefix : String) {
		// Create an Akka system
		val system = ActorSystem("BitcoinSystem")

		// create the master
		val master =
			system.actorOf(Props(new Master(nrOfWorkers, targetPrefix)), name = "master")

		// start the calculation
		master ! Calculate
	}

	def MD5(s: String): String = {
		val m = java.security.MessageDigest.getInstance("SHA-256").digest(s.getBytes("UTF-8"))
		m.map("%02x".format(_)).mkString
	}

	/**
	 * Get next n-th string
	 * */
	def getNext(key : String, n : Int) : String = {
		val builder = new StringBuilder(key)
		if (builder.isEmpty) {
			builder.append(33.toChar)
			return builder.toString()
		}

		val charSetSize = 126 - 33 + 1
		val offSet = 33
		var reminder = 0
		var i = 0
		var sum = (key(i) - offSet) + n

		builder.setCharAt(i, (sum % charSetSize + offSet).asInstanceOf[Char])
		reminder = sum / charSetSize
		i += 1

		while (i < builder.length && reminder > 0) {
			sum = (key(i) - offSet) + reminder
			builder.setCharAt(i, (sum % charSetSize + offSet).asInstanceOf[Char])
			reminder = sum / charSetSize
			i += 1
		}

		while (reminder > 0) {
			builder.append((reminder % charSetSize + offSet).asInstanceOf[Char])
			reminder /= charSetSize
			i += 1
		}

		builder.toString()
	}
}