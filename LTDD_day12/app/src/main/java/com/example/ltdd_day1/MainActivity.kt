package com.example.ltdd_day1

import android.os.Bundle
import android.util.Log // Dùng Log để xem kết quả trong Android
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ltdd_day1.ui.theme.LTDD_day1Theme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay // Thêm để dùng cho suspend
import kotlin.math.PI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        InMauCoChuoi()
        runFunctions()

        enableEdgeToEdge()
        setContent {
            LTDD_day1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DiceWithButtonAndImage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
    private fun runFunctions() {
        Log.d("KOTLIN", "Kết quả tung xúc xắc: ${roll()}")
        ifelse()
        RollDice()
        list()
        loop()
        stringFun()
        sett()
        mapp()
        Lambda()
        quanti()
        launch()
    }
}

@Composable
fun DiceWithButtonAndImage(modifier: Modifier = Modifier) {
    var result by remember { mutableStateOf(1) }

    val imageResource = when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = result.toString()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { result = (1..6).random() }) {
            Text("Roll")
        }
    }
}

@Composable
fun printText() {
    println("This is the text to print!")
}

val binding: Any = "Hello Android"
val age = "5"
val name = "Rover"
var roll = 6
var rolledValue: Int = 4

fun InMauCoChuoi() {
    println("You are already ${age}!")
    println("You are already ${age} days old, ${name}!")
}

fun printHello () {
    println ("Hello Kotlin")
}

fun printBorder(border: String, timesToRepeat: Int) {
    repeat(timesToRepeat) { print(border) }
    println()
}

fun roll(): Int {
    return (1..6).random()
}

val diceRange = (1..6)
val randomNumber: Int = diceRange.random()

fun printBorder() {
    repeat(23) { print("=") }
}

fun printCakeBottom(age: Int, layers: Int) {
    repeat(layers) {
        repeat(age + 2) { print("@") }
        println()
    }
}

fun ifelse() {
    val num = 4
    if (num > 4) {
        println("The variable is greater than 4")
    } else if (num == 4) {
        println("The variable is equal to 4")
    } else {
        println("The variable is less than 4")
    }
}

val rollResult: Int = (1..6).random()
val luckyNumber: Int = 10
fun RollDice() {
    when (rollResult) {
        luckyNumber -> println("You won!")
        1 -> println("So sorry! You rolled a 1. Try again!")
        2 -> println("Sadly, you rolled a 2. Try again!")
        3 -> println("Unfortunately, you rolled a 3. Try again!")
        4 -> println("No luck! You rolled a 4. Try again!")
        5 -> println("Don't cry! You rolled a 5. Try again!")
        6 -> println("Apologies! you rolled a 6. Try again!")
    }
}

fun Drawable() {
    val diceRoll = (1..6).random()
    // Giả sử R.drawable đã có các ID này
    val drawableResource = when (diceRoll) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }
}

class Dice {
    var sides = 6
    fun roll() {
        println((1..6).random())
    }
}

class Dice2 (val numSides: Int) {
    fun roll(): Int = (1..numSides).random()
}

val myFirstDice = Dice2(6)

abstract class Dwelling(val residents: Int) {
    abstract val buildingMaterial: String
    abstract fun floorArea(): Double
}

open class RoundHut(residents: Int, val radius: Double) : Dwelling(residents) {
    override val buildingMaterial = "Straw"
    override fun floorArea(): Double = PI * radius * radius
}

class SquareCabin(residents: Int, val length: Double) : Dwelling(residents) {
    override val buildingMaterial = "Wood"
    override fun floorArea(): Double {
        return length * length
    }
}

fun list() {
    val numbers = listOf(1, 2, 3, 4, 5, 6)
    println(numbers.size)
    println(numbers[0])
    println(listOf("red", "blue", "green").reversed())
    val entrees = mutableListOf<String>()
    entrees.add("spaghetti")
    entrees[0] = "lasagna"
    entrees.remove("lasagna")
}

fun loop() {
    val myList = listOf(1, 2, 3 ,4, 5 ,6)
    for (element in myList) { println(element) }
    var index = 0
    while (index < myList.size) {
        println(myList[index])
        index++
    }
}

fun stringFun() {
    val name = "Android"
    println(name.length)
    val number = 10
    println("$number people")
    val numbers = 10
    val groups = 5
    println("${numbers * groups} people")
}

// val pi = kotlin.math.PI * radius * radius

fun sett() {
    val numbers = listOf(0, 3, 8, 4, 0, 5, 5, 8, 9, 2)
    val setOfNumbers = numbers.toSet()
    val set1 = setOf(1,2,3)
    val set2 = mutableSetOf(3, 4, 5)
    set1.intersect(set2)
    set1.union(set2)
}

fun mapp() {
    val peopleAges = mutableMapOf<String, Int>("Fred" to 30, "Ann" to 23)
    peopleAges.put("Barbara", 42)
    peopleAges["Joe"] = 51
    peopleAges.forEach { print("${it.key} is ${it.value}, ") }
    println(peopleAges.map { "${it.key} is ${it.value}" }.joinToString(", ") )
}

private var _currentScrambledWord = "test"
val currentScrambledWord: String get() = _currentScrambledWord

fun Lambda() {
    val triple: (Int) -> Int = { a: Int -> a * 3 }
    println(triple(5))
}

private var wordsList: MutableList<String> = mutableListOf()
private lateinit var currentWord: String

fun quanti() {
    var quantity : Int? = null
    val result1 = quantity ?: 0
    quantity = 4
    val result2 = quantity ?: 0
}

suspend fun getValue(): Double {
    delay(100)
    return 6.0
}

fun launch() {
    GlobalScope.launch {
        val output = getValue()
        println("Output coroutine: $output")
    }
}

enum class Direction { NORTH, SOUTH, WEST, EAST }

fun handleDirection(direction: Direction) {
    when (direction) {
        Direction.NORTH -> { }
        Direction.SOUTH -> { }
        Direction.WEST -> { }
        Direction.EAST -> { }
    }
}