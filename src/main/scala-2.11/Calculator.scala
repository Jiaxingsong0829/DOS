/**
 * Created by win8 on 2015/9/12.
 */
object Calculator {

   var counter = 0
   var count10 = 0
   var count9 = 0
   var count8 = 0
   var count7 = 0
   var count6 = 0
   var count5 = 0
   var count4 = 0
   var count3 = 0
   var count2 = 0
   var count1 = 0

   var long = ""

   def count(string : String): Unit = {
      counter += 1;
      val hashValue = string.split("\t\t")(1)

      if (hashValue.startsWith("0000000000")) {count10 += 1
         long += string+","
      }
      else if (hashValue.startsWith("000000000")) {count9 += 1
         long += string+","
      }
      else if (hashValue.startsWith("00000000")) {count8 += 1
         long += string+","
      }
      else if (hashValue.startsWith("0000000")) {count7 += 1
         long += string+","
      }
      else if (hashValue.startsWith("000000")) {count6 += 1}
      else if (hashValue.startsWith("00000")) {count5 += 1}
      else if (hashValue.startsWith("0000")) {count4 += 1}
      else if (hashValue.startsWith("000")) {count3 += 1}
      else if (hashValue.startsWith("00")) {count2 += 1}
      else if (hashValue.startsWith("0")) {count1 += 1}


      if (long.length > 0)
         println(long)
      print(count10+" ")
      print(count9+" ")
      print(count8+" ")
      print(count7+" ")
      print(count6+" ")
      print(count5+" ")
      print(count4+" ")
      print(count3+" ")
      print(count2+" ")
      println(count1)
   }
}
