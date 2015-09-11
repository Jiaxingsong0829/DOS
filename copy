
 
import akka.actor._
import akka.routing.RoundRobinPool
import scala.concurrent.duration._
import scala.collection.mutable.Queue
 
object Bitcoin  {

  def main(args: Array[String]): Unit = {
    calculate(nrOfWorkers = 6, start = 33, end = 126)
  }
  
 
  
  sealed trait BitcoinMessage
  case object Calculate extends BitcoinMessage
  case class Work(i: Int, start: Int, end: Int, dur: Int, nrOfWorkers: Int) extends BitcoinMessage
  case class Result(value: String) extends BitcoinMessage
  case class showBitcoin(value: String, duration: Duration)
 
  	class Worker extends Actor {
 
    	def getBitcoinFromI(i: Int, start: Int, end: Int, dur: Int, nrOfWorkers: Int): String = {
        val string = "adobra"
        if (i == nrOfWorkers){
          return bfs(string, start + dur * (i - 1), end)
        } 
        else
          println("The" + i + "actor")
          return bfs(string, startIndex = start + dur * (i - 1), endIndex = start + dur * (i - 1) + dur)
  		}

  		def bfs(string: String, startIndex: Int, endIndex: Int): String = {
    
    		var queue = Queue[String]()
        for(num <- startIndex to endIndex){
          println(string + num.asInstanceOf[Char])
          queue += (string + num.asInstanceOf[Char])
        }
    		while (!queue.isEmpty) {
      			val candidate = queue.dequeue
      			if (MD5(candidate.toString()).startsWith("00000")) {
        		return candidate.toString()
      			}
      			for (char <- 33 to 126) {
        			queue += (candidate + char.asInstanceOf[Char])
      			}
    		}
    		return string
  		}

  		def MD5(s: String): String = {
    
    		// Besides "MD5", "SHA-256", and other hashes are available
    		val m = java.security.MessageDigest.getInstance("SHA-256").digest(s.getBytes("UTF-8"))
    		m.map("%02x".format(_)).mkString
  		}
  		def receive = {
      		case Work(i, start, end, dur, nrOfWorkers) ⇒
        	sender ! Result(getBitcoinFromI(i, start, end, dur, nrOfWorkers)) // perform the work
    	}
    }
 
  	class Master(nrOfWorkers: Int, start: Int, end: Int, listener: ActorRef) extends Actor {
 		
 		val startTime: Long = System.currentTimeMillis

    	val workerRouter = context.actorOf(
      	Props[Worker].withRouter(RoundRobinPool(nrOfWorkers)), name = "workerRouter")
 
    	def receive = {
      		case Calculate ⇒
        	for (i ← 1 until nrOfWorkers) 
          { 
            val dur = (end - start) / nrOfWorkers;
            workerRouter ! Work(i, start, end, dur, nrOfWorkers)
          }
      		case Result(value) ⇒
        		
          		listener ! showBitcoin(value, duration = (System.currentTimeMillis - startTime).millis)
          		// Stops this actor and all its supervised children
              context.stop(self)
              println("stop itself")
        }
    }
 
  	class Listener extends Actor {
    	def receive = {
      		case showBitcoin(value, duration) ⇒
        	println("\n\tThe answer is: \t\t%s\n\tCalculation time: \t%s".format(value, duration))
        	context.system.shutdown()
          println("shutdown")
    	}
  	}
 
 
  	def calculate(nrOfWorkers: Int, start: Int, end: Int) {
    	// Create an Akka system
      val system = ActorSystem("BitcoinSystem")
 
    	// create the result listener, which will print the result and shutdown the system
    	val listener = system.actorOf(Props[Listener], name = "listener")
 
    	// create the master
    	val master = system.actorOf(Props(new Master(
      	nrOfWorkers, start, end, listener)),
      	name = "master")
 
    	// start the calculation
    	master ! Calculate
  	}
}