import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.RoundRobinPool

import scala.collection.mutable.Queue
import scala.concurrent.duration._
 
object Project1 {

  def main(args: Array[String]): Unit = {
    calculate(nrOfWorkers = 8, start = 33, end = 126)

  }

  sealed trait BitcoinMessage
  case object Calculate extends BitcoinMessage
  case class Work(i: Int, start: Int, end: Int, dur: Int, nrOfWorkers: Int) extends BitcoinMessage
  case class Result(value: String) extends BitcoinMessage
  case class showBitcoin(value: String, duration: Duration)

	// the special prefix
	val string = "zhihuang"
	class Worker extends Actor {

   	def getBitcoinFromI(i: Int, start: Int, end: Int, dur: Int, nrOfWorkers: Int): String = {
		  	if (i == nrOfWorkers){
				bfs(string, start + dur * (i - 1), end)
		  	}
      	else {
			 	bfs(string, startIndex = start + dur * (i - 1), endIndex = start + dur * (i - 1) + dur)
			}
		}

		def bfs(string: String, startIndex: Int, endIndex: Int): String = {
		  	var queue = Queue[String]()
			for(num <- startIndex to endIndex){
				queue += (string + num.asInstanceOf[Char])
			}
			while (queue.nonEmpty) {
				val candidate = queue.dequeue
				if (MD5(candidate).startsWith("0000")) {
					return candidate
				} else {
					for (char <- 33 to 126) {
						queue += (candidate + char.asInstanceOf[Char])
					}
				}
				// too deep
				if (candidate.length() > string.length() + 5) {
					queue.clear();
				}
			}
			string
		}

		def receive = {
			case Work(i, start, end, dur, nrOfWorkers) ⇒
			sender ! Result(getBitcoinFromI(i, start, end, dur, nrOfWorkers)) // perform the work
		}
	}

	def MD5(s: String): String = {
		val m = java.security.MessageDigest.getInstance("SHA-256").digest(s.getBytes("UTF-8"))
		m.map("%02x".format(_)).mkString
	}

	class Master(nrOfWorkers: Int, start: Int, end: Int) extends Actor {
		val startTime: Long = System.currentTimeMillis
		val workerRouter = context.actorOf(Props[Worker].withRouter(RoundRobinPool(nrOfWorkers)), name = "workerRouter")

		def receive = {
			case Calculate ⇒
				for (i ← 1 until nrOfWorkers)	{
					val dur = (end - start) / nrOfWorkers
					workerRouter ! Work(i, start, end, dur, nrOfWorkers)
				}
			case Result(value) ⇒
				val duration = (System.currentTimeMillis - startTime).millis
				// Stops this actor and all its supervised children
				println("%s\t\t%s".format(value, MD5(value)))
				println(duration)
				context.stop(self)
				context.system.shutdown()
		}
	}

	def calculate(nrOfWorkers: Int, start: Int, end: Int) {
		// Create an Akka system
		val system = ActorSystem("BitcoinSystem")

		// create the master
		val master = system.actorOf(Props(new Master(nrOfWorkers, start, end)),	name = "master")

		// start the calculation
		master ! Calculate
	}
}