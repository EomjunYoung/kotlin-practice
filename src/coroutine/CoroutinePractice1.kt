package coroutine

import kotlinx.coroutines.*

fun main(args: Array<String>) = runBlocking{

/**
 *
 *    GlobalScope.launch{}블록은 코루틴을 생성하기 위한 코루틴 빌더다.
 *    코루틴은 호출 쓰레드(모든쓰레드)와 독립적이기 때문에, 현 상태에서
 *    호출 쓰레드의 실행상태를 늘려주지 않으면 동작하지 않는다.
 *
 *    그래서 스레드를 멈추게하는 역할을 수행하는 함수가 있는데 이것이 중단함수(Blocking function)이다.
 *
 *    runBlocking{}은 주어진 블록이 완료될때까지 현재의 스레드를 멈추는 새로운 코루틴을 생성하여
 *    실행하는 코루틴 빌더다..
 *
 *    코루틴안에서는 runBlocking{}의 사용이 권장되지 않으며, 일반적인 함수코드블록에서 중단함수를 호출할 수 있도록
 *    존재하는 장치이다.(참고로 delay는 중단함수이며, 모든 중단함수들은 코루틴안에서만 호출될 수 있다는 제약이있다.
 *
 *
GlobalScope.launch{

delay(1000L)
println("World")
}
println("Hello")
runBlocking { delay(2000L) }
 *
 *
 **/


    /**
     * 위처럼 delay(2000L)을 현재 실행된 코루틴의 수행이 완료될때까지 임의로 2초 늘렸는데,
     * 이와같은 방식은 적절치못하다.(왜냐면 프로그램상 부모 코루틴이 자식 코루틴의 동작이 멈추는데까지 얼마나
     * 걸리는지 알 수 없으므로)
     *
     * 그래서 runBlocking{}빌더로 생성된 코루틴 블록은 GlobalScope.launch{}빌더를 이용해 생성한
     * 코루틴이 종료될때까지 대기한 후 종료하는 방식을 이용하며 아래처럼 코딩할 수 있다.
     * 이때 GlobalScope.launch{}의 결과로 Job인스턴스가 반환된다.
     *
     *
     *  fun main(args: Array<String>) = runBlocking
     *
     *
     *
    var job = GlobalScope.launch{

    delay(1000L)
    println("World")
    }
    println("Hello, ")
    job.join()
     */


    /**
     *
     * runBlocking{}과 달리 coroutineScope{}는 자식들의 종료를 기다리는 동안 현재 스레드를 블록하지 않는다.
     *

    launch{
//        delay(10L)
        println("Task from runBlocking")
    }

    runBlocking {
        launch{
            delay(150L)
            println("Task from nested launch")
        }
        delay(50L)
        println("Task from coroutine scope")
    }

    println("Coroutine scope is over")

     */


    val job = launch(Dispatchers.Default){
        for ( i in 1..10 ){
            println("I'm sleeping $i ...")
            Thread.sleep(5000L)
        }
    }

    delay(1300L)
    println("main : I'm tired of waiting!")
    job.cancelAndJoin()
    println("main : Now I can quit")



}