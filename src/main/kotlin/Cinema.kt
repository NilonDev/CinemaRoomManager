package cinema

import kotlin.system.exitProcess

const val RELOAD_MENU_OPTION = -1

/**
 *  Создание схемы зала
 */
fun createHall(numberOfRows: Int, numberOfSeats:Int, hallScheme: MutableList<MutableList<Char>>) {
    for (i in 0..numberOfRows) {
        if (i == 0) {
            val firstRow: MutableList<Char> = mutableListOf()
            firstRow.add(' ')
            for (elem in 1..numberOfSeats) {
                firstRow.add(elem.digitToChar())
            }
            hallScheme.add(firstRow)
        } else {
            val otherRow = mutableListOf(i.digitToChar())
            for (elem in 1..numberOfSeats) {
                otherRow.add('S')
            }
            hallScheme.add(otherRow)
        }
    }
}

/**
 *  Покупка билета
 */
fun buyTicket(hallScheme: MutableList<MutableList<Char>>) {
    val numberOfRows = hallScheme.lastIndex             // количество рядов
    val numberOfSeats = hallScheme.first().lastIndex    // количество сидений
    val totalSeats = numberOfRows * numberOfSeats       // количество мест

    var choiceRows: Int                                 // Выбор ряда
    var choiceSeats: Int                                // Выбор сидения
    var ticketPrice = 0                                 // цена билета
    var buyTicket = false                               // успех / провал покупки билета

    // Ввод данных для выбора места
    try {
        println("Enter a row number:")
        choiceRows = readln().toInt()
        println("Enter a seat number in that row:")
        choiceSeats = readln().toInt()

    } catch (e: NumberFormatException) {
        choiceRows = 0
        choiceSeats = 0
        println(e.message)
    }

    while (buyTicket == false) {
        when {
            choiceRows !in 1..numberOfRows || choiceSeats !in 1..numberOfSeats -> {
                try {
                    println("\nWrong input!\n")
                    println("Enter a row number:")
                    choiceRows = readln().toInt()
                    println("Enter a seat number in that row:")
                    choiceSeats = readln().toInt()
                } catch (e: NumberFormatException) {
                    choiceRows = 0
                    choiceSeats = 0
                    //println(e.message)
                }
            }
            hallScheme[choiceRows][choiceSeats] == 'B' -> {
                try {
                    println("\nThat ticket has already been purchased!\n")
                    println("Enter a row number:")
                    choiceRows = readln().toInt()
                    println("Enter a seat number in that row:")
                    choiceSeats = readln().toInt()
                } catch (e: NumberFormatException) {
                    choiceRows = 0
                    choiceSeats = 0
                    //println(e.message)
                }
            }
            totalSeats in 1..60 -> {
                ticketPrice = 10
                hallScheme[choiceRows][choiceSeats] = 'B'
                buyTicket = true
            }
            totalSeats > 60 && choiceRows <= numberOfRows / 2 -> {
                ticketPrice = 10
                hallScheme[choiceRows][choiceSeats] = 'B'
                buyTicket = true
            }
            totalSeats > 60 && choiceRows > numberOfRows / 2 -> {
                ticketPrice = 8
                hallScheme[choiceRows][choiceSeats] = 'B'
                buyTicket = true
            }
        }
    }
    println()
    println("Ticket price: $$ticketPrice\n")
}

/**
 *  Вывод схемы зала
 */
fun showHall(hallScheme: MutableList<MutableList<Char>>) {
    println("Cinema:")
    for (str in hallScheme) {
        println(str.joinToString(" "))
    }
    println()
}

/**
 *  Меню программы
 */
fun showMenu() {
    println("1. Show the seats")
    println("2. Buy a ticket")
    println("3. Statistics")
    println("0. Exit")
}

/**
 *  Подсчет статистики
 */
fun showStatistic(hallScheme: MutableList<MutableList<Char>>) {
    val numberOfPurchasedTickets: Int                   // проданные билеты
    val percentage: Double                              // процент проданных билетов
    val formatPercentage: String                        // отформатированный процент
    val currentIncome: Int                              // текущая прибыль
    val totalIncome: Int                                // общая максимальная прибыль

    val numberOfRows = hallScheme.lastIndex             // ряды в зале
    val numberOfSeats = hallScheme.first().lastIndex    // сидения в зале

    numberOfPurchasedTickets = PurchasedTickets(hallScheme)
    percentage = numberOfPurchasedTickets / (numberOfSeats * numberOfRows).toDouble() * 100
    formatPercentage = "%.2f".format(percentage)
    currentIncome = currentIncome(hallScheme)
    totalIncome = totalIncome(numberOfRows, numberOfSeats)

    println("Number of purchased tickets: $numberOfPurchasedTickets")
    println("Percentage: $formatPercentage%")
    println("Current income: $$currentIncome")
    println("Total income: $$totalIncome")
    println()
}

/**
 *  Подсчет проданных билетов
 */
fun PurchasedTickets(hallScheme: MutableList<MutableList<Char>>): Int {
    var soldTickets = 0
    for (row in hallScheme) {
        for (seats in row) {
            if (seats == 'B') {
                soldTickets++
            }
        }
    }
    return soldTickets
}

/**
 *  Подсчет стоимочти проданных билетов
 */
fun currentIncome(hallScheme: MutableList<MutableList<Char>>): Int {
    val numberOfRows = hallScheme.lastIndex             // количество рядов
    val numberOfSeats = hallScheme.first().lastIndex    // количество сидений
    val totalSeats = numberOfRows * numberOfSeats       // всего мест в зале
    var sum = 0                                         // стоимость проданных билетов $

    when {
        totalSeats <= 60 -> {
            for (row in hallScheme) {
                for (seats in row) {
                    if (seats == 'B') {
                        sum += 10
                    }
                }
            }
        }
        totalSeats > 60 -> {
            for (i in 1..numberOfRows) {
                for (j in 1..numberOfSeats) {
                    if (i <= numberOfRows / 2 && hallScheme[i][j] == 'B') {
                        sum += 10
                    } else if (i > numberOfRows / 2 && hallScheme[i][j] == 'B') {
                        sum += 8
                    }
                }
            }
        }
    }
    return sum
}

/**
 *  Максимальная прибыль с зала
 */
fun totalIncome(numberOfRows: Int, numberOfSeats: Int): Int {
    var totalIncome = 0     //прибыль с зала
    if (numberOfRows * numberOfSeats <= 60) {
        totalIncome = numberOfRows * numberOfSeats * 10
    } else if (numberOfRows * numberOfSeats > 60){
        totalIncome = (numberOfRows / 2 * numberOfSeats * 10) +
                ((numberOfRows - (numberOfRows / 2)) * numberOfSeats * 8)
    }
    return totalIncome
}

fun main() {
    val hallScheme: MutableList<MutableList<Char>> = mutableListOf()   // Схема зала
    var numberOfRows = 0                                               // Количество рядов
    var numberOfSeats = 0                                              // Количество сидений
    var menuOption = RELOAD_MENU_OPTION                                // Выбор действия в меню кинотеатра
    var correctInput = false

    // Ввод данных для рисования карты

    while (correctInput == false) {
        try {
            println("Enter the number of rows:")
            numberOfRows = readln().toInt()
            println("Enter the number of seats in each row:")
            numberOfSeats = readln().toInt()
            println()

            if (numberOfRows in 1..9 && numberOfSeats in 1..9) {
                correctInput = true
            } else {
                println("number must be in the range 1..9")
                println("Wrong input!\n")
            }

        } catch (e: NumberFormatException) {
            println()
            println(e.message)
            println("Wrong input!\n")
        }
    }

    createHall(numberOfRows, numberOfSeats, hallScheme)

    while (true) {
        showMenu()
        menuOption = try { readln().toInt() } catch (e: Exception) { RELOAD_MENU_OPTION }
        println()

        when(menuOption) {
            1 -> showHall(hallScheme)
            2 -> buyTicket(hallScheme)
            3 -> showStatistic(hallScheme)
            0 -> break
        }
    }
}