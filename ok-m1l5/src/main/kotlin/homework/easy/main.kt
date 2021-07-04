package homework.easy

fun main() {
    val numbers = generateNumbers()
    val toFind = 10
    val toFindOther = 1000

    val foundNumbers = listOf(
        findNumberInList(toFind, numbers),
        findNumberInList(toFindOther, numbers)
    )

     foundNumbers.forEach {
         if (it != -1) {
             println("Your number found!")
         } else {
             println("Not found number $toFind")
         }
     }
}