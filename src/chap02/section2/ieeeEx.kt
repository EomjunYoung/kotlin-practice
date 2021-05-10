package chap02.section2

import java.awt.Color

fun main() {


    var num: Double = 0.1
    var arr: List<String> = listOf("Eom", "Jun", "young")
    var arr2: ArrayList<String> = arrayListOf("Eom", "Jun", "young")
    var arr3 = IntArray(5, {1})

    for(x in 0..999){
        num += 0.1
    }
    println("num ${num}")

    println("${Int.MAX_VALUE}")
    
    print("Name is: ")
    for( str in arr3){
        print("$str")

    }


    var str1 = "Hello"
    var str2 = "World"
    var str3 = "Hello"

    //?을 넣음으로써 null허용변수로 바꿀수있음.. 빼면 초기화 안되었다고 컴파일에러 발생
    var str: String? = null

   println("str1 === str2 ${str1 === str2}" )
   println("str1 === str3 ${str1 === str3}" )

    println("str: $str, length: ${str?.length}")
    //C#은 $"{str.length}/v4..." 처럼 변수로 값을 넣고싶을땐 이렇게 $"" 진행하고 그안에 {}를 통해 변수를 넣으면됨.
    //더불어 일반적인 string은 "" 안에만 자유롭게 넣으면 됨...
    //코트린은 ""안에 값을 넣고싶을때 ${}형태를 넣는것과 비슷하면서도 다름


    read("엄", 1)
    println(plus(2, 1))
    println(getMnemonic(Color.PINK))


    //step은 양수만가능하며 순증가를 어디부터 어디까지를 표기하기위해서
    //1..100이나 1 until 100 식의 형태를 써줘야 한다.
    for( i in 1..100 step 2){

        print("$i ")
    }

    //downTo는 어느숫자까지 감소할건지를 묻는것..
    //100부터 -2씩 줄인다는 의미이다
    for(i in 100 downTo 1 step 2){

        print("$i ")
    }

    //이 라인의 핵심은 생성자의 parameter명(name)과 클래스내의 property명이(name)
    //같을경우 어떻게 값이 할당되는지를 보려는 것이다.
    //생성자 name에 kim을 넣을경우 생성자(init)에서는 kim이 할당되고
    //아래 sayHello를 통해 나오는 name은 class property로 할당된 eom이란 값이 나온다.
    //cf. eom1클래스내의 name이 val로 선언되었기때문에 class property가 된 것임.
    //함수 인자값에 val이나 var로 선언하면 class property로 인식된다.. 고로 생성자 초기화시 컴파일 에러가 발생함
    var eom = eom1("kim", "seoul")
    eom.sayHello()


    //obj7을 상위클래스로 캐스팅하고 다시 sub1로 하위캐스팅하는 케이스인데,
    //문제는 Super?이므로 null값을 받는경우임..
    //obj7가 null일때 캐스팅시도하면 런타임에러가 발생하는데 이를 방지하려면
    //as를 as?로 바꿔준다. 이렇게되면 obj7이 null이면 캐스팅시도시 그냥 null을 반환해줌
//    val obj7: Super? = null
//    val obj8: Sub1 = obj7 as Sub1 //런타임 에러


    //sum1은 람다함수로 표현한 것(자세한 방법은 코틀린 학습내용 메모장 20210405참고
    var sum1 = {x1: Int, x2: Int -> x1+x2}
    var result1 = sum1(10, 20)
    //sum은 일반으로 합을 리턴하는 함수 정의
    var result2 = sum(10, 25)

    println(result1)
    println(result2)

    run{println("람다함수 바로 실행")}//매개변수에 넣지않고 바로실행할때 run메소드를 사용


    hoFun(10, {x->x*x})
    hoFun2(20, {x,y -> x*y})

}

//20210405 메모장 학습내용 확인
fun hoFun(x1: Int, argFun: (Int) -> Int){
    val result = argFun(10)
    println("x1: $x1, someFun1: $result")
}

fun hoFun2(x1: Int, argFun: (Int, Int) -> Int){
    val result = argFun(10, 20)
    println("x1: $x1, someFun1: $result")
}




fun read(param1: String, param2: Int){
    println("${param1} + $param2")
}

fun plus(param1: Int, param2: Int): Int{
    return param1 + param2
}

fun plus2(param1: Int, param2: Int): Int{
    return if (param1 + param2 >=5) 6 else 1
}

fun getMnemonic(color: Color) =
        when(color){

            Color.RED, Color.PINK -> "R"
            Color.ORANGE ->"O"
            else -> "ㅗ"

        }


//람다식 예제
fun sum(x1: Int, x2: Int): Int{
    return x1 + x2
}

//클래스마다 주생성자는 하나이며.. 보조생성자는 여러개만들수있다. 보조생성자가 있으면 주생성자를 생략할 수 있다.
class eom1 constructor(name: String, address: String) {
    //property는 선언시 반드시 초기화를 해줘야하는데, "eom"의 값을 초기화 하지않으면,
    //초기화하라거나, 추상형으로 선언하라는 메시지가 뜸.
    //이때 코틀린은 함수뿐만아니라 추상형 프로퍼티도 제공함..
    // abstract val name: String을 하면 초기화를 안해도됨!
    val name: String = "eom"

    init{
        println("i am init... constructor: $name")

    }

    fun sayHello(){
        println("hello: $name")
    }

}