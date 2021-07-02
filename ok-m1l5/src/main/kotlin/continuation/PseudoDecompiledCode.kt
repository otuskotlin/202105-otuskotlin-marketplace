package continuation

//fun coroutine(continuation: Continuation<Any?>) {
//    when(continuation.label){
//        0 -> {
//            println("Coroutine starts")
//            delay(1000L)
//        }
//        1 -> {
//            println("Coroutine middle stage")
//            delay(2000L)
//        }
//        2 -> {
//            println("Coroutine has ended")
//            continuation.resume(Unit)
//        }
//    }
//}