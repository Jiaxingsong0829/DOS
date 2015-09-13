import java.net.InetAddress

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.RoundRobinPool
import com.typesafe.config.ConfigFactory

import scala.collection.mutable.ArrayBuffer

object Project1 {

  	def main(args: Array[String]): Unit = {
	  	var input = args(0)
//		input = "4"
//	  	input = "192.168.2.10"

      input = "1"
      input = "192.168.93.1"

	  	// if it is a server
	  	if (!input.contains(".")) {
			val targetPrefixBuilder = new StringBuilder
			for (i <- 1 to input.toInt) {
				targetPrefixBuilder.append('0')
			}
			calculate(nrOfWorkers = 2, targetPrefixBuilder.toString() : String)
		}
		//else it is a client and could join the server
	  	else {
			serverIP = input
			val system = ActorSystem("clientSystem", ConfigFactory.parseString("""
    		akka {
				 actor {
					  provider = "akka.remote.RemoteActorRefProvider"
						 }
        	}"""))

			// create the master for client
			val clientMaster =
				system.actorOf(Props(new ClientMaster(nrOfWorkers = 2)), name = "clientMaster")
			clientMaster ! "connect"
		}
 	}

  	sealed trait BitcoinMessage
  	case object Calculate extends BitcoinMessage
	case class Work(startSuffix : String, workload : Int, targetPrefix : String) extends BitcoinMessage
  	case class Result(value: String) extends BitcoinMessage
	case class ClientResult(value : ArrayBuffer[String]) extends BitcoinMessage

	// the special prefix
	val prefix = "zhihuang"
	// by default
	var serverIP = "127.0.0.1"

	class ServerWorker extends Actor {
		def receive = {
			case Work(startSuffix, workLoad, targetPrefix) ⇒
				sender ! Result(serverSearch(startSuffix, workLoad, targetPrefix))
		}
	}

	class ClientWorker extends Actor {
		def receive = {
			case Work(startSuffix, workLoad, targetPrefix) ⇒
				sender ! ClientResult(clientSearch(startSuffix, workLoad, targetPrefix))
		}
	}

	class ServerMaster (nrOfWorkers: Int, targetPrefix: String) extends Actor {
		// The work unit for a single actor
		val workUnit = 10000
		// The work unit for a single remote client
		val workLoad = workUnit * 32
		// Total mount of work
		var remainingWorkLoad = BigInt(10000*10000)

		val workerRouter =
			context.actorOf(Props[ServerWorker].withRouter(RoundRobinPool(nrOfWorkers)), name = "workerRouter")
		var startSuffix = ""

		def stopSystem() : Unit = {
			context.stop(self)
			context.system.shutdown()
		}

		def receive = {
			case Calculate ⇒
				for (i ← 0 until nrOfWorkers)	{
					workerRouter ! Work(startSuffix, workUnit, targetPrefix)
					startSuffix = getNext(startSuffix, workUnit)
				}
			// Get the result from server actor
			case Result(value) =>
				if (remainingWorkLoad > 0L) {
					remainingWorkLoad -= workUnit
					sender ! Work(startSuffix, workUnit, targetPrefix)
					startSuffix = getNext(startSuffix, workUnit)
				} else {
					stopSystem
				}
			// Get the result from client actors
			case ClientResult(value) =>
				for (string : String <- value) {
					println(string + "\tremote client: "+sender.path)
               Calculator.count(string)
				}
				if (remainingWorkLoad > 0) {
					remainingWorkLoad -= workLoad
					sender ! Work(startSuffix, workLoad, targetPrefix)
					startSuffix = getNext(startSuffix, workLoad)
				} else {
					sender ! "STOP"
					stopSystem
				}
			// The client join the group and notify the server
			case "JOIN" =>
				sender ! Work(startSuffix, workLoad, targetPrefix)
				startSuffix = getNext(startSuffix, workLoad)
		}
	}

	class ClientMaster (nrOfWorkers: Int) extends Actor{
		val workUnit = 10000
		val workerRouter =
			context.actorOf(Props[ClientWorker].withRouter(RoundRobinPool(nrOfWorkers)), name ="workerRouter")
		var serverMaster = context.actorSelection("akka.tcp://BitcoinSystem@%s:9999/user/master".format(serverIP))
		var startSuffix = ""
		var targetPrefix = ""
		var remainingWorkLoad = 10000
		var clientResult = new ArrayBuffer[String]()

		def connectServer() : Unit = {
			serverMaster ! "JOIN"
		}

		def stopSystem() : Unit = {
			context.stop(self)
			context.system.shutdown()
		}

		def receive = {
			case "connect" =>
				connectServer()
			case Work(start, workLoad, prefix) =>
				clientResult = new ArrayBuffer[String]()
				startSuffix = start
				remainingWorkLoad = workLoad
				targetPrefix = prefix
				for (i ← 0 until nrOfWorkers)	{
					workerRouter ! Work(startSuffix, workUnit, targetPrefix)
					remainingWorkLoad -= workUnit
					startSuffix = getNext(startSuffix, workUnit)
				}
			case ClientResult(value) =>
				clientResult ++= value
				if (remainingWorkLoad > 0) {
					sender !  Work(startSuffix, workUnit, targetPrefix)
					remainingWorkLoad -= workUnit
               startSuffix = getNext(startSuffix, workUnit)
				} else {
					serverMaster ! new ClientResult(clientResult)
				}
			case "STOP" =>
				stopSystem
		}
	}

	def calculate(nrOfWorkers: Int, targetPrefix : String) {
		// start remoting configuration
		val localIpAddress = InetAddress.getLocalHost().getHostAddress()
		val system = ActorSystem("BitcoinSystem", ConfigFactory.parseString("""
    		akka {
				 actor {
					  provider = "akka.remote.RemoteActorRefProvider"
             }
				 remote {
					  transport = ["akka.remote.netty.tcp"]
				 netty.tcp {
					  hostname = "%s"
					  port = 9999
             }
            }
        	}""".format(localIpAddress)))

		// create the master
		val master =
			system.actorOf(Props(new ServerMaster(nrOfWorkers, targetPrefix)), name = "master")

		// start the calculation
		master ! Calculate
	}

	/**
	 * The server directly print out the result and notify the master when finish.
	 * */
	def serverSearch(startSuffix : String, workLoad : Int, targetPrefix : String): String = {
		var suffix = startSuffix
		for (i <- 0 until workLoad) {
			val hashValue = MD5(prefix + suffix)
			if (hashValue.startsWith(targetPrefix)) {
				println(prefix + suffix + "\t\t" + hashValue)
            Calculator.count(prefix + suffix + "\t\t" + hashValue);
			}
			suffix = getNext(suffix, 1)
		}
		"DONE"
	}

	/**
	 * The client result a collection of results to minize the cost on communication.
	 * */
	def clientSearch(startSuffix : String, workLoad : Int, targetPrefix : String):
	ArrayBuffer[String] = {
		var suffix = startSuffix
		var result = new ArrayBuffer[String]()
		for (i <- 0 until workLoad) {
			val hashValue = MD5(prefix + suffix)
			if (hashValue.startsWith(targetPrefix)) {
				result += (prefix + suffix+"\t\t"+hashValue)
            println((prefix + suffix+"\t\t"+hashValue))
			}
			suffix = getNext(suffix, 1)
		}
		result
	}

	def MD5(s: String): String = {
		val m = java.security.MessageDigest.getInstance("SHA-256").digest(s.getBytes("UTF-8"))
		m.map("%02x".format(_)).mkString
	}

	/**
	 * Get next n-th string
	 * */
	def getNext(k : String, n : Int) : String = {
		var key = k
		if (key == null || key.isEmpty) { key = "!"}
		val builder = new StringBuilder(key)

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