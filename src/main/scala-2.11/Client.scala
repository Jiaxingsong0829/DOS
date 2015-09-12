
import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Client{

   def main (args: Array[String]) {
      val system = ActorSystem("clientSystem", ConfigFactory.parseString("""
    		akka {
				 actor {
					  provider = "akka.remote.RemoteActorRefProvider"
						 }
        	}"""))

      // create the master
      val client =
         system.actorOf(Props[Connector], name = "client")

      client ! "connect"
   }

   class Connector extends Actor {
      def connectServer (): Unit = {
         val ar = context.actorSelection("akka.tcp://BitcoinSystem@localhost:2209/user/master")
         ar ! "JOIN"
      }

      def receive = {
         case "connect" => connectServer
         case _ => println("received message from the server")
      }
   }
}
