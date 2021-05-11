package chap02.section2


fun main() {


    var list: ArrayList<Int> = arrayListOf(1, 3 ,4)

    //filter는 내용들중 일부를 추출하겠다는 의미고, it은 그안에 내용들을 지칭하는 것
    var result = list.filter{it>1}

    for(i in result){
        println(i)
    }

    //위 3라인의 코드를 다음과 같이 축약할 수 있음(forEach 함수를 사용한 것임)
    //it의 값이 2개가 있다면 forEach 함수가 두번실행되면서 it내용을 출력함
    list.filter{it>1}.forEach{print("$it ")}
    println()

    //forEachIndexed는 index와 value를 매개변수로 인덱스와 해당 인덱스의 값을 리턴해준다
    //forEach처럼 it이 2개를 갖고 있으면 2번 실행되면서 값을 리턴해준다
    list.filter{it>1}.forEachIndexed{idx, value -> println("index: $idx, value: $value")}




    val obj:Super = Sub()
    obj.sayHello()

    val obj2:Sub = obj as Sub
    obj2.sayHello()


}

//kotlin은 기본적으로 final이 붙어있다. final이 붙은것은 상속이 불가하므로
//kotlin에서 상속을 허용하고싶으면 open을 붙여야한다(오버라이딩을 허용하려는 프로퍼티나 메소드앞에도 open붙여야함)
open class Super {
    open fun sayHello(){
        println("i m super")
    }
}

class Sub:Super(){
    override fun sayHello(){
        println("i m Sub")
    }
}