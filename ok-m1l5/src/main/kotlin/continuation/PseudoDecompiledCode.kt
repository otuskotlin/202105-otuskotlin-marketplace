package continuation

//fun coroutine(continuation: Continuation<Any?>) {
//    val cont = continuation as? ThisCont ?: object: ThisCont {
//        fun resume(...) {
//            coroutine(this)
//        }
//    }
//    when(cont.label){
//        0 -> {
//            cont.label = 1
//            getText(cont)
//        }
//        1 -> {
//            val result = cont.result as String
//            printText(result)
//        }
//    }
//}