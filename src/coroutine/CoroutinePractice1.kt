package coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import java.io.Closeable

data class Ball(var hits: Int)

fun main(args: Array<String>) = runBlocking<Unit>{

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




/** a1
    val job = launch(Dispatchers.Default){
        for ( i in 1..10 ){
            yield()//코루틴의 취소요청이 있는지 확인하는 코드 (job.cancel~ 을 통해 취소요청을 받은거임)
            println("I'm sleeping $i ...")
            Thread.sleep(500L)
        }
    }

    delay(1300L)
    println("main : I'm tired of waiting!")
    job.cancelAndJoin()
    println("main : Now I can quit")
**/


    /** a2
    val job = launch(Dispatchers.Default) {

        for (i in 1..10) {
            if (!isActive) { //coroutineScope에 정의된 isActive 속성을 통해 코루틴이 비활성 상태인 경우 작업종료
                break
            }
            println("I'm sleeping $i ...")
            Thread.sleep(500L)
        }

    }
        delay(1300L)
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 유추상.. 이 job.cancel~ 로인해 job 부분의 코루틴이 작동을 멈추게되어서 !isActive된듯
        println("main: Now I can quit")
**/

    /** a3 (a1, a2와 결과가 같음)
    val job = launch{
        try{
            repeat(1000){
                i -> println("I'm sleeping $i ...")
                delay(500L)
            }
        } catch(e: Exception){
            println(e.printStackTrace())
        }
        finally{
            println("main : I'm running finally!")
        }

    }

    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main : Now I can quit.")
    **/


    /** a4
    val job = launch{
        SleepingBed().use{

            it.sleep(1000)
        }
    }

    delay(1300L)
    println("main : I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit.")


}

//suspend 함수는 코루틴 처리블록인 launch나 async에서 호출될 수 있고,
//또 다른 suspend 함수에서 호출될 수 있다.
class SleepingBed : Closeable {
    suspend fun sleep(times: Int){
        repeat(times){
            i -> println("I'm sleeping $i ...")
            delay(500)
        }
    }

    override fun close(){
        println("main : I'm running close() SleepingBed!")
    }

    **/

    /** a5
    val job = launch{
        try{
            repeat(1000){ i ->
                println("I'm sleeping $i ... ")
                delay(500L)
            }
        } finally {
            println("main: I'm running finally")
        }
    }

    launch{
        delay(1300L)
        println("main: I'm tired of waiting. Cancel the job!")
        if(job.isActive){
            job.cancelAndJoin()
        }
    }
    **/

    /** a6 기본적으로 a5와 같은방향의 코드다.
     * 다만 a5는 job객체 참조를 유지하면서 별도의 코루틴에서 처리하고 있는 번거로움이 있다.
     * 이를 해결하기 위해 코루틴 프레임워크는 withTimeout을 통해 이러한 문제를 해결할수 있다.
     *
     * a6 코드를 실행하면 나오는 TimeoutCancellationException는 예제가 메인함수에서 바로 실행되었기 때문이다.
     * 코루틴이 취소될때 발생하는 TimeoutCancellationException 예외는 코루틴에서는 일반적인 종료상황중 하나다.
    withTimeout(1300L){
        launch{
            try{
                repeat(1000){
                    i -> println("I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                println("main: I'm running finally!")
            }
        }
    }
    **/


    /** a7
    val channel = Channel<Int>()
    launch{
        for(x in 1..5) channel.send(x * x)
        channel.close() // 채널을 닫는요청을 큐의 마지막에 넣음으로써 모든 데이터가 잘 전달될것임을 보장한다.
    }

    repeat(5) { println(channel.receive()) }
    println("Done!")
    **/


    /** a8(채널 프로듀스) 코루틴이 어떤 데이터스트림(연속된 값들의 흐름)을 생성해내는 일은 꽤나 흔한일인데, 코루틴에서는
     * 이러한 생성작업을 용이하게 하기 위해서 produce{}라는 코루틴 빌더와 이렇게 생성된 값을 수신하기 위한
     * consumeEach()라는 확장함수를 제공한다. (for대체)
     *
     * cf. Channel 객체를 만들던가, 함수의 리턴타입을 ReceiveChannel로 하여 변수객체를 하나 만들면,
     * consumeEach 등을 통해 it으로 채널로 보내진 값(send로)을 출력할 수 있다.
     *
    val squares = produceSquares(5)
    squares.consumeEach{println(it)} // consumeEach
    println("Done")

    fun CoroutineScope.produceSquares(max: Int): ReceiveChannel<Int> = produce {
    for(x in 1..max){
    send(x*x)
    }
}   **/


    /**
     a9 파이프라인이란 하나의 코루틴이 데이터 스트림(무한 스트림 포함)을 생산해내고 다른 하나 이상의 코루틴들이 이 스트림을
      수신받아 필요한 작업을 수행한 후 가공된 결과를 다시 전달하는 패턴을 말한다.--

      *
      * 아래 예를들면 ReceiveChannel 타입을 리턴하는 produceNumbers는 produce를 통해 일련의 정수를 생성함
      * 그리고, 이 생성된 일련의 정수가 저장된 Channel타입인 numbers를 produceDouble이라는 또하나의 코루틴에
      * 매개변수로 전달됨으로써 파이프라인이 형성된 것.
      *

    val numbers = produceNumbers(5)
    val doubleNumbers = produceDouble(numbers)
    doubleNumbers.consumeEach { println(it) }
    println("Done")


     fun CoroutineScope.produceNumbers(max: Int): ReceiveChannel<Int> = produce{
     for(x in 1..max){
     send(x)
     }
     }

     fun CoroutineScope.produceDouble(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce{
     numbers.consumeEach { send(it*it) }
     }
     **/

    /** a10 소수
    var cur = numbersFrom(2)
    for(i in 1..10){
        val prime = cur.receive()
        println(prime)
        cur = filter(cur, prime)
    }
    coroutineContext.cancelChildren()
    println("Done")

    fun CoroutineScope.numbersFrom(start: Int) = produce<Int> {
        var x = start
        while(true) send(x++)
    }

    fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int) = produce<Int> {
        for(x in numbers) {
            if(x % prime != 0) send(x)
        }
    }
    **/

    /** a11 fan-out 하나의 채널로부터 두개 이상의 수신 코루틴들이 데이터를 분배하여 수신받을 수 있다.

    val producer = produceNumbers()
    repeat(5){

        launchProcessor(it, producer)

    }
    delay(950L)
    producer.cancel()

    fun CoroutineScope.produceNumbers() = produce<Int>{
        var x = 1
        while(true){
            send(x++)
            delay(100L)
        }
    }

    fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch{
        for (msg in channel){
            println("Processor #$id receive $msg")
        }
    }
    **/

    /** a12 fan-out example2
    val producer = produceNumbers()
    repeat(5) {
        val job = launchProcessor(it, producer)
        if(it == 3){
            delay(200)
            job.cancel()
        }
    }
    delay(950L)
    producer.cancel()

    fun CoroutineScope.produceNumbers() = produce<Int> {
        var x = 1
        while(true) {
            send(x++)
            delay(100L)
        }
    }

    fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
        channel.consumeEach {
            println("Processor #$id received $it")
        }
    }
    **/

    /** a13 fan-in
    val channel = Channel<String>()
    launch{sendString(channel, "Foo", 200L)}
    launch{ sendString(channel, "Bar", 500L)}
    repeat(6){
        println(channel.receive())
    }
    coroutineContext.cancelChildren()


    suspend fun sendString(channel: SendChannel<String>, text: String, time: Long){
        while(true){
            delay(time)
            channel.send(text)
        }
    }

     **/

    /** a14 Buffered Channel

    val channel = Channel<Int>(4)

    val sender = launch{
        repeat(10){
            print("Try to send $it : ")
            channel.send(it)
            print("Down\n")
        }
    }

    delay(1000L)
    sender.cancel()
     **/


    /** a15 channels are fair (채널의 공정성 예제)
     *
     * 2개 이상의 코루틴들이 하나의 채널로 송/수신을 수행한다면 실행 순서는 그 호출순서에따라 공정하게 할당되고
     * FIFO 방식으로 스케쥴링 됨
     *
     * 다시 말해, 처음 receive()를 호출한 코루틴이 데이터를 먼저 수신함
     *
     * TODO: 채널객체를 통해 .recevice()로 채널에 있는 데이터를 받던, consumeEach나 기타 반복문을 통해 채널로부터 데이터를 받아 출력시키면 안에 데이터가 소멸되는 듯!
     *
    val table = Channel<Ball>()

    launch{ player("ping", table)}
    launch{ player("pong", table)}

    table.send(Ball(0))
    delay(1000L)
    coroutineContext.cancelChildren()


    suspend fun player(name: String, table: Channel<Ball>){
    for (ball in table){
    ball.hits++
    println("$name $ball")
    delay(300L)
    table.send(ball)
    }
    }
    **/

    /** a16 **/

    val tickerChannel = ticker(
            delayMillis = 100,
            initialDelayMillis = 0
    )

    var nextElement = withTimeoutOrNull(1){tickerChannel.receive()}
    println("Initial element is available immediately")

    nextElement = withTimeoutOrNull(50){ tickerChannel.receive()}
    println("Next element is not ready in 50ms: $nextElement")

    nextElement = withTimeoutOrNull(60){ tickerChannel.receive()}
    println("Next element is not ready in 100ms: $nextElement")






}









