from AccountManager import AccountManager
from datetime import datetime
from dateutil.parser import parse
import re

class UserInterface:
    def __init__(self, am):

        self.am = am
        self.maxDayInMonth = (0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

        n = '0'
        while n != 'x':
            n = input("dodaj wydatek: 1 \n" +
                      "pokaż statystyki : 2 \n" +
                      "zakończ: x \n" + "~>")

            if n == '1':
                self.addExpenditure()
            elif n == '2':
                self.showStatistics()
            elif n != 'x':
                print("niepoprawne dane")



    def showStatistics(self):
        N = input("pokaz statystyki z podanego roku: 1 \n" +
                  "pokaż statystyki z miesiaca: 2 \n" +
                  "pokaż statystyki z dnia: 3 \n" +
                  "pokaż statystyki z podanej kategorii: 4 \n" +
                  "pokaż statystyki z podanego okresu: 5 \n" +"~>")


        if N == '1':
            year = self.putYear()
            rows = self.am.getOperationsFromYear(year)
            sum = 0
            avarge = 0
            count = 0
            print("historia operacji:")
            if len(rows) > 0:
                for row in rows:
                    count = count + 1
                    sum = sum + row[1]
                    print(row)
                avarge = sum / count
                print("*****************************")
                print("srednia wydaykow w podanym miesiacu ", avarge)
                print("suma wydaykow w podanym miesiacu ", sum)
                print("*****************************")
                print("")
            else:
                print("brak operacji")
                print("**************")

        if N == '2':
            year = self.putYear()
            month = self.putMonth()

            rows = self.am.getOperationsFromMonth(year, month)
            sum = 0
            avarge = 0
            count = 0
            print("historia operacji:")
            if len(rows) > 0:
                for row in rows:
                    count = count + 1
                    sum = sum + row[1]
                    print(row)
                avarge = sum / count
                print("*****************************")
                print("srednia wydaykow w podanym miesiacu ", avarge)
                print("suma wydaykow w podanym miesiacu ", sum)
                print("*****************************")
                print("")
            else:
                print("brak operacji")
                print("**************")

        if N == '3':
            year = self.putYear()
            month = self.putMonth()
            day = self.putDay(month)
            rows = self.am.getOperationsFromDay(year, month, day)

            sum = 0
            avarge = 0
            count = 0
            print("historia operacji:")
            if len(rows)>0:
                for row in rows:
                    count = count + 1
                    sum = sum + row[1]
                    print(row)
                avarge = sum / count
                print("*****************************")
                print("srednia wydaykow w podanym miesiacu ", avarge)
                print("suma wydaykow w podanym miesiacu ", sum)
                print("*****************************")
                print("")
            else:
                print("brak operacji")
                print("**************")
        if N == '4':

            rows = self.am.getAllCategories()
            print("wybierz numer kategorii:", rows)

            x=0
            cathegoryExist = False

            while cathegoryExist == False:
                x = input('~>')
                cathegoryExist = self.am.doesCategoryIdExist(x)
                if cathegoryExist == False:
                    print("nie istnieje kategoria o takim numerze")

            rows = self.am.getOperationsWhereCategory(x)
            sum = 0
            avarge = 0
            count = 0
            print("historia operacji:")
            if len(rows) > 0:
                for row in rows:
                    count = count + 1
                    sum = sum + row[1]
                    print(row)
                avarge = sum / count
                print("*****************************")
                print("avarge ", avarge)
                print("sum ", sum)
                print("*****************************")
                print("")
            else:
                print("brak operacji")
                print("**************")

        if N == '5':
            print("podaj poczatek okresu:")
            year = self.putYear()
            month = self.putMonth()
            day = self.putDay(month)
            print("podaj koniec okresu:")
            year2 = self.putYear()
            month2 = self.putMonth()
            day2 = self.putDay(month)

            rows = self.am.getOperationsInSetInterval(year, month, day, year2, month2, day2)

            sum = 0
            avarge = 0
            count = 0
            print("historia operacji:")
            if len(rows) > 0:
                for row in rows:
                    count = count + 1
                    sum = sum + row[1]
                    print(row)
                avarge = sum / count
                print("*****************************")
                print("avarge ", avarge)
                print("sum ", sum)
                print("*****************************")
                print("")
            else:
                print("brak operacji")
                print("**************")








    def addExpenditure(self):

        valueIsFalse = True

        while valueIsFalse:
            valueIsFalse = False
            print("podaj wartosc wydatku:")
            value = input('~>')
            try:
                float(value)
            except ValueError:
                print("niepoprawny format danych, wpisz jeszcze raz")
                valueIsFalse = True


        year = self.putYear()
        month = self.putMonth()
        day = self.putDay(month)

        rows = self.am.getAllCategories()
        print("wybierz numer kategorii:", rows)
        print("dodaj wlasna kategorie wpisz N")

        x = 0
        cathegoryExist = False
        while cathegoryExist == False and x != 'N':
            x = input('~>')
            if x != 'N':
                cathegoryExist = self.am.doesCategoryIdExist(x)
            if cathegoryExist == False and x != 'N':
                print("nie istnieje kategoria o takim numerze")

        name = 'none'
        if x != 'N':
            (name) = self.am.getCategoryNameById(x)


        cathegoryExist = False
        if x == 'N':
            while cathegoryExist == False:
                print("podaj nazwe nowej kategorii:")
                name = input("~>")
                cathegoryExist = not self.am.doesCategoryNameAlreadyExist(name)
            self.am.addNewCategory(name)
            x = self.am.getCategoryIdByName(name)

        note = ""
        print("chcesz dodac notatke? Y/N:")
        yn = input("~>")
        if yn == 'Y':
            print("wpisz notatke:")
            note = input("~>")

        date = parse(str(year) + '-' + str(month) + '-' + str(day))
        date = date.timestamp()
        self.am.addExpenditure(float(value), x, date, note)
        print("dodano: ", value, "   ",datetime.fromtimestamp(date), "   " ,x, "  ", name, "  ", note)

    def putYear(self):
        is_match = False
        while is_match == False:
            is_match = True
            print("podaj rok rrrr > 1971:")
            year = input("~>")
            matched = re.match("[0-9][0-9][0-9][0-9]", year)

            if bool(matched) == False:
                is_match = False
            elif int(year) < 1971:
                is_match = False
            if is_match == False:
                print("niepoprawny rok")
        return year
    def putMonth(self):
        is_match = False
        while is_match == False:
            is_match = True

            print("podaj miesiac mm:")
            month = input("~>")
            matched = re.match("[0-9][0-9]", month)

            if bool(matched) == False:
                is_match = False
            elif int(month) < 1 or int(month) > 12:
                is_match = False
            if is_match == False:
                print("niepoprawnie podany miesiac")
        return month
    def putDay(self, month):
        is_match = False
        while is_match == False:
            is_match = True

            print("podaj dzien dd:")
            day = input("~>")
            matched = re.match("[0-9][0-9]", day)

            if bool(matched) == False:
                is_match = False
            elif int(day) <= 0 or int(day) > int(self.maxDayInMonth[int(month)]):
                is_match = False
            if is_match == False:
                print("niepoprawny podany dzien")
        return day
